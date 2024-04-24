package com.matttax.drivebetter.profile.presentation.avatar

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream

object BitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        val inputStream: InputStream?
        return try {
            inputStream = contentResolver.openInputStream(uri)
            val decoded = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            decoded
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
