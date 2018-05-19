package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet

/**
 * Created by xuanhong on 11/16/17. HappyCoding!
 */

class MoneyEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : android.support.v7.widget.AppCompatEditText(context, attrs, defStyleAttr) {

    private val MAX_LENGTH = 19

    init {
        init()
    }

    val money: Long?
        get() {
            var cleanString = super.getText().toString()
            cleanString = cleanString.replace("[,.]".toRegex(), "")
            var parse: Long
            try {
                parse = java.lang.Long.parseLong(cleanString)
            } catch (e: NumberFormatException) {
                parse = 0L
            }

            return parse
        }

    private fun init() {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        addTextChangedListener(MoneyNumberFormattingTextWatcher(this, MAX_LENGTH))

        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(MAX_LENGTH)
        filters = fArray
    }
}
