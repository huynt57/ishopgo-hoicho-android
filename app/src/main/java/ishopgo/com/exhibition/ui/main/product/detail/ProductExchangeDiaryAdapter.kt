package ishopgo.com.exhibition.ui.main.product.detail

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExchangeDiaryImage
import ishopgo.com.exhibition.domain.response.ExchangeDiaryProduct
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.product.ProductExchangeDiaryImageAdapter
import kotlinx.android.synthetic.main.item_product_exchange_diary.view.*

class ProductExchangeDiaryAdapter : ClickableAdapter<ExchangeDiaryProduct>() {

    companion object {
        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_exchange_diary
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ExchangeDiaryProduct> {
        return ProcessHolder(v, ConverterDiaryProduct())
    }

    override fun onBindViewHolder(holder: ViewHolder<ExchangeDiaryProduct>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {

            itemView.tv_account_name.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_USER_CLICK)
            }
        }
    }

    inner class ProcessHolder(v: View, private val converter: Converter<ExchangeDiaryProduct, DiaryProvider>) : BaseRecyclerViewAdapter.ViewHolder<ExchangeDiaryProduct>(v) {

        override fun populate(data: ExchangeDiaryProduct) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                if (data.accountName?.isNotEmpty() == true)
                    tv_account_name.text = converted.provideAccountName()
                else tv_account_name.visibility = View.GONE
                if (data.name?.isNotEmpty() == true)
                    tv_tenGiaoDich.text = converted.provideTitle()
                else tv_tenGiaoDich.visibility = View.GONE
                if (data.content?.isNotEmpty() == true)
                    tv_noiDung.text = converted.provideContent()
                else tv_noiDung.visibility = View.GONE
                if (data.stampCode?.isNotEmpty() == true)
                    tv_maSoLo.text = converted.provideMaSoLo()
                else tv_maSoLo.visibility = View.GONE
                if (data.senderBoothName?.isNotEmpty() == true)
                    tv_benGiao.text = converted.provideBenGui()
                else tv_benGiao.visibility = View.GONE
                if (data.receiverBoothName?.isNotEmpty() == true)
                    tv_benNhan.text = converted.provideBenNhan()
                else tv_benNhan.visibility = View.GONE
                if (data.address?.isNotEmpty() == true)
                    tv_diaChi.text = converted.provideDiaChi()
                else tv_diaChi.visibility = View.GONE
                if (data.expiryDate?.isNotEmpty() == true)
                    tv_HSD.text = converted.provideHSD()
                else tv_HSD.visibility = View.GONE
                if (data.quantity?.isNotEmpty() == true)
                    tv_soLuong.text = converted.provideSoLuong()
                else tv_soLuong.visibility = View.GONE

                Glide.with(context)
                        .load(converted.provideAccountAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(img_account_avatar)

                if (converted.provideImages().isNotEmpty()) {
                    if (converted.provideImages().size > 1) {
                        img_exchange_diary.visibility = View.GONE
                        rv_exchange_diary_image.visibility = View.VISIBLE

                        val adapter = ProductExchangeDiaryImageAdapter()
                        adapter.replaceAll(converted.provideImages())
                        rv_exchange_diary_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_exchange_diary_image.isNestedScrollingEnabled = false
                        rv_exchange_diary_image.setHasFixedSize(false)
                        rv_exchange_diary_image.adapter = adapter
                        adapter.listener = object : ClickableAdapter.BaseAdapterAction<ExchangeDiaryImage> {
                            override fun click(position: Int, data: ExchangeDiaryImage, code: Int) {
                                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_IMAGE_CLICK)
                            }
                        }
                    } else {
                        img_exchange_diary.visibility = View.VISIBLE
                        rv_exchange_diary_image.visibility = View.GONE

                        Glide.with(this).load(converted.provideImages()[0].image)
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_exchange_diary)

                        img_exchange_diary.setOnClickListener {
                            listener?.click(adapterPosition, getItem(adapterPosition), DIARY_IMAGE_CLICK)
                        }
                    }
                } else {
                    img_exchange_diary.visibility = View.GONE
                    rv_exchange_diary_image.visibility = View.GONE
                }
            }
        }
    }

    interface DiaryProvider {
        fun provideAccountName(): CharSequence
        fun provideAccountAvatar(): CharSequence
        fun provideTitle(): CharSequence
        fun provideContent(): CharSequence
        fun provideHSD(): CharSequence
        fun provideBenGui(): CharSequence
        fun provideBenNhan(): CharSequence
        fun provideDiaChi(): CharSequence
        fun provideSoLuong(): CharSequence
        fun provideMaSoLo(): CharSequence
        fun provideImages(): List<ExchangeDiaryImage>

    }

    class ConverterDiaryProduct : Converter<ExchangeDiaryProduct, DiaryProvider> {
        override fun convert(from: ExchangeDiaryProduct): DiaryProvider {
            return object : DiaryProvider {
                override fun provideAccountAvatar(): CharSequence {
                    return ""
                }

                override fun provideMaSoLo(): CharSequence {
                    return "<b>Mã số lô:</b> ${from.stampCode ?: ""}".asHtml()
                }

                override fun provideAccountName(): CharSequence {
                    return "<b>${from.accountName
                            ?: ""}</b> - <font color=\"#757575\">${from.createdAt?.asDateTime()
                            ?: ""}</font>".asHtml()
                }

                override fun provideTitle(): CharSequence {
                    return "<b>Tên giao dịch:</b> ${from.accountName ?: ""}".asHtml()
                }

                override fun provideContent(): CharSequence {
                    return "<b>Nội dung:</b> ${from.content ?: ""}".asHtml()
                }

                override fun provideHSD(): CharSequence {
                    return "<b>Hạn sử dụng:</b> ${from.expiryDate?.asDate() ?: ""}".asHtml()
                }

                override fun provideBenGui(): CharSequence {
                    return "<b>Bên gửi:</b> ${from.senderBoothName ?: ""}".asHtml()
                }

                override fun provideBenNhan(): CharSequence {
                    return "<b>Bên nhận:</b> ${from.receiverBoothName ?: ""}".asHtml()
                }

                override fun provideDiaChi(): CharSequence {
                    return "<b>Địa chỉ:</b> ${from.address ?: ""}".asHtml()
                }

                override fun provideSoLuong(): CharSequence {
                    return "<b>Số lượng:</b> ${from.quantity ?: ""}".asHtml()
                }

                override fun provideImages(): List<ExchangeDiaryImage> {
                    return from.images ?: mutableListOf()
                }
            }
        }
    }

}