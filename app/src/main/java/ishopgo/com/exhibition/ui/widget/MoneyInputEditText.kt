package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.appcompat.R
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet

/**
 * Created by xuanhong on 11/16/17. HappyCoding!
 */

class MoneyInputEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private val MAX_LENGTH = 19

    val money: Long
        get() {
            var cleanString = super.getText().toString()
            cleanString = cleanString.replace("[$,.]".toRegex(), "")

            return try {
                java.lang.Long.parseLong(cleanString)
            } catch (e: NumberFormatException) {
                0L
            }
        }

    private fun init() {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        addTextChangedListener(MoneyNumberFormattingTextWatcher(this, MAX_LENGTH))

        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(MAX_LENGTH)
        filters = fArray
    }
}
