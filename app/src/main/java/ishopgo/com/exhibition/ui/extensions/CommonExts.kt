package ishopgo.com.exhibition.ui.extensions

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import java.io.PrintWriter
import java.io.StringWriter
import java.text.NumberFormat
import java.util.*


/**
 * Created by xuanhong on 11/6/17. HappyCoding!
 */
fun Throwable.showStackTrace(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    this.printStackTrace(pw)
    return sw.toString()
}

fun Int.asColor(context: Context): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.resources.getColor(this, context.theme)
    else context.resources.getColor(this)
}

fun String.asNumber(hour: Double): String? {
    var total_sec = hour * 60.0 * 60.0
    var i = 4
    val number = IntArray(4)
    while (i > 0) {
        i--
        number[i] = (total_sec % 60).toInt()
        total_sec /= 60.0
    }

    val builder = StringBuilder()
    if (number[0] > 0) builder.append(number[0]).append(" ngày ")
    if (number[1] > 0) builder.append(number[1]).append(" giờ ")
    if (number[2] > 0) builder.append(number[2]).append(" phút ")
    if (number[3] > 0) builder.append(number[3]).append(" giây ")
    return if (builder.isEmpty()) "0" else builder.toString()
}

fun String.asDate(): String? {
    return try {
        Toolbox.displayDateFormat.format(Toolbox.apiDateFormat.parse(this))
    } catch (e: Exception) {
        this
    }
}

fun String.asDateTime(): String? {
    return try {
        Toolbox.displayDateTimeFormat.format(Toolbox.apiDateFormat.parse(this))
    } catch (e: Exception) {
        this
    }
}

fun Long.asMoney(): String {
    val numberFormat = NumberFormat.getIntegerInstance(Locale("vi"))
    val formatted = numberFormat.format(this)
    return formatted + " đ"
}

fun Long.asNumber(): String? {
    var i = 3
    var sec = this
    val number = IntArray(3)
    while (i > 0) {
        i--
        sec /= 60
        number[i] = (sec % 60).toInt()
    }

    val builder = StringBuilder()
    if (number[0] > 0) builder.append(number[0]).append(" ngày ")
    if (number[1] > 0) builder.append(number[1]).append(" giờ ")
    if (number[2] > 0) builder.append(number[2]).append(" phút")
    return if (builder.isEmpty()) "0" else builder.toString()
}

fun String.asHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    else Html.fromHtml(this)

}

fun Int.dpToPx(ctx: Context): Int {
    val displayMetrics = ctx.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics) as Int
}

fun Int.pxToDp(ctx: Context): Float {
    val displayMetrics = ctx.resources.displayMetrics
    return this.toFloat() / (displayMetrics.densityDpi / 160f)
}
