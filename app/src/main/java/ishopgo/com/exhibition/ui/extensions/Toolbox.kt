package ishopgo.com.exhibition.ui.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
object Toolbox {
    const val TAG = "Toolbox"

    private val defaultGson = Gson()

    fun getDefaultGson(): Gson {
        return defaultGson
    }

    val LOCALE_VN = Locale("vi", "VN")
    val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", LOCALE_VN)
    val displayDateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", LOCALE_VN)
    val apiDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", LOCALE_VN)

    val gson = defaultGson

    fun exceedSize(context: Context, uri: Uri, maxSize: Long): Boolean {
        var isExceed = false
        val query = context.contentResolver.query(uri, null, null, null, null)
        var fileName: String? = null
        try {
            if (query != null && query.moveToFirst()) {
                fileName = query.getString(query.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val size = query.getLong(query.getColumnIndex(OpenableColumns.SIZE))
                if (size > maxSize) {
                    isExceed = true
                }
            }
        } finally {
            query?.close()
        }
        if (fileName == null) {
            fileName = uri.path
            val file = File(fileName!!)
            if (file.length() > maxSize) {
                isExceed = true
            }
        }

        return isExceed
    }

    fun formatApiDateTime(apiTime: String): String {
        try {
            return Toolbox.displayDateTimeFormat.format(apiDateFormat.parse(apiTime))
        } catch (e: ParseException) {
            return apiTime
        }

    }

    fun formatApiDate(apiTime: String): String {
        try {
            return Toolbox.displayDateFormat.format(apiDateFormat.parse(apiTime))
        } catch (e: ParseException) {
            return apiTime
        }

    }

    @Throws(IOException::class)
    fun reEncodeBitmap(context: Context, sourceUri: Uri, targetSize: Int, destinationUri: Uri) {
        Log.d(TAG, "reEncodeBitmap: source: $sourceUri")
        Log.d(TAG, "reEncodeBitmap: targetSize: $targetSize")
        Log.d(TAG, "reEncodeBitmap: destination: $destinationUri")

        val closeables = ArrayList<Closeable>()
        var sourceFile: File? = null

        try {
            // Open stream
            val inputStream = context.contentResolver.openInputStream(sourceUri)
            closeables.add(inputStream)

            // Copy to cache
            sourceFile = File(context.cacheDir, UUID.randomUUID().toString())
            FileUtils.copyInputStreamToFile(inputStream!!, sourceFile)

            // Get size
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(sourceFile.canonicalPath, options)
            Log.d(TAG, "reEncodeBitmap: size: " + options.outWidth + " x " + options.outHeight)

            // Setting output size
            val orgMaxWidth = Math.max(options.outWidth, options.outHeight)
            if (orgMaxWidth > targetSize) {
                // Size is bigger than target size, resize the bitmap
                options.inSampleSize = Math.round(orgMaxWidth.toFloat() / targetSize)
                Log.d(TAG, "reEncodeBitmap: inSampleSize = " + options.inSampleSize)
            }
            options.inJustDecodeBounds = false

            // Decode
            val bitmap = BitmapFactory.decodeFile(sourceFile.canonicalPath, options)

            // Exif interface
            val exif = ExifInterface(sourceFile.canonicalPath)
            sourceFile.delete()
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            Log.d(TAG, "reEncodeBitmap: orientation = $orientation")

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_FLIP_HORIZONTAL")
                    matrix.setScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_ROTATE_180")
                    matrix.setRotate(180f)
                }
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_FLIP_VERTICAL")
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_TRANSPOSE")
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_ROTATE_90")
                    matrix.setRotate(90f)
                }
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_TRANSVERSE")
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_ROTATE_270")
                    matrix.setRotate(-90f)
                }
            }

            // Rotate
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            // Compress
            val outputStream = context.contentResolver.openOutputStream(destinationUri)
            closeables.add(outputStream)
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)

        } finally {
            for (closeable in closeables) {
                IOUtils.closeQuietly(closeable)
            }
            FileUtils.deleteQuietly(sourceFile)
        }
    }
}