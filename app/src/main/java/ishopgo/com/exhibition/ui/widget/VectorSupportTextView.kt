package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import ishopgo.com.exhibition.R


/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
open class VectorSupportTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.VectorSupportTextView)

            var dStart: Drawable? = null
            val drawableStartSquareSize: Int
            var dEnd: Drawable? = null
            val drawableEndSquareSize: Int
            var dBottom: Drawable? = null
            val drawableBottomSquareSize: Int
            var dTop: Drawable? = null
            val drawableTopSquareSize: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dStart = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableStartCompat)
                dEnd = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableEndCompat)
                dBottom = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableBottomCompat)
                dTop = attributeArray.getDrawable(R.styleable.VectorSupportTextView_drawableTopCompat)
            } else {
                val drawableStartId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableStartCompat, 0)
                val drawableEndId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableEndCompat, 0)
                val drawableBottomId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableBottomCompat, 0)
                val drawableTopId = attributeArray.getResourceId(R.styleable.VectorSupportTextView_drawableTopCompat, 0)

                if (drawableStartId != 0) {
                    dStart = AppCompatResources.getDrawable(context, drawableStartId)
                }
                if (drawableEndId != 0) {
                    dEnd = AppCompatResources.getDrawable(context, drawableEndId)
                }
                if (drawableBottomId != 0) {
                    dBottom = AppCompatResources.getDrawable(context, drawableBottomId)
                }
                if (drawableTopId != 0) {
                    dTop = AppCompatResources.getDrawable(context, drawableTopId)
                }
            }

            drawableStartSquareSize = attributeArray.getDimensionPixelSize(R.styleable.VectorSupportTextView_drawableStartSquareSize, -1)
            drawableEndSquareSize = attributeArray.getDimensionPixelSize(R.styleable.VectorSupportTextView_drawableEndSquareSize, -1)
            drawableBottomSquareSize = attributeArray.getDimensionPixelSize(R.styleable.VectorSupportTextView_drawableBottomSquareSize, -1)
            drawableTopSquareSize = attributeArray.getDimensionPixelSize(R.styleable.VectorSupportTextView_drawableTopSquareSize, -1)

            if (drawableStartSquareSize != -1) {
                dStart?.setBounds(0, 0, drawableStartSquareSize, drawableStartSquareSize)
            }
            if (drawableEndSquareSize != -1) {
                dEnd?.setBounds(0, 0, drawableEndSquareSize, drawableEndSquareSize)
            }
            if (drawableBottomSquareSize != -1) {
                dBottom?.setBounds(0, 0, drawableBottomSquareSize, drawableBottomSquareSize)
            }
            if (drawableTopSquareSize != -1) {
                dTop?.setBounds(0, 0, drawableTopSquareSize, drawableTopSquareSize)
            }

            val hasCustomSize = drawableStartSquareSize != -1
                    || drawableEndSquareSize != -1
                    || drawableBottomSquareSize != -1
                    || drawableTopSquareSize != -1

            if (hasCustomSize)
                setCompoundDrawablesRelative(dStart, dTop, dEnd, dBottom)
            else
                setCompoundDrawablesRelativeWithIntrinsicBounds(dStart, dTop, dEnd, dBottom)

            val color = attributeArray.getColor(R.styleable.VectorSupportTextView_drawableTintCompat, 0)
            if (color != 0)
                tintDrawable(color)


            attributeArray.recycle()
        }
    }

    fun drawableCompat(@DrawableRes startResId: Int = 0, @DrawableRes topResId: Int = 0, @DrawableRes endResId: Int = 0, @DrawableRes bottomResId: Int = 0) {
        var dStart: Drawable? = null
        var dEnd: Drawable? = null
        var dBottom: Drawable? = null
        var dTop: Drawable? = null

        if (startResId != 0)
            dStart = AppCompatResources.getDrawable(context, startResId)
        if (endResId != 0)
            dEnd = AppCompatResources.getDrawable(context, endResId)
        if (bottomResId != 0)
            dBottom = AppCompatResources.getDrawable(context, bottomResId)
        if (topResId != 0)
            dTop = AppCompatResources.getDrawable(context, topResId)

        setCompoundDrawablesRelativeWithIntrinsicBounds(dStart, dTop, dEnd, dBottom)
    }

    fun tintDrawable(@ColorInt color: Int) {
        compoundDrawablesRelative.map {
            it?.let {
                DrawableCompat.setTint(it, color)
            }
        }

        compoundDrawables.map {
            it?.let {
                DrawableCompat.setTint(it, color)
            }
        }
    }
}