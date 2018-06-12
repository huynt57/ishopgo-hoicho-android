package ishopgo.com.exhibition.ui.main.boothfollow

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
            itemView.btn_save_booth.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), SAVE_QRCODE_TO_STORAGE) }
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEM_TO_BOOTH) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<BoothFollowProvider>(v) {

        override fun populate(data: BoothFollowProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideQrCode())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        ).into(img_code)
                tv_booth_name.text = data.provideName()
                tv_booth_address.text = data.provideAddress()
                tv_booth_phone.text = data.providePhone()
                tv_booth_number_product.text = data.provideNumberProduct()
                tv_booth_member_cnt.text = data.provideMemberCNT()
            }
        }
    }

    companion object {
        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_BOOTH = 1
    }
}