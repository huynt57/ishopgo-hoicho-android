package ishopgo.com.exhibition.ui.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.google.i18n.phonenumbers.PhoneNumberUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.PrintWriter
import java.io.StringWriter
import java.text.NumberFormat
import java.util.*
import android.text.style.ClickableSpan
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity


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

fun String.asPhone(): String? {
    val phoneUtil = PhoneNumberUtil.getInstance()
    val phoneNumber = try {
        phoneUtil.parse(this, "VN")
    } catch (e: Exception) {
        null
    }
    return if (phoneNumber != null && phoneUtil.isValidNumber(phoneNumber)) phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164) else this
}

fun String.asDate(): String? {
    return try {
        Toolbox.displayDateFormat.format(Toolbox.apiDateFormat.parse(this))
    } catch (e: Exception) {
        this
    }
}

fun String.asDateProdcutDetail(): String? {
    return try {
        Toolbox.displayDateFormat.format(Toolbox.apiDateProductFormat.parse(this))
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

fun String.asConvertUnixtime(): String? {
    return try {
        val date = Date(this.toLong() * 1000L)
        Toolbox.displayDateTimeFormat.format(date)
    } catch (e: Exception) {
        this
    }
}

fun Long.asMoney(): String {
    val numberFormat = NumberFormat.getIntegerInstance(Locale("vi"))
    val formatted = numberFormat.format(this)
    return "$formatted đ"
}

fun Double.asMoney(): String {
    val numberFormat = NumberFormat.getIntegerInstance(Locale("vi"))
    val formatted = numberFormat.format(this)
    return "$formatted đ"
}

fun TextView.setPhone(phone: CharSequence, phoneNumber: String) {
    this.text = phone.setPhone(phoneNumber)
    this.movementMethod = LinkMovementMethod.getInstance()
}

fun CharSequence.setPhone(phoneNumber: String): CharSequence {
    val spannable = SpannableString(this)
    if (this.isNotEmpty() && phoneNumber.isNotEmpty()) {
        val positionStart = if (this.indexOf(phoneNumber.substring(0)) > 0) this.indexOf(phoneNumber.substring(0)) else 0
        val positionEnd = positionStart + (phoneNumber.length)
        spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                positionStart, positionEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
                UnderlineSpan(),
                positionStart, positionEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                //what happens whe i click
                val uri = Uri.parse("tel:$phoneNumber")
                val i = Intent(Intent.ACTION_DIAL, uri)
                view.context.startActivity(i)
            }
        }
        spannable.setSpan(clickableSpan,
                positionStart, positionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannable
}

fun CharSequence.setBoothName(text: String, boothId: Long): CharSequence {
    val spannable = SpannableString(this)
    if (this.isNotEmpty()) {
        val positionStart = this.indexOf(text.substring(0))
        val positionEnd = this.indexOf(text.substring(0)) + (text.length)
        spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                positionStart, positionEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
                BOLD,
                positionStart, positionEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                //what happens whe i click
                val intent = Intent(view.context, ShopDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
                view.context.startActivity(intent)
            }
        }
        spannable.setSpan(clickableSpan,
                positionStart, positionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannable
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

fun View.showSoftKeyboard() {
    if (requestFocus()) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.observable(): Observable<String> {

    val subject = PublishSubject.create<String>()

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            subject.onNext(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })

    return subject
}