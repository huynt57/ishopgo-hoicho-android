package ishopgo.com.exhibition.ui.main.productmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_product_manager.view.*


/**
 * Created by xuanhong on 2/2/18. HappyCoding!
 */
class ProductManagerAdapter : ClickableAdapter<ProductManagerProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductManagerProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ProductManagerProvider>(v) {

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