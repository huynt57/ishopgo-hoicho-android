package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.CommunityImageAdapter
import ishopgo.com.exhibition.ui.extensions.asDate
import kotlinx.android.synthetic.main.item_product_comment.view.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductCommentAdapter : ClickableAdapter<ProductComment>() {
    companion object {
        const val COMMUNITY_REPLY = 0
        const val COMMUNITY_REPLY_CHILD = 1
        const val COMMUNITY_SHOW_CHILD = 2
    }


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
            itemView.view_reply.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_REPLY) }
            itemView.view_reply_child.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_REPLY_CHILD) }
            itemView.tv_show_child_all.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHOW_CHILD) }
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

                if (convert.providerLastComment() != null) {
                    constraintLayout_child.visibility = View.GONE
                    val child = convert.providerLastComment()!!
                    Glide.with(this).load(child.accountImage)
                            .apply(RequestOptions.circleCropTransform()
                                    .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_avatar_child)

                    tv_comment_content_child.text = child.content
                    tv_comment_name_child.text = child.accountName
                    tv_comment_time_child.text = child.updatedAt

                    if (child.images != null && child.images!!.isNotEmpty()) {
                        if (child.images!!.size > 1) {
                            img_comment_child.visibility = View.GONE
                            rv_comment_image_child.visibility = View.VISIBLE

                            val adapter = CommunityImageAdapter()
                            adapter.replaceAll(child.images!!)
                            rv_comment_image_child.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                            rv_comment_image_child.adapter = adapter
                        } else {
                            img_comment_child.visibility = View.VISIBLE
                            rv_comment_image_child.visibility = View.GONE

                            Glide.with(this).load(child.images!![0])
                                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_comment_child)
                        }
                    }
                } else {
                    constraintLayout_child.visibility = View.GONE
                }

                tv_show_child_all.visibility = View.GONE

                if (convert.providerImages().isNotEmpty()) {
                    if (convert.providerImages().size > 1) {
                        img_comment.visibility = View.GONE
                        rv_comment_image.visibility = View.VISIBLE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(convert.providerImages())
                        rv_comment_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_comment_image.adapter = adapter
                    } else {
                        img_comment.visibility = View.VISIBLE
                        rv_comment_image.visibility = View.GONE

                        Glide.with(this).load(convert.providerImages()[0])
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_comment)
                    }
                } else {
                    img_comment.visibility = View.GONE
                    rv_comment_image.visibility = View.GONE
                }
            }
        }
    }

    interface ProductCommentProvider {
        fun provideName(): String
        fun provideAvatar(): String
        fun providerImages(): MutableList<String>
        fun provideTime(): String
        fun provideContent(): String
        fun provideRate(): Float
        fun providerLastComment(): ProductComment?
        fun provideCommentCount(): Int
    }

    class ProductCommentConverter : Converter<ProductComment, ProductCommentProvider> {

        override fun convert(from: ProductComment): ProductCommentProvider {
            return object : ProductCommentProvider {
                override fun providerImages(): MutableList<String> {
                    return from.images ?: mutableListOf()
                }

                override fun provideCommentCount(): Int {
                    return from.commentCount ?: 0
                }

                override fun providerLastComment(): ProductComment? {
                    return from.lastComment
                }

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