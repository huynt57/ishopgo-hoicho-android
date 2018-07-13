package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import ishopgo.com.exhibition.R

/**
 * Created by xuanhong on 7/13/18. HappyCoding!
 */
class ProductInfo @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_product_single_info, this)
    }


}