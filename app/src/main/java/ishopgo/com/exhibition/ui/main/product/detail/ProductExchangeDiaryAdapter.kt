package ishopgo.com.exhibition.ui.main.product.detail

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExchangeDiaryImage
import ishopgo.com.exhibition.domain.response.ExchangeDiaryProduct
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.diary.DiaryImages
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_product_exchange_diary.view.*

class ProductExchangeDiaryAdapter : ClickableAdapter<ExchangeDiaryProduct>() {

    companion object {
        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
        const val DIARY_DELETE_CLICK = 2
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
                tv_account_name.text = converted.provideAccountName()
//                tv_diary_title.text = converted.provideTitle()
//                tv_diary_content.text = converted.provideContent()
//                tv_comment_time.text = converted.provideDateTime()

//                Glide.with(context)
//                        .load(converted.provideAvatar())
//                        .apply(RequestOptions.circleCropTransform()
//                                .placeholder(R.drawable.avatar_placeholder)
//                                .error(R.drawable.avatar_placeholder))
//                        .into(img_account_avatar)
//
//                if (UserDataManager.currentType == "Nhân viên gian hàng" && UserDataManager.currentBoothId == data.boothId) {
//                    val listPermission = Const.listPermission
//                    if (listPermission.isNotEmpty())
//                        for (i in listPermission.indices)
//                            if (Const.Permission.EXPO_BOOTH_PRODUCTION_DIARY_ADD == listPermission[i]) {
//                                tv_diary_delete.visibility = View.VISIBLE
//                                break
//                            }
//                } else if (UserDataManager.currentUserId == data.accountId)
//                    tv_diary_delete.visibility = View.VISIBLE
//                else tv_diary_delete.visibility = View.GONE
//
//                if (converted.provideImages().isNotEmpty()) {
//                    if (converted.provideImages().size > 1) {
//                        img_diary.visibility = View.GONE
//                        rv_diary_image.visibility = View.VISIBLE
//
//                        val adapter = ProductDiaryImageAdapter()
//                        adapter.replaceAll(converted.provideImages())
//                        rv_diary_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//                        rv_diary_image.isNestedScrollingEnabled = false
//                        rv_diary_image.setHasFixedSize(false)
//                        rv_diary_image.adapter = adapter
//                        adapter.listener = object : ClickableAdapter.BaseAdapterAction<DiaryImages> {
//                            override fun click(position: Int, data: DiaryImages, code: Int) {
//                                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_IMAGE_CLICK)
//                            }
//                        }
//                    } else {
//                        img_diary.visibility = View.VISIBLE
//                        rv_diary_image.visibility = View.GONE
//
//                        Glide.with(this).load(converted.provideImages()[0].image)
//                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_diary)
//
//                        img_diary.setOnClickListener {
//                            listener?.click(adapterPosition, getItem(adapterPosition), DIARY_IMAGE_CLICK)
//                        }
//                    }
//                } else {
//                    img_diary.visibility = View.GONE
//                    rv_diary_image.visibility = View.GONE
//                }
            }
        }
    }

    interface DiaryProvider {
        fun provideAccountName(): CharSequence
        fun provideTitle(): CharSequence
        fun provideContent(): String
        fun provideHSD(): CharSequence
        fun provideBenGui(): CharSequence
        fun provideBenNhan(): CharSequence
        fun provideDiaChi(): CharSequence
        fun provideSoLuong(): CharSequence
        fun provideImages(): List<ExchangeDiaryImage>

    }

    class ConverterDiaryProduct : Converter<ExchangeDiaryProduct, DiaryProvider> {
        override fun convert(from: ExchangeDiaryProduct): DiaryProvider {
            return object : DiaryProvider {
                override fun provideAccountName(): CharSequence {
                    return from.accountName ?: ""
                }

                override fun provideTitle(): CharSequence {
                    return "Tên giao dịch: ${from.accountName ?: ""}"
                }

                override fun provideContent(): String {
                    return "Nội dung: ${from.content ?: ""}"
                }

                override fun provideHSD(): CharSequence {
                    return "Hạn sử dụng: ${from.expiryDate?.asDateTime() ?: ""}"
                }

                override fun provideBenGui(): CharSequence {
                    return "Bên gửi: ${from.senderBoothName ?: ""}"
                }

                override fun provideBenNhan(): CharSequence {
                    return "Bên nhân: ${from.receiverBoothName ?: ""}"
                }

                override fun provideDiaChi(): CharSequence {
                    return "Địa chỉ: ${from.address ?: ""}"
                }

                override fun provideSoLuong(): CharSequence {
                    return "Số lượng: ${from.quantity ?: ""}"
                }

                override fun provideImages(): List<ExchangeDiaryImage> {
                    return from.images ?: mutableListOf()
                }
            }
        }
    }

}