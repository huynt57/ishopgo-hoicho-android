package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_product_retated_horizontal.view.*

class ProductManagerDetailRelatedAdapter : ClickableAdapter<Product>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_retated_horizontal
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Product> {
        return Holder(v)
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<Product>(v) {

        override fun populate(data: Product) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_spk_product_related_image)

                tv_spk_product_related_name.text = data.provideName()
                tv_spk_product_related_price.text = data.providePrice()
                btn_spk_product_related_delete.visibility = View.GONE

            }
        }

    }
}