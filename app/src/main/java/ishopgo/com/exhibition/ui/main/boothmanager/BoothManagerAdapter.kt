package ishopgo.com.exhibition.ui.main.boothmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_booth_manager.view.*

class BoothManagerAdapter : ClickableAdapter<BoothManagerProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_booth_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothManagerProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_save_booth.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), SAVE_QRCODE_TO_STORAGE) }
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEM_TO_BOOTH) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<BoothManagerProvider>(v) {

        override fun populate(data: BoothManagerProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideQrCode())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        ).into(img_code)
                tv_booth_manager_name.text = data.provideName()
                tv_booth_manager_phone.text = data.providePhone()
                tv_booth_manager_region.text = data.provideRegion()
                tv_booth_manager_number_product.text = data.provideNumberProduct()
                tv_booth_manager_member_cnt.text = data.provideMemberCNT()
            }
        }
    }

    companion object {
        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_BOOTH = 1
    }
}