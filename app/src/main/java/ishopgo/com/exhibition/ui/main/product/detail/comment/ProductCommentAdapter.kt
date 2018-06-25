package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import kotlinx.android.synthetic.main.item_product_comment.view.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductCommentAdapter : ClickableAdapter<ProductComment>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_comment
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductComment> {
        return ProductHolder(v, ProductCommentConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductComment>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.view_avatar.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            itemView.view_name.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class ProductHolder(v: View, private val converter: Converter<ProductComment, ProductCommentProvider>) : BaseRecyclerViewAdapter.ViewHolder<ProductComment>(v) {

        override fun populate(data: ProductComment) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                view_name.text = convert.provideName()
                view_time.text = convert.provideTime()
                view_content.text = convert.provideContent()
                view_rating.rating = convert.provideRate()
            }
        }
    }

    interface ProductCommentProvider {
        fun provideName(): String
        fun provideAvatar(): String
        fun provideTime(): String
        fun provideContent(): String
        fun provideRate(): Float
    }

    class ProductCommentConverter : Converter<ProductComment, ProductCommentProvider> {

        override fun convert(from: ProductComment): ProductCommentProvider {
            return object : ProductCommentProvider {
                override fun provideRate(): Float {
                    return from.rate ?: 0.0f
                }

                override fun provideName(): String {
                    return from.accountName ?: "Tên tài khoản"
                }

                override fun provideAvatar(): String {
                    return from.accountImage ?: ""
                }

                override fun provideTime(): String {
                    return from.updatedAt?.asDate() ?: ""
                }

                override fun provideContent(): String {
                    return from.content ?: ""
                }
            }
        }
    }

}