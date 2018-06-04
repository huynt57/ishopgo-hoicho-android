package ishopgo.com.exhibition.ui.main.product

import android.graphics.Paint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_product_grid.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<ProductProvider>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_grid
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductProvider> {
        val productHolder = ProductHolder(v)
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    internal inner class ProductHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<ProductProvider>(view) {

        override fun populate(data: ProductProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(itemView.context).load(data.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder))
                        .into(iv_thumb)
                tv_item_name.text = data.provideName()

                tv_price.text = data.providePrice()
                val hideMarketPrice = data.provideMarketPrice().equals(data.providePrice(), true)
                tv_tt_price.visibility = if (data.provideMarketPrice() == "0 đ" || hideMarketPrice) View.INVISIBLE else View.VISIBLE
                tv_tt_price.paintFlags = tv_tt_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tv_tt_price.text = data.provideMarketPrice()
            }

        }

    }

}