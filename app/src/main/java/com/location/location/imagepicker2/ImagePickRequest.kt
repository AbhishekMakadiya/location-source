package com.location.location.imagepicker2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.location.location.R
import com.location.location.constants.Const
import com.location.location.utils.LogHelper.e
import com.location.location.utils.LogHelper.printStackTrace
import com.location.location.utils.LogHelper.v
import com.location.location.utils.Util.message
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.FileUtils.getPath
import java.io.*

class ImagePickRequest private constructor(`object`: Any) {
    private var mResult: ImagePickResult? = null
    private val mContext: Activity = `object` as Activity
    private val TAG = ImagePickRequest::class.java.simpleName
    private var isCameraImage = false
    private var capturedImageUri: Uri? = null
    private var CAMERA_PIC_REQUEST = 1212
    private var REQUEST_CODE = 1313
    private var allowCropping = false
    var IMAGE_WIDTH = 1000
    var IMAGE_HEIGHT = 1300
    var FORMAT = 1
    var progressDialog: ProgressDialog? = null
    var aspectRatioX = 0f
    var aspectRadioY = 0f
    var imagePicRequestResultData: ImagePicRequestResultData = ImagePicRequestResultData()
    var forceUniqueName = true
    var currentTimeInMillis: Long = 0
    var isHideCroppingControls: Boolean = false

    fun allowCropping(allowCropping: Boolean): ImagePickRequest {
        this.allowCropping = allowCropping
        return this
    }

    fun forceUniqueName(forceUniqueName: Boolean): ImagePickRequest {
        this.forceUniqueName = forceUniqueName
        return this
    }

    fun setIsHideCroppingControls(isHideCroppingControls: Boolean): ImagePickRequest {
        this.isHideCroppingControls = isHideCroppingControls
        return this
    }

    fun setAspectRatio(x: Float, y: Float): ImagePickRequest {
        aspectRatioX = x
        aspectRadioY = y
        return this
    }

    fun initializeVariable() {
        isCameraImage = false
        capturedImageUri = null
        CAMERA_PIC_REQUEST = 1212
        REQUEST_CODE = 1313
        allowCropping = false
    }

    fun isCameraImage(isCameraImage: Boolean): ImagePickRequest {
        this.isCameraImage = isCameraImage
        return this
    }

    fun setRequestCode(requestCode: Int): ImagePickRequest {
        REQUEST_CODE = requestCode
        return this
    }

    fun setCameraRequestCode(requestCode: Int): ImagePickRequest {
        CAMERA_PIC_REQUEST = requestCode
        return this
    }

    fun result(result: ImagePickResult?): ImagePickRequest {
        mResult = result
        return this
    }

    fun request() { /*Activity activity = getActivity(mObject);
        if (activity == null) {
            throw new IllegalArgumentException(mObject.getClass().getName() + " is not supported");
        }*/
        progressDialog = getProgressDialog(mContext)
        currentTimeInMillis = System.currentTimeMillis() //Set time Here. So that it won't unNecessary create duplicate files and overwrite same file.
        if (isCameraImage) {
            //takePicture();
        } else {
            startActivityToSelectImage()
        }
    }

