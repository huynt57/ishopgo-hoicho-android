package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.layout_header_list.view.*


/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 *
 * ref: https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-2-grids-688829b1d29b
 */
class HeaderRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_header_list, this)
        orientation = VERTICAL
    }

    fun setLabel(label: CharSequence) {
        view_label.text = label
    }

    fun getLabel() = view_label

    fun getList() = view_list

}