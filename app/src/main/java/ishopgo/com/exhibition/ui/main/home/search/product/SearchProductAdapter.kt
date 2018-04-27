package ishopgo.com.exhibition.ui.main.home.search.product

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_search_product.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class SearchProductAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SearchProductProvider>() {

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_search_product
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchProductProvider> {
        val productHolder = ProductHolder(v)
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchProductProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    internal inner class ProductHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<SearchProductProvider>(view) {

        override fun populate(data: SearchProductProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(itemView.context).load(data.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder))
                        .into(iv_thumb)
                view_name.text = data.provideName()
                view_code.text = data.provideCode()


            }

        }

    }

}