    private fun getActivity(`object`: Any?): Activity? {
        if (`object` != null) {
            if (`object` is Activity) {
                return `object`
            } else if (`object` is Fragment) {
                return `object`.activity
            }
        }
        return null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        e(TAG, "onActivityResult")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                try {
                    if (data != null && data.data != null) {
                        val dirs = File(mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM), setFileNameBasedONInternet(REQUEST_CODE) + ".jpg")
                        SaveImageAsync().execute(dirs, data.data)
                    }
                } catch (e: Exception) {
                    printStackTrace(e)
                }
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                try {
                    if (capturedImageUri == null) return
                    if (allowCropping) {
                        startCropActivity(data!!.data)
                    } else {
                        val path = capturedImageUri!!.path
                        setOriginalFileName(path)
                        setImage(path, true)
                    }
                } catch (e: Exception) {
                    printStackTrace(e)
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                try {
                    val resultUri = UCrop.getOutput(data!!)
                    val filepath = getPath(mContext, resultUri!!)
                    setOriginalFileName(filepath)
                    setImage(filepath, true)
                } catch (e: Exception) {
                    printStackTrace(e)
                }
            }
        }
    }

    private fun startActivityToSelectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        mContext.startActivityForResult(Intent.createChooser(intent, null), REQUEST_CODE)
    }

    fun setImage(filePath: String?, compressImage: Boolean) {
        if (filePath == null || filePath.isEmpty()) {
            e(TAG, "filePath is null or empty")
            if (mResult != null) {
                mResult!!.onFail("filePath is null or empty")
            }
            return
        }
        val fileToVerify = File(filePath)
        e(TAG, fileToVerify.absolutePath)
        e(TAG, "file size=" + fileToVerify.length())
        //!compressImage means if we compress image and then come back and again size is more then 2MB then display toast.
        if (isValidFileSize(mContext, filePath, !compressImage)) {
            if (mResult != null) {
                imagePicRequestResultData.requestCode =
                    if (isCameraImage) CAMERA_PIC_REQUEST else REQUEST_CODE
                imagePicRequestResultData.filePath = filePath
                imagePicRequestResultData.isCompressedImage = allowCropping
                mResult!!.onSuccess(imagePicRequestResultData)
            }
        } else if (compressImage) { //Once we already compress then no need to compress again.
            imagePicRequestResultData.isCompressedImage = true
            MyAsync().execute(filePath)
        } else { //If we compress image and again its large then 2 MB then delete it.
            deleteFile(filePath)
        }
        sendBroadCastToScanner(filePath)
    }

    fun compressImage(filePath: String?): String? {
        var scaledBitmap: Bitmap
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outWidth
        var actualWidth = options.outHeight
        val maxWidth = IMAGE_WIDTH.toFloat()
        val maxHeight = IMAGE_HEIGHT.toFloat()
        var imgRatio = actualWidth / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        scaledBitmap = try {
            Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
            return null
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                bmp, 0, 0,
                bmp.width, bmp.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out: FileOutputStream
        if (mContext.getExternalFilesDir("Temp") == null) {
            return null
        }
        val myDir = mContext.getExternalFilesDir("Temp")
        if (myDir == null) {
            e(TAG, "problem with creating Temp Directory")
            return null
        }
        /*  if specified not exist create new */if (!myDir.exists()) {
            val success = myDir.mkdir()
            if (!success) {
                e(TAG, "problem with creating Temp Directory")
            }
            v("", "inside mkdir")
        }
        val tempFile = File(myDir, setFileNameBasedONInternet(REQUEST_CODE) + ".jpg")
        try {
            out = FileOutputStream(tempFile)
            if (FORMAT == 1) scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) //70
            else scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 100, out) //70
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return tempFile.absolutePath
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio =
                Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio =
                Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = heightRatio
        }
        val totalPixels = width * height.toFloat()
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    internal inner class   MyAsync :
        AsyncTask<String?, Void?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog!!.show()
        }

        protected override fun doInBackground(vararg params: String?): String? { /*try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            return compressImage(params[0])
        }

        override fun onPostExecute(filePath: String?) {
            super.onPostExecute(filePath)
            progressDialog!!.dismiss()
            setImage(filePath, false)
        }
    }

    //Camera
  /*  void takePicture() {
        File filePhoto = new File(String.valueOf(Util.getOutputMediaFile(mContext, CAMERA_PIC_REQUEST)));
        capturedImageUri = Uri.fromFile(filePhoto);
        Intent takePictureIntent = new Intent(mContext, CameraActivity.class);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            isCameraImage = true;
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri.getPath());
            mContext.startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
        }
    }*/
    private fun sendBroadCastToScanner(filePath: String?) { // Media scanner need to scan for the image saved
        val mediaScannerIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val fileContentUri = Uri.fromFile(File(filePath))
        mediaScannerIntent.data = fileContentUri
        mContext.sendBroadcast(mediaScannerIntent)
    }

    private fun deleteFile(filePath: String?) {
        try {
            val file = File(filePath)
            file.delete()
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    fun deleteFileWithBroadCast(filePath: String?) {
        deleteFile(filePath)
        try { /*// Media scanner need to scan for the image delete. So that is can be deleted from gallery.
            Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath)));
            mContext.sendBroadcast(mediaScannerIntent);*/
//Above broadCast code not work to remove image from gallery so here is different code.
//            https://stackoverflow.com/questions/10716642/android-deleting-an-image
            mContext.contentResolver.delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Files.FileColumns.DATA + "=?", arrayOf(filePath)
            )
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    private fun setOriginalFileName(path: String?) {
        imagePicRequestResultData.originalFileName = File(path).name
    }

    internal inner class SaveImageAsync :
        AsyncTask<Any?, Void?, ImageSaverModel?>() {
        var file: File? = null
        override fun doInBackground(vararg objects: Any?): ImageSaverModel? {
            var uri: Uri? = null
            if (objects[0] is File) {
                file = objects[0] as File
            }
            if (objects[1] is Uri) {
                uri = objects[1] as Uri
            }
            return try {
                copyFileStream(file, uri)
            } catch (e: IOException) {
                printStackTrace(e)
                ImageSaverModel(false, e.message)
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog!!.show()
        }

        override fun onPostExecute(result: ImageSaverModel?) {
            super.onPostExecute(result)
            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()
            if (result != null) {
                if (result.isSuccess) {
                    if (allowCropping) {
                        startCropActivity(Uri.fromFile(file))
                    } else {
                        setImage(file!!.absolutePath, true)
                    }
                } else {
                    message(mContext, result.message)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun copyFileStream(dest: File?, uri: Uri?): ImageSaverModel { /*
         * Never display any toast here. Because this method is called from async.
         * Also don't update any UI here.
         * */
        var `is`: InputStream? = null
        var os: OutputStream? = null
        val imageSaverModel = ImageSaverModel(true)
        try {
            `is` = mContext.contentResolver.openInputStream(uri!!)
            os = FileOutputStream(dest)
            val buffer = ByteArray(1024)
            var length: Int
            while (`is`!!.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
        } catch (e: Exception) {
            printStackTrace(e)
            imageSaverModel.isSuccess = false
            imageSaverModel.message = e.message
        } finally {
            `is`?.close()
            os?.close()
        }
        return imageSaverModel
    }

    inner class ImageSaverModel {
        var isSuccess: Boolean
        var message: String? = null

        constructor(isSuccess: Boolean) {
            this.isSuccess = isSuccess
        }

        constructor(isSuccess: Boolean, message: String?) {
            this.isSuccess = isSuccess
            this.message = message
        }

    }

    private fun startCropActivity(uri: Uri?) {
        var uCrop = UCrop.of(uri!!, uri)
        val option = UCrop.Options()
        option.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        option.setCompressionQuality(100)
        if (isHideCroppingControls) {
            option.setHideBottomControls(true)
            option.setFreeStyleCropEnabled(false)
        }
        if (aspectRatioX > 0f && aspectRadioY > 0f) {
            uCrop = uCrop.withAspectRatio(aspectRatioX, aspectRadioY)
        } else {
            option.setFreeStyleCropEnabled(true)
        }

        uCrop = uCrop.withOptions(option)
        uCrop.start(mContext)
    }

    companion object {
        fun with(activity: Activity): ImagePickRequest {
            return ImagePickRequest(activity)
        }

        fun getProgressDialog(mContext: Context): ProgressDialog {
            val dialog = ProgressDialog(mContext)
            val drawable = ProgressBar(mContext).indeterminateDrawable
            drawable.setColorFilter(
                mContext.resources.getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP
            )
            dialog.setIndeterminateDrawable(drawable)
            dialog.setMessage(mContext.getString(R.string.prompt_please_wait))
            dialog.isIndeterminate = true
            dialog.setCancelable(false)
            return dialog
        }

        fun isValidFileSize(
            mContext: Context,
            filePath: String?,
            displayToast: Boolean
        ): Boolean {
            return try {
                val fileToVerify = File(filePath)
                if (fileToVerify.length() > Const.MAXIMUM_IMAGE_SIZE_TO_UPLOAD) {
                    if (displayToast) message(
                        mContext,
                        mContext.getString(R.string.prompt_max_image_file_size)
                    )
                    false
                } else true
            } catch (e: Exception) {
                printStackTrace(e)
                false
            }
        }
    }

    private fun setFileNameBasedONInternet(requestCode: Int): String {
        //If not connected to internet then use different name for offline syncing.so that image not override.
        //Also set unique name if user force to set uniqueName
        return if (forceUniqueName)
            currentTimeInMillis.toString()
        else
            requestCode.toString()
    }

}