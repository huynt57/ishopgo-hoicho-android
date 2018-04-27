package ishopgo.com.exhibition.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import java.io.File;

public class Toolbox {

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

}
