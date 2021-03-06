package ishopgo.com.exhibition.ui.main.product.detail

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.diary.DiaryImages
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_product_diary.view.*

class ProductDiaryAdapter : ClickableAdapter<DiaryProduct>() {

    companion object {
        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
        const val DIARY_DELETE_CLICK = 2
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_diary
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<DiaryProduct> {
        return ProcessHolder(v, ConverterDiaryProduct())
    }

    override fun onBindViewHolder(holder: ViewHolder<DiaryProduct>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {

            itemView.tv_account_name.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_USER_CLICK)
            }

            itemView.img_account_avatar.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_USER_CLICK)
            }

            itemView.tv_diary_delete.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_DELETE_CLICK)
            }
        }
    }

    inner class ProcessHolder(v: View, private val converter: Converter<DiaryProduct, DiaryProvider>) : BaseRecyclerViewAdapter.ViewHolder<DiaryProduct>(v) {

        override fun populate(data: DiaryProduct) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                tv_account_name.text = converted.provideAccountName()
                tv_diary_title.text = converted.provideTitle()
                tv_diary_content.text = converted.provideContent()
                tv_diary_maLo.text = converted.provideMaLo()
                tv_diary_address.text = converted.provideAddress()

                Glide.with(context)
                        .load(converted.provideAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(img_account_avatar)

                if (UserDataManager.currentType == "Nhân viên gian hàng" && UserDataManager.currentBoothId == data.boothId) {
                    val listPermission = Const.listPermission
                    if (listPermission.isNotEmpty())
                        for (i in listPermission.indices)
                            if (Const.Permission.EXPO_BOOTH_PRODUCTION_DIARY_ADD == listPermission[i]) {
                                tv_diary_delete.visibility = View.VISIBLE
                                break
                            }
                } else if (UserDataManager.currentUserId == data.accountId)
                    tv_diary_delete.visibility = View.VISIBLE
                else tv_diary_delete.visibility = View.GONE

                if (converted.provideImages().isNotEmpty()) {
                    if (converted.provideImages().size > 1) {
                        img_diary.visibility = View.GONE
                        rv_diary_image.visibility = View.VISIBLE

                        val adapter = ProductDiaryImageAdapter()
                        adapter.replaceAll(converted.provideImages())
                        rv_diary_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_diary_image.isNestedScrollingEnabled = false
                        rv_diary_image.setHasFixedSize(false)
                        rv_diary_image.adapter = adapter
                        adapter.listener = object : ClickableAdapter.BaseAdapterAction<DiaryImages> {
                            override fun click(position: Int, data: DiaryImages, code: Int) {
                                listener?.click(adapterPosition, getItem(adapterPosition), DIARY_IMAGE_CLICK)
                            }
                        }
                    } else {
                        img_diary.visibility = View.VISIBLE
                        rv_diary_image.visibility = View.GONE

                        Glide.with(this).load(converted.provideImages()[0].image)
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_diary)

                        img_diary.setOnClickListener {
                            listener?.click(adapterPosition, getItem(adapterPosition), DIARY_IMAGE_CLICK)
                        }
                    }
                } else {
                    img_diary.visibility = View.GONE
                    rv_diary_image.visibility = View.GONE
                }
            }
        }
    }

    interface DiaryProvider {
        fun provideAvatar(): CharSequence
        fun provideAccountName(): CharSequence
        fun provideTitle(): CharSequence
        fun provideContent(): CharSequence
        fun provideImages(): List<DiaryImages>
        fun provideDateTime(): CharSequence
        fun provideAddress(): CharSequence
        fun provideMaLo(): CharSequence
    }

    class ConverterDiaryProduct : Converter<DiaryProduct, DiaryProvider> {
        override fun convert(from: DiaryProduct): DiaryProvider {
            return object : DiaryProvider {
                override fun provideMaLo(): CharSequence {
                    return "<b>Mã lô: <font color=\"red\">${from.maLo ?: ""}</font></b>".asHtml()
                }

                override fun provideAvatar(): CharSequence {
                    return from.account?.avatar ?: ""
                }

                override fun provideAccountName(): CharSequence {
                    return "<b>${from.account?.name
                            ?: "unknown"}</b> - ${from.createdAt?.asDateTime() ?: ""}".asHtml()
                }

                override fun provideTitle(): CharSequence {
                    return from.title ?: ""
                }

                override fun provideContent(): CharSequence {
                    return from.content ?: ""
                }

                override fun provideImages(): List<DiaryImages> {
                    return from.images ?: mutableListOf()
                }

                override fun provideDateTime(): CharSequence {
                    return from.createdAt?.asDateTime() ?: ""
                }

                override fun provideAddress(): CharSequence {
                    return from.address ?: ""
                }

            }
        }
    }

}