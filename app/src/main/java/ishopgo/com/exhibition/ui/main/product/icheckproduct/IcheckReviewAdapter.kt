package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckReview
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asConvertUnixtime
import kotlinx.android.synthetic.main.item_product_comment.view.*

class IcheckReviewAdapter : ClickableAdapter<IcheckReview>() {
    companion object {
        const val COMMUNITY_IMAGE_CLICK = 0
    }


    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_comment
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<IcheckReview> {
        return ProductHolder(v, ProductCommentConverter())
    }

    inner class ProductHolder(v: View, private val converter: Converter<IcheckReview, ProductCommentProvider>) : BaseRecyclerViewAdapter.ViewHolder<IcheckReview>(v) {

        override fun populate(data: IcheckReview) {
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

                constraintLayout_child.visibility = View.GONE
                tv_show_child_all.visibility = View.GONE

//                if (convert.providerImages().isNotEmpty()) {
//                    if (convert.providerImages().size > 1) {
//                        img_comment.visibility = View.GONE
//                        rv_comment_image.visibility = View.VISIBLE
//
//                        val adapter = CommunityImageAdapter()
//                        adapter.replaceAll(convert.providerImages())
//                        rv_comment_image.isNestedScrollingEnabled = false
//                        rv_comment_image.setHasFixedSize(true)
//                        rv_comment_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//                        rv_comment_image.adapter = adapter
//                        adapter.listener = object : ClickableAdapter.BaseAdapterAction<String> {
//                            override fun click(position: Int, data: String, code: Int) {
//                                listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_IMAGE_CLICK)
//                            }
//                        }
//                    } else {
//                        img_comment.visibility = View.VISIBLE
//                        rv_comment_image.visibility = View.GONE
//
//                        Glide.with(this).load(convert.providerImages()[0])
//                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_comment)
//                    }
//                } else {
//                    img_comment.visibility = View.GONE
//                    rv_comment_image.visibility = View.GONE
//                }
            }
        }
    }

    interface ProductCommentProvider {
        fun provideName(): String
        fun provideAvatar(): String
        fun provideTime(): String
        fun provideContent(): String
        fun provideRate(): Float
        fun provideCommentCount(): Int
    }

    class ProductCommentConverter : Converter<IcheckReview, ProductCommentProvider> {

        override fun convert(from: IcheckReview): ProductCommentProvider {
            return object : ProductCommentProvider {

                override fun provideCommentCount(): Int {
                    return from.commentCount ?: 0
                }

                override fun provideRate(): Float {
                    return from.star?.toFloat() ?: 0.0f
                }

                override fun provideName(): String {
                    return from.owner?.socialName ?: "Tên người dùng"
                }

                override fun provideAvatar(): String {
                    return from.owner?.cover ?: ""
                }

                override fun provideTime(): String {
//                    return from.updatedAt.toString().asConvertUnixtime() ?: ""
                    return ""
                }

                override fun provideContent(): String {
                    return from.content ?: ""
                }
            }
        }
    }

}