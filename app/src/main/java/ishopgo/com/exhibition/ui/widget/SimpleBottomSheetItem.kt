package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.widget_bottom_sheet_item.view.*

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class SimpleBottomSheetItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private var iconRes: Int = -1
    private var text: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_bottom_sheet_item, this)

        init(attrs, defStyleAttr)

        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SimpleBottomSheetItem, defStyleAttr, 0)

        text = a.getString(R.styleable.SimpleBottomSheetItem_text)
        view_text.text = text

        if (a.hasValue(R.styleable.SimpleBottomSheetItem_icon)) {
            val iconRes = a.getResourceId(R.styleable.SimpleBottomSheetItem_icon, iconRes)
            if (iconRes != -1) {
                val drawable = AppCompatResources.getDrawable(context, iconRes)
                if (drawable != null)
                    view_icon.setImageDrawable(drawable)

            }
        }

        a.recycle()
    }

    fun setText(text: CharSequence) {
        view_text.text = text
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        val drawable = AppCompatResources.getDrawable(context, iconRes)
        if (drawable != null)
            view_icon.setImageDrawable(drawable)
    }

}