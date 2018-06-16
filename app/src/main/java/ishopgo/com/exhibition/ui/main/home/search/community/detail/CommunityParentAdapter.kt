package ishopgo.com.exhibition.ui.main.home.search.community.detail

import android.annotation.SuppressLint
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.community.CommunityImageAdapter
import ishopgo.com.exhibition.ui.community.CommunityProvider
import kotlinx.android.synthetic.main.item_community.view.*

class CommunityParentAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<CommunityProvider>() {
    companion object {
        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_COMMENT_CLICK = 3
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
        const val COMMUNITY_IMAGE_CLICK = 7
        const val COMMUNITY_PROFILE_CLICK = 8
    }

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_community
    }


    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityProvider> {
        val productHolder = ProductHolder(v)
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<CommunityProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ProductHolder) {
            holder.apply {
                itemView.tv_community_comment.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_COMMENT_CLICK) }
                itemView.tv_community_number_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_NUMBER_CLICK) }
                itemView.img_community_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_PRODUCT_CLICK) }
                itemView.cv_community_product.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PRODUCT_CLICK) }
                itemView.img_community_image.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_IMAGE_CLICK) }
                itemView.tv_community_username.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PROFILE_CLICK) }
                itemView.img_community_avatar.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PROFILE_CLICK) }

                if (UserDataManager.currentUserId > 0) {
                    itemView.toggle_community_like.setOnClickListener {
                        if (itemView.toggle_community_like.isChecked) {
                            if (itemView.tv_community_like.text.toString().toInt() == getItem(adapterPosition).provideLikeCount()) {
                                itemView.tv_community_like.text = (getItem(adapterPosition).provideLikeCount() + 1).toString()
                                itemView.toggle_community_like.text = (getItem(adapterPosition).provideLikeCount() + 1).toString()
                            } else {
                                itemView.toggle_community_like.text = (getItem(adapterPosition).provideLikeCount()).toString()
                                itemView.tv_community_like.text = (getItem(adapterPosition).provideLikeCount()).toString()
                            }
                        } else
                            if (itemView.tv_community_like.text.toString().toInt() == getItem(adapterPosition).provideLikeCount()) {
                                itemView.toggle_community_like.text = (getItem(adapterPosition).provideLikeCount() - 1).toString()
                                itemView.tv_community_like.text = (getItem(adapterPosition).provideLikeCount() - 1).toString()
                            } else {
                                itemView.toggle_community_like.text = (getItem(adapterPosition).provideLikeCount()).toString()
                                itemView.tv_community_like.text = (getItem(adapterPosition).provideLikeCount()).toString()
                            }

                        listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_LIKE_CLICK)
                    }
                } else itemView.toggle_community_like.isEnabled = false
            }
        }
    }

    internal inner class ProductHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(view) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: CommunityProvider) {
            super.populate(data)

            itemView.apply {
                tv_community_username.text = data.providerUserName()
                tv_community_time.text = data.provideTime()
                tv_community_content.text = data.provideContent()
                tv_community_comment.text = data.provideCommentCount().toString()
                tv_community_number_share.text = data.provideShareCount().toString()
                toggle_community_like.isChecked = data.provideLiked()
                toggle_community_like.text = data.provideLikeCount().toString()
                tv_community_like.text = data.provideLikeCount().toString()

                Glide.with(this).load(data.providerUserAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_community_avatar)

                tv_community_number_share.visibility = View.GONE

                if (data.provideProduct() != null) {
                    cv_community_product.visibility = View.VISIBLE
                    tv_community_number_share.visibility = View.VISIBLE
                    tv_community_like.visibility = View.VISIBLE
                    tv_community_like.text = "${data.provideLikeCount()} thích"
                    toggle_community_like.visibility = View.GONE

                    Glide.with(this).load(data.provideProduct()?.providerImage())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_product)
                    tv_community_product_name.text = data.provideProduct()?.providerName()
                    tv_community_product_price.text = data.provideProduct()?.providerPrice()
                } else {
                    cv_community_product.visibility = View.GONE
                    toggle_community_like.visibility = View.VISIBLE
                    tv_community_like.visibility = View.GONE
                }

                if (data.provideListImage().isNotEmpty()) {
                    if (data.provideListImage().size > 1) {
                        img_community_image.visibility = View.GONE
                        rv_community_image.visibility = View.VISIBLE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(data.provideListImage())
                        rv_community_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_community_image.adapter = adapter
                        (adapter as ClickableAdapter<String>).listener = object : ClickableAdapter.BaseAdapterAction<String> {
                            override fun click(position: Int, data: String, code: Int) {
                                listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_IMAGE_CLICK)
                            }
                        }
                    } else {
                        img_community_image.visibility = View.VISIBLE
                        rv_community_image.visibility = View.GONE

                        Glide.with(this).load(data.provideListImage()[0])
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_image)
                    }
                } else {
                    img_community_image.visibility = View.GONE
                    rv_community_image.visibility = View.GONE
                }
            }
        }
    }
}