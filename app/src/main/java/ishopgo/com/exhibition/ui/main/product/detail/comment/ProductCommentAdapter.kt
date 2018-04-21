package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductCommentAdapter : ClickableAdapter<ProductCommentProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_comment
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductCommentProvider> {
        return ProductHolder(v)
    }

    inner class ProductHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ProductCommentProvider>(v) {

        override fun populate(data: ProductCommentProvider) {
            super.populate(data)

        }
    }


}