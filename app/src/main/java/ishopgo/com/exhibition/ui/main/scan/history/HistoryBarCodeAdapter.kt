package ishopgo.com.exhibition.ui.main.scan.history

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.HistoryScan
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_history_barcode.view.*

class HistoryBarCodeAdapter : ClickableAdapter<HistoryScan>() {
    companion object {
        const val CLICK_PRODUCT = 0
        const val CLICK_BARCODE = 1
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_history_barcode
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<HistoryScan> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<HistoryScan>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.constraintLayoutProduct.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_PRODUCT) }
            itemView.constraintLayoutBarCode.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_BARCODE) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<HistoryScan>(v) {

        override fun populate(data: HistoryScan) {
            super.populate(data)

            itemView.apply {
                if (data.icheckProduct != null) {
                    constraintLayoutProduct.visibility = View.VISIBLE
                    constraintLayoutBarCode.visibility = View.GONE
                    Glide.with(itemView.context).load(data.productImage)
                            .apply(RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_product)

                    tv_product.text = data.productName
                    tv_product_code.text = data.code
                    tv_product_price.text = data.productPrice.asHtml()
                    tv_product_time.text = data.time
                } else {
                    constraintLayoutProduct.visibility = View.GONE
                    constraintLayoutBarCode.visibility = View.VISIBLE
                    view_barCode.text = data.code
                    view_time.text = data.time
                }
            }
        }
    }
}