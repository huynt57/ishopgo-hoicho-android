package ishopgo.com.exhibition.ui.community

import android.annotation.SuppressLint
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_community.view.*
import kotlinx.android.synthetic.main.item_community_share.view.*

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityAdapter : ClickableAdapter<Community>() {
    companion object {
        const val COMMUNITY_SHARE = 0
        const val COMMUNITY_LIST = 1

        const val COMMUNITY_SHARE_CLICK = 1
        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_COMMENT_CLICK = 3
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
        const val COMMUNITY_IMAGE_CLICK = 7
        const val COMMUNITY_PROFILE_CLICK = 8

        const val LIKED = 1

    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == COMMUNITY_SHARE) R.layout.item_community_share else R.layout.item_community
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == COMMUNITY_SHARE) COMMUNITY_SHARE else COMMUNITY_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Community> {
        return if (viewType == COMMUNITY_SHARE) ShareHolder(v) else ProductHolder(v, CommunityConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Community>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ShareHolder) {
            holder.apply {
                itemView.constrain_community_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_CLICK) }
            }
        } else if (holder is ProductHolder) {
            holder.apply {
                itemView.tv_community_comment.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_COMMENT_CLICK) }
                itemView.tv_community_number_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_NUMBER_CLICK) }
                itemView.img_community_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_PRODUCT_CLICK) }
                itemView.cv_community_product.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PRODUCT_CLICK) }
                itemView.img_community_image.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_IMAGE_CLICK) }
                itemView.img_community_avatar.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PROFILE_CLICK) }
                itemView.tv_community_username.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PROFILE_CLICK) }

                if (UserDataManager.currentUserId > 0) {
                    val likeCount = getItem(adapterPosition).likeCount ?: 0
                    itemView.toggle_community_like.setOnClickListener {
                        if (itemView.toggle_community_like.isChecked) {
                            if (itemView.tv_community_like.text.toString().toInt() == likeCount) {
                                itemView.tv_community_like.text = (likeCount + 1).toString()
                                itemView.toggle_community_like.text = (likeCount + 1).toString()
                            } else {
                                itemView.toggle_community_like.text = (likeCount).toString()
                                itemView.tv_community_like.text = (likeCount).toString()
                            }
                        } else
                            if (itemView.tv_community_like.text.toString().toInt() == likeCount) {
                                itemView.toggle_community_like.text = (likeCount - 1).toString()
                                itemView.tv_community_like.text = (likeCount - 1).toString()
                            } else {
                                itemView.toggle_community_like.text = (likeCount).toString()
                                itemView.tv_community_like.text = (likeCount).toString()
                            }

                        listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_LIKE_CLICK)
                    }
                } else itemView.toggle_community_like.isEnabled = false
            }
        }
    }

    inner class ProductHolder(v: View, private val converter: Converter<Community, CommunityProvider>) : BaseRecyclerViewAdapter.ViewHolder<Community>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Community) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_community_username.text = convert.providerUserName()
                tv_community_time.text = convert.provideTime()
                tv_community_content.text = convert.provideContent()
                tv_community_comment.text = convert.provideCommentCount().toString()
                tv_community_number_share.text = convert.provideShareCount().toString()
                Log.d("123123123", convert.provideLiked().toString())
                toggle_community_like.isChecked = convert.provideLiked()
                toggle_community_like.text = convert.provideLikeCount().toString()
                tv_community_like.text = convert.provideLikeCount().toString()

                Glide.with(this).load(convert.providerUserAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_community_avatar)

                if (convert.provideProduct() != null) {
                    cv_community_product.visibility = View.VISIBLE
                    tv_community_like.visibility = View.VISIBLE
                    tv_community_like.text = "${convert.provideLikeCount()} thích"
                    toggle_community_like.visibility = View.GONE

                    Glide.with(this).load(convert.provideProduct()?.providerImage())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_product)
                    tv_community_product_name.text = convert.provideProduct()?.providerName()
                    tv_community_product_price.text = convert.provideProduct()?.providerPrice()
                } else {
                    cv_community_product.visibility = View.GONE
                    toggle_community_like.visibility = View.VISIBLE
                    tv_community_like.visibility = View.GONE
                }

                if (convert.provideListImage().isNotEmpty()) {
                    if (convert.provideListImage().size > 1) {
                        img_community_image.visibility = View.GONE
                        rv_community_image.visibility = View.VISIBLE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(convert.provideListImage())
                        rv_community_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_community_image.isNestedScrollingEnabled = false
                        rv_community_image.setHasFixedSize(false)
                        rv_community_image.adapter = adapter
                        (adapter as ClickableAdapter<String>).listener = object : ClickableAdapter.BaseAdapterAction<String> {
                            override fun click(position: Int, data: String, code: Int) {
                                listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_IMAGE_CLICK)
                            }
                        }
                    } else {
                        img_community_image.visibility = View.VISIBLE
                        rv_community_image.visibility = View.GONE

                        Glide.with(this).load(convert.provideListImage()[0])
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_image)
                    }
                } else {
                    img_community_image.visibility = View.GONE
                    rv_community_image.visibility = View.GONE
                }
            }
        }
    }

    inner class ShareHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<Community>(v) {

        override fun populate(data: Community) {
            super.populate(data)
            itemView.apply {
                Glide.with(this).load(UserDataManager.currentUserAvatar)
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_community_share_avatar)
            }
        }
    }

    interface CommunityProvider {
        fun providerUserName(): String
        fun providerUserAvatar(): String
        fun provideContent(): String
        fun provideTime(): String
        fun provideLikeCount(): Int
        fun provideLiked(): Boolean
        fun provideCommentCount(): Int
        fun provideShareCount(): Int
        fun provideProduct(): CommunityProductProvider?
        fun provideListImage(): MutableList<String>
    }

    class CommunityConverter : Converter<Community, CommunityProvider> {
        override fun convert(from: Community): CommunityProvider {
            return object : CommunityProvider {
                override fun provideLiked(): Boolean {
                    return isLiked()
                }

                override fun providerUserName(): String {
                    return if (from.accountName.isNullOrBlank()) "Người dùng ẩn danh" else from.accountName!!
                }

                override fun providerUserAvatar(): String {
                    return from.accountImage ?: ""
                }

                override fun provideContent(): String {
                    return from.content ?: ""
                }

                override fun provideTime(): String {
                    return from.createdAt?.asDateTime() ?: ""
                }

                override fun provideLikeCount(): Int {
                    return from.likeCount ?: 0
                }

                override fun provideCommentCount(): Int {
                    return from.commentCount ?: 0
                }

                override fun provideShareCount(): Int {
                    return from.shareCount ?: 0
                }

                override fun provideProduct(): CommunityProductProvider? {
                    return from.product
                }

                override fun provideListImage(): MutableList<String> {
                    return from.images ?: mutableListOf()
                }

                private fun isLiked() = from.liked == LIKED

            }
        }
    }
}