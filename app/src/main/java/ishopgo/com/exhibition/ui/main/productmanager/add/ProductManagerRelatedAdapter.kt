package ishopgo.com.exhibition.ui.main.productmanager.add

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider
import kotlinx.android.synthetic.main.item_product_manager_related.view.*


class ProductManagerRelatedAdapter : ClickableAdapter<ProductManagerProvider>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_manager_related
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<ProductManagerProvider> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<ProductManagerProvider>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: ProductManagerProvider) {
            super.populate(data)
            itemView.apply {
                Glide.with(context)
                        .load(data.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_thumb)

                tv_item_name.text = data.provideName()
                tv_price.text = data.providePrice()
                tv_item_code.text = data.provideCode()
                tv_department.text = data.provideDepartment()
                tv_status.text = data.provideStatus()
            }
        }
    }
}