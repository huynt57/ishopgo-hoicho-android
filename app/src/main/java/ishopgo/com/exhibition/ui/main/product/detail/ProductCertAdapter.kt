package ishopgo.com.exhibition.ui.main.product.detail

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_product_cert.view.*

class ProductCertAdapter : ClickableAdapter<ProductDetail.ListCert>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_cert
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductDetail.ListCert> {
        return ProcessHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductDetail.ListCert>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class ProcessHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ProductDetail.ListCert>(v) {

        override fun populate(data: ProductDetail.ListCert) {
            super.populate(data)

            itemView.apply {

                Glide.with(context)
                        .load(data.image)
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(iv_image)
            }
        }
    }
}