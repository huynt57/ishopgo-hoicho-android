package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import ishopgo.com.exhibition.R


/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class VectorSupportTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.VectorSupportTextView)

            var dStart: Drawable? = null
            var dEnd: Drawable? = null
            var dBottom: Drawable? = null
            var dTop: Drawable? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dStart = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableStartCompat)
                dEnd = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableEndCompat)
                dBottom = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableBottomCompat)
                dTop = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableTopCompat)
            } else {
                val drawableStartId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableStartCompat, -1)
                val drawableEndId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableEndCompat, -1)
                val drawableBottomId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableBottomCompat, -1)
                val drawableTopId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableTopCompat, -1)

                if (drawableStartId != -1)
                    dStart = AppCompatResources.getDrawable(context, drawableStartId)
                if (drawableEndId != -1)
                    dEnd = AppCompatResources.getDrawable(context, drawableEndId)
                if (drawableBottomId != -1)
                    dBottom = AppCompatResources.getDrawable(context, drawableBottomId)
                if (drawableTopId != -1)
                    dTop = AppCompatResources.getDrawable(context, drawableTopId)
            }

            // to support rtl
            setCompoundDrawablesRelativeWithIntrinsicBounds(dStart, dTop, dEnd, dBottom)
            attributeArray.recycle()
        }
    }
}