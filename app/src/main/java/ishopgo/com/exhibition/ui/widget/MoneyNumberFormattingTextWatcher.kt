package ishopgo.com.exhibition.ui.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

/**
 * Created by xuanhong on 11/15/17. HappyCoding!
 */

class MoneyNumberFormattingTextWatcher(private val editText: EditText, private val maxLength: Int) : TextWatcher {
    private val current: String? = null

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    @Synchronized
    override fun afterTextChanged(s: Editable) {
        if (s.toString() != current) {
            editText.removeTextChangedListener(this)

            val cleanString = s.toString().replace("[$,.]".toRegex(), "")

            val locale = Locale("vi", "VN")
            val numberFormat = NumberFormat.getIntegerInstance(locale)
            var number: Long = 0
            try {
                number = java.lang.Long.parseLong(cleanString)
            } catch (ignored: NumberFormatException) {
            }

            val formatted = numberFormat.format(number)
            if (formatted.length <= maxLength) {
                editText.setText(formatted.toCharArray(), 0, formatted.length)
                editText.setSelection(formatted.length)
            }

            editText.addTextChangedListener(this)
        }
    }
}
