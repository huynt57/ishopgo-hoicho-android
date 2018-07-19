package ishopgo.com.exhibition.ui.main.boothfollow

import android.content.Intent
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.BoothFollow
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_booth_follow.view.*

class BoothFollowAdapter : ClickableAdapter<BoothFollow>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_booth_follow
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothFollow> {
        return Holder(v, BoothFollowConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothFollow>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_save_booth.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), SAVE_QRCODE_TO_STORAGE) }
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEM_TO_BOOTH) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<BoothFollow, BoothFollowProvider>) : BaseRecyclerViewAdapter.ViewHolder<BoothFollow>(v) {

        override fun populate(data: BoothFollow) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideQrCode())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        ).into(img_code)
                tv_booth_name.text = convert.provideName()
                tv_booth_address.text = convert.provideAddress()
                tv_booth_phone.text = convert.providePhone()
                tv_booth_phone.setOnClickListener {
                    val uri = Uri.parse("tel:${convert.providePhone()}")
                    val i = Intent(Intent.ACTION_DIAL, uri)
                    it.context.startActivity(i)
                }
                tv_booth_number_product.text = convert.provideNumberProduct()
                tv_booth_member_cnt.text = convert.provideMemberCNT()
            }
        }
    }

    interface BoothFollowProvider {
        fun provideName(): String
        fun providePhone(): String
        fun provideAddress(): String
        fun provideNumberProduct(): String
        fun provideMemberCNT(): String
        fun provideQrCode(): String
    }

    class BoothFollowConverter : Converter<BoothFollow, BoothFollowProvider> {

        override fun convert(from: BoothFollow): BoothFollowProvider {
            return object : BoothFollowProvider {
                override fun provideQrCode(): String {
                    return from.qrcode ?: ""
                }

                override fun provideMemberCNT(): String {
                    return "Số thành viên quan tâm: ${from.memberCnt}"
                }

                override fun provideAddress(): String {
                    return "${from.address?.trim() ?: ""}, ${from.district?.trim()
                            ?: ""}, ${from.city?.trim()
                            ?: ""}"
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun providePhone(): String {
                    return from.phone ?: ""
                }

                override fun provideNumberProduct(): String {
                    return "Số sản phẩm: ${from.numberProduct}"
                }
            }
        }
    }

    companion object {
        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_BOOTH = 1
    }
}