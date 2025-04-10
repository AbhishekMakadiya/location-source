package com.location.location.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {

    fun saveFileFromUri(mContext: Context, sourceUri: Uri, isSending: Boolean): Uri? {
        val mimeType = getMimeType(mContext, sourceUri)
        val fileExtension = getFileExtension(mContext, sourceUri, mimeType)
        val fileName = "${System.currentTimeMillis()}.$fileExtension"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveFileScopedStorage(mContext, sourceUri, fileName, mimeType ?: "application/octet-stream")
        } else {
            saveFileLegacyStorage(mContext, sourceUri, fileName, mimeType ?: "application/octet-stream")
        }
    }

    private fun saveFileLegacyStorage(mContext: Context, sourceUri: Uri, fileName: String, mimeType: String): Uri? {
        val baseDir = getPrivateMediaFullPathLegacy(mContext, mimeType)
        val file = File(baseDir, fileName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        return try {
            mContext.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun saveFileScopedStorage(mContext: Context, sourceUri: Uri, fileName: String, mimeType: String): Uri? {
        val baseDir = getPrivateMediaFullPathScoped(mContext, mimeType)
        val file = File(baseDir, fileName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        return try {
            mContext.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getPrivateMediaFullPathLegacy(mContext: Context, mimeType: String): String {
        val mediaType = getMediaType(mimeType)
        return File(mContext.getExternalFilesDir(null), "Media/$mediaType/Sent").absolutePath
    }

    private fun getPrivateMediaFullPathScoped(mContext: Context, mimeType: String): String {
        val mediaType = getMediaType(mimeType)
        return File(mContext.getExternalFilesDir(null), "Media/$mediaType/Sent").absolutePath
    }

    private fun getMimeType(mContext: Context, uri: Uri): String? {
        return mContext.contentResolver.getType(uri)
    }

    private fun getFileExtension(mContext: Context, uri: Uri, mimeType: String?): String {
        val cursor = mContext.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val extension = displayName.substringAfterLast('.', "")
                if (extension.isNotEmpty()) {
                    return extension
                }
            }
        }

        return when (mimeType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            "video/mp4" -> "mp4"
            "video/3gpp" -> "3gp"
            "audio/mpeg" -> "mp3"
            "audio/wav" -> "wav"
            else -> "dat"
        }
    }

    private fun getMediaType(mimeType: String): String {
        return when {
            mimeType.startsWith("image/") -> "image"
            mimeType.startsWith("video/") -> "video"
            mimeType.startsWith("audio/") -> "audio"
            else -> "Others"
        }
    }
}