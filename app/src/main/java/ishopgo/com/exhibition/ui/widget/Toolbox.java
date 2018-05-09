package ishopgo.com.exhibition.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class Toolbox {
    private static final String TAG = "MDT";
    public static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
    public static final SimpleDateFormat displayDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("vi", "VN"));
    public static final SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("vi", "VN"));
    private static Gson defaultGson;

    static {
        defaultGson = new Gson();
    }

    public static Gson getDefaultGson() {
        return defaultGson;
    }

    public static boolean exceedSize(Context context, Uri uri, long maxSize) {
        boolean isExceed = false;
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        String fileName = null;
        try {
            if (query != null && query.moveToFirst()) {
                fileName = query.getString(query.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Long size = query.getLong(query.getColumnIndex(OpenableColumns.SIZE));
                if (size > maxSize) {
                    isExceed = true;
                }
            }
        } finally {
            if (query != null) query.close();
        }
        if (fileName == null) {
            fileName = uri.getPath();
            File file = new File(fileName);
            if (file.length() > maxSize) {
                isExceed = true;
            }
        }

        return isExceed;
    }

    public static String formatApiDateTime(String apiTime) {
        try {
            return Toolbox.displayDateTimeFormat.format(apiDateFormat.parse(apiTime));
        } catch (ParseException e) {
            return apiTime;
        }
    }

    public static String formatApiDate(String apiTime) {
        try {
            return Toolbox.displayDateFormat.format(apiDateFormat.parse(apiTime));
        } catch (ParseException e) {
            return apiTime;
        }
    }

    public static void reEncodeBitmap(Context context, Uri sourceUri, int targetSize, Uri destinationUri) throws IOException {
        Log.d(TAG, "reEncodeBitmap: source: " + sourceUri);
        Log.d(TAG, "reEncodeBitmap: targetSize: " + targetSize);
        Log.d(TAG, "reEncodeBitmap: destination: " + destinationUri);

        ArrayList<Closeable> closeables = new ArrayList<>();
        File sourceFile = null;

        try {
            // Open stream
            InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
            closeables.add(inputStream);

            // Copy to cache
            sourceFile = new File(context.getCacheDir(), UUID.randomUUID().toString());
            FileUtils.copyInputStreamToFile(inputStream, sourceFile);

            // Get size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(sourceFile.getCanonicalPath(), options);
            Log.d(TAG, "reEncodeBitmap: size: " + options.outWidth + " x " + options.outHeight);

            // Setting output size
            int orgMaxWidth = Math.max(options.outWidth, options.outHeight);
            if (orgMaxWidth > targetSize) {
                // Size is bigger than target size, resize the bitmap
                options.inSampleSize = Math.round((float) orgMaxWidth / targetSize);
                Log.d(TAG, "reEncodeBitmap: inSampleSize = " + options.inSampleSize);
            }
            options.inJustDecodeBounds = false;

            // Decode
            Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getCanonicalPath(), options);

            // Exif interface
            ExifInterface exif = new ExifInterface(sourceFile.getCanonicalPath());
            sourceFile.delete();
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Log.d(TAG, "reEncodeBitmap: orientation = " + orientation);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_FLIP_HORIZONTAL");
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_ROTATE_180");
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_FLIP_VERTICAL");
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_TRANSPOSE");
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_ROTATE_90");
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_TRANSVERSE");
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.d(TAG, "reEncodeBitmap: orientation = " + "ORIENTATION_ROTATE_270");
                    matrix.setRotate(-90);
                    break;
            }

            // Rotate
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Compress
            OutputStream outputStream = context.getContentResolver().openOutputStream(destinationUri);
            closeables.add(outputStream);
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);

        } finally {
            for (Closeable closeable : closeables) {
                IOUtils.closeQuietly(closeable);
            }
            FileUtils.deleteQuietly(sourceFile);
        }
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }


}
