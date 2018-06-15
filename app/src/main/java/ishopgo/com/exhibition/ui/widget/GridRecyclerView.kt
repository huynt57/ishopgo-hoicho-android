package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController


/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
open class GridRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    override fun attachLayoutAnimationParameters(child: View?, params: ViewGroup.LayoutParams?, index: Int, count: Int) {
        val layoutManager = layoutManager
        if (adapter != null && layoutManager is GridLayoutManager) {
            params?.let {
                var parameters = it.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters?
                if (parameters == null) {
                    // If there are no animation parameters, create new once and attach them to
                    // the LayoutParams.
                    parameters = GridLayoutAnimationController.AnimationParameters()
                    it.layoutAnimationParameters = parameters
                }

                // Next we are updating the parameters

                // Set the number of items in the RecyclerView and the index of this item
                parameters.count = count
                parameters.index = index

                // Calculate the number of columns and rows in the grid
                val columns = layoutManager.spanCount
                parameters.columnsCount = columns
                parameters.rowsCount = count / columns

                // Calculate the column/row position in the grid
                val invertedIndex = count - 1 - index
                parameters.column = columns - 1 - invertedIndex % columns
                parameters.row = parameters.rowsCount - 1 - invertedIndex / columns

            }
        } else
            super.attachLayoutAnimationParameters(child, params, index, count)
    }
}