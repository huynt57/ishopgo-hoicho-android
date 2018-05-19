package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.widget_menu_with_text_and_icon.view.*

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class SimpleMenuItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var iconRes: Int = -1
    private var text: String? = null
    private var secondaryText: String? = null
    private var showBottomDivider: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_menu_with_text_and_icon, this)

        init(attrs, defStyleAttr)

        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SimpleMenuItem, defStyleAttr, 0)

        text = a.getString(R.styleable.SimpleMenuItem_menuText)
        secondaryText = a.getString(R.styleable.SimpleMenuItem_menuSecondaryText)
        showBottomDivider = a.getBoolean(R.styleable.SimpleMenuItem_menuShowBottomDivider, false)

        view_text.text = text
        view_secondary_text.text = secondaryText

        if (secondaryText.isNullOrBlank()) view_secondary_text.visibility = View.GONE
        else view_secondary_text.visibility = View.VISIBLE

        view_divider.visibility = if (showBottomDivider) View.VISIBLE else View.GONE

        if (a.hasValue(R.styleable.SimpleMenuItem_menuIcon)) {
            val iconRes = a.getResourceId(R.styleable.SimpleMenuItem_menuIcon, iconRes)
            if (iconRes != -1) {
                val drawable = AppCompatResources.getDrawable(context, iconRes)
                if (drawable != null)
                    view_icon.setImageDrawable(drawable)

            }
        }

        a.recycle()
    }

}