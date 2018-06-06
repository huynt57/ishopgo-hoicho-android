package ishopgo.com.exhibition.ui.main.boothfollow

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_booth_follow.view.*

class BoothFollowAdapter : ClickableAdapter<BoothFollowProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_booth_follow
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothFollowProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothFollowProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<BoothFollowProvider>(v) {

        override fun populate(data: BoothFollowProvider) {
            super.populate(data)

            itemView.apply {
                tv_booth_name.text = data.provideName()
                tv_booth_address.text = data.provideAddress()
                tv_booth_phone.text = data.providePhone()
                tv_booth_number_product.text = data.provideNumberProduct()
            }
        }

    }
}