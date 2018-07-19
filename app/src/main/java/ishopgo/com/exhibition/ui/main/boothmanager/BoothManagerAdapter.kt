package ishopgo.com.exhibition.ui.main.boothmanager

import android.content.Intent
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_booth_manager.view.*

class BoothManagerAdapter : ClickableAdapter<BoothManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_booth_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothManager> {
        return Holder(v, BoothManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_save_booth.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), SAVE_QRCODE_TO_STORAGE) }
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEM_TO_BOOTH) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<BoothManager, BoothManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<BoothManager>(v) {

        override fun populate(data: BoothManager) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideQrCode())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        ).into(img_code)
                tv_booth_manager_name.text = convert.provideName()
                tv_booth_manager_phone.text = convert.providePhone()
                tv_booth_manager_phone.setOnClickListener {
                    val uri = Uri.parse("tel:${convert.providePhone()}")
                    val i = Intent(Intent.ACTION_DIAL, uri)
                    it.context.startActivity(i)
                }
                tv_booth_manager_region.text = convert.provideRegion()
                tv_booth_manager_number_product.text = convert.provideNumberProduct()
                tv_booth_manager_member_cnt.text = convert.provideMemberCNT()
            }
        }
    }

    interface BoothManagerProvider {
        fun provideName(): String
        fun providePhone(): String
        fun provideRegion(): String
        fun provideNumberProduct(): String
        fun provideMemberCNT(): String
        fun provideQrCode(): String
    }

    class BoothManagerConverter : Converter<BoothManager, BoothManagerProvider> {

        override fun convert(from: BoothManager): BoothManagerProvider {
            return object : BoothManagerProvider {
                override fun provideQrCode(): String {
                    return from.qrcode ?: ""
                }

                override fun provideName(): String {
                    return from.boothName ?: ""
                }

                override fun providePhone(): String {
                    return from.phone ?: ""
                }

                override fun provideRegion(): String {
                    return "Khu vực: ${from.region}"
                }

                override fun provideNumberProduct(): String {
                    return "Số sản phẩm: ${from.numberProduct}"
                }

                override fun provideMemberCNT(): String {
                    return "Số thành viên quan tâm: ${from.numberProduct}"
                }
            }
        }
    }

    companion object {
        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_BOOTH = 1
    }
}