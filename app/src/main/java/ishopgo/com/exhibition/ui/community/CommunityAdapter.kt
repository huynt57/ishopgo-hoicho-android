package ishopgo.com.exhibition.ui.community

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_community.view.*
import kotlinx.android.synthetic.main.item_community_share.view.*

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityAdapter : ClickableAdapter<CommunityProvider>() {
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
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == COMMUNITY_SHARE) R.layout.item_community_share else R.layout.item_community
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == COMMUNITY_SHARE) COMMUNITY_SHARE else COMMUNITY_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityProvider> {
        return if (viewType == COMMUNITY_SHARE) ShareHolder(v) else ProductHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CommunityProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ShareHolder) {
            holder.apply {
                itemView.constrain_community_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_CLICK) }
            }
        } else if (holder is ProductHolder) {
            holder.apply {
                itemView.linear_like.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_LIKE_CLICK) }
                itemView.tv_community_comment.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_COMMENT_CLICK) }
                itemView.tv_community_number_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_NUMBER_CLICK) }
                itemView.cv_community_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_PRODUCT_CLICK) }
                itemView.cv_community_product.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PRODUCT_CLICK) }
                itemView.img_community_image.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_IMAGE_CLICK) }
            }
        }
    }

    inner class ProductHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(v) {

        override fun populate(data: CommunityProvider) {
            super.populate(data)
            itemView.apply {
                tv_community_username.text = data.providerUserName()
                tv_community_time.text = data.provideTime()
                tv_community_content.text = data.provideContent()
                tv_community_like.text = data.provideLikeCount().toString()
                tv_community_comment.text = data.provideCommentCount().toString()
                tv_community_number_share.text = data.provideShareCount().toString()

                if (data.provideLiked() == 0)
                    Glide.with(context)
                            .load(R.drawable.ic_added_to_favorite_24dp)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder))
                            .into(img_community_like)
                else Glide.with(context)
                        .load(R.drawable.ic_add_to_favorite_24dp)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(img_community_like)

                Glide.with(this).load(data.providerUserAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_community_avatar)

                cv_community_share.visibility = View.GONE
                tv_community_number_share.visibility = View.GONE

                if (data.provideProduct() != null) {
                    cv_community_product.visibility = View.VISIBLE
                    cv_community_share.visibility = View.VISIBLE
                    tv_community_number_share.visibility = View.VISIBLE

                    Glide.with(this).load(data.provideProduct()?.providerImage())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_product)
                    tv_community_product_name.text = data.provideProduct()?.providerName()
                    tv_community_product_price.text = data.provideProduct()?.providerPrice()
                } else cv_community_product.visibility = View.GONE

                if (data.provideListImage().isNotEmpty()) {
                    if (data.provideListImage().size > 1) {
                        img_community_image.visibility = View.GONE
                        rv_community_image.visibility = View.VISIBLE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(data.provideListImage())
                        rv_community_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_community_image.adapter = adapter
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

    inner class ShareHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(v) {

        override fun populate(data: CommunityProvider) {
            super.populate(data)
            itemView.apply {
                Glide.with(this).load(UserDataManager.currentUserAvatar)
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_community_share_avatar)
            }
        }
    }
}