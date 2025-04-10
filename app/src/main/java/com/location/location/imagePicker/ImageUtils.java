package com.location.location.imagePicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.location.location.constants.Const;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public final class ImageUtils {

    private static final String BASE_IMAGE_NAME = "i_prefix_";

    private ImageUtils() {
    }

    public static String savePicture(Context context, Bitmap bitmap, String imageSuffix) {
        File savedImage = getTemporalFile(context, imageSuffix);
        FileOutputStream fos = null;
        if (savedImage.exists()) {
            savedImage.delete();
        }
        try {
            fos = new FileOutputStream(savedImage.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedImage.getAbsolutePath();
    }
    public static File savePictureFile(Context context, Bitmap bitmap, String imageSuffix) {
        File savedImage = getTemporalFile(context, imageSuffix);
        FileOutputStream fos = null;
        if (savedImage.exists()) {
            savedImage.delete();
        }
        try {
            fos = new FileOutputStream(savedImage.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedImage;
    }

    public static File savePictureFile1(Context context, Bitmap bitmap) {
        File savedImage = getTemporalUniqueFile(context, Const.DIR_TEMP_IMAGE_FILE);
        FileOutputStream fos = null;
        if (savedImage.exists()) {
            savedImage.delete();
        }
        try {
            fos = new FileOutputStream(savedImage.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedImage;
    }

    public static File getTemporalFile(Context context) {
       /* File docsFolder = new File(Environment.getExternalStorageDirectory() + "/AMC");
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir();
        }
        if (isPresent) {
            return new File(docsFolder.getAbsolutePath(), BASE_IMAGE_NAME + System.currentTimeMillis() + ".jpeg");
        } else {
            return new File(context.getExternalCacheDir(), BASE_IMAGE_NAME + System.currentTimeMillis() + ".jpeg");
        }*/
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        ), BASE_IMAGE_NAME + System.currentTimeMillis() + ".jpeg");
    }

    public static File getTemporalFile(Context context, String payload) {
        /*File storageDir = ;
        return new File(storageDir, BASE_IMAGE_NAME + payload + ".jpeg");*/
        return new File(context.getExternalCacheDir(), BASE_IMAGE_NAME + payload + ".jpeg");
    }

    public static File getTemporalUniqueFile(Context context, String dirName) {
        /*File storageDir = ;
        return new File(storageDir, BASE_IMAGE_NAME + payload + ".jpeg");*/

        File mediaStorageDir = new File(
                context.getExternalCacheDir(),
                File.separator + dirName
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return new File(context.getExternalCacheDir(), dirName+ File.separator + BASE_IMAGE_NAME + System.currentTimeMillis() + ".png");
    }
}
