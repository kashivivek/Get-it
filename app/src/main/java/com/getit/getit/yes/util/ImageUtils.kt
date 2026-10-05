package com.getit.getit.yes.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.min

object ImageUtils {

    /** Center-crops, downsizes and JPEG-compresses an image so it fits comfortably in a Firestore document. */
    suspend fun avatarJpeg(context: Context, uri: Uri, size: Int = 320, quality: Int = 80): ByteArray =
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri).use { BitmapFactory.decodeStream(it, null, bounds) }

            var sample = 1
            while (min(bounds.outWidth, bounds.outHeight) / (sample * 2) >= size) sample *= 2
            val decoded = resolver.openInputStream(uri).use {
                BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
            } ?: error("Unsupported image")

            val rotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                resolver.openInputStream(uri).use { stream -> stream?.let { exifRotation(ExifInterface(it)) } ?: 0 }
            } else {
                0
            }

            val side = min(decoded.width, decoded.height)
            val scale = size.toFloat() / side
            val matrix = Matrix().apply {
                postScale(scale, scale)
                postRotate(rotation.toFloat())
            }
            val cropped = Bitmap.createBitmap(
                decoded, (decoded.width - side) / 2, (decoded.height - side) / 2, side, side, matrix, true
            )
            ByteArrayOutputStream().use { out ->
                cropped.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.toByteArray()
            }
        }

    private fun exifRotation(exif: ExifInterface): Int =
        when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
}
