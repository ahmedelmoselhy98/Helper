package com.chefshub.data.utils

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
//
//fun convertToRequestBodyPart(
//    key: String,
//    bitmap: Bitmap?
//): MultipartBody.Part? {
//    if (bitmap == null) {
//        return null
//    } else {
//        val requestBody = getFileDataFromBitmap(bitmap)?.toRequestBody("image/png".toMediaTypeOrNull(), 0, it.size
//        )
//        return MultipartBody.Part.createFormData(key, "$key.png", requestBody!!)
//    }
//}



fun convertToRequestBodyPart(
    key: String,
    bitmap: Bitmap?
): MultipartBody.Part? {
    return if (bitmap == null) null else {
        getFileDataFromBitmap(bitmap)?.let {
            it
                .toRequestBody(
                    "image/png".toMediaTypeOrNull(),
                    0, it.size
                )
        }?.let {
            MultipartBody.Part.createFormData(
                key,
                "$key.png",
                it
            )
        }
    }
}

private fun getFileDataFromBitmap(bitmap: Bitmap): ByteArray? {
    val stream = ByteArrayOutputStream()

    // Compress the bitmap into the stream as a JPEG image with quality 50
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

    // Convert the stream to a byte array
    return stream.toByteArray()
}


fun convertToRequestBody(part: String): RequestBody? {
    return if (part.isNullOrEmpty()) {
        null
    } else {
        part.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }
}

