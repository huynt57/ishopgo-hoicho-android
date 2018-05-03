package ishopgo.com.exhibition.ui.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class Toolbox {

    companion object {
        val LOCALE_VN = Locale("vi", "VN")
        val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", LOCALE_VN)
        val displayDateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", LOCALE_VN)
        val apiDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", LOCALE_VN)
    }
}