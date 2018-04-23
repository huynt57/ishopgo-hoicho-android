package ishopgo.com.exhibition.ui.community

import android.support.v7.content.res.AppCompatResources
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_community.view.*
import kotlinx.android.synthetic.main.item_community_share.view.*

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityAdapter : ClickableAdapter<CommunityProvider>() {
    var listenerClick: onClickListener? = null

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == 0) R.layout.item_community_share else R.layout.item_community
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityProvider> {
        return if (viewType == 0) ShareHolder(v) else ProductHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CommunityProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ShareHolder) {
            holder.apply {
                itemView.constrain_community_share.setOnClickListener { listenerClick?.onShareListenner(adapterPosition, getItem(adapterPosition)) }
            }
        } else if (holder is ProductHolder) {
            holder.apply {
                itemView.tv_community_like.setOnClickListener { listenerClick?.onClickAction(adapterPosition, getItem(adapterPosition)) }
                itemView.tv_community_comment.setOnClickListener { listenerClick?.onClickAction(adapterPosition, getItem(adapterPosition)) }
                itemView.tv_community_number_share.setOnClickListener { listenerClick?.onClickAction(adapterPosition, getItem(adapterPosition)) }
                itemView.cv_community_share.setOnClickListener { listenerClick?.onSelectShare(adapterPosition, getItem(adapterPosition)) }
                itemView.cv_community_product.setOnClickListener { listenerClick?.onClickProduct(adapterPosition, getItem(adapterPosition)) }
            }
        }
    }

    inner class ProductHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(v) {

        override fun populate(data: CommunityProvider) {
            super.populate(data)
            itemView.apply {
                val codeDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_barcode) }
                tv_community_product_code.setCompoundDrawablesWithIntrinsicBounds(codeDrawable, null, null, null)

                val likeDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_like_heath) }
                tv_community_like.setCompoundDrawablesWithIntrinsicBounds(likeDrawable, null, null, null)

                val commentDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_comment_black) }
                tv_community_comment.setCompoundDrawablesWithIntrinsicBounds(commentDrawable, null, null, null)

                val shareDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_share_option) }
                tv_community_number_share.setCompoundDrawablesWithIntrinsicBounds(shareDrawable, null, null, null)

                val shareWhiteDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_share_white) }
                img_community_share.setCompoundDrawablesWithIntrinsicBounds(shareWhiteDrawable, null, null, null)

                tv_community_username.text = data.userName()
                tv_community_time.text = data.communityTime()
                tv_community_content.text = data.communityContent()
                tv_community_content.text = data.communityContent()
                tv_community_product_name.text = data.communityProductName()
                tv_community_product_code.text = data.communityProductCode()
                tv_community_product_price.text = data.communityProductPrice()
                tv_community_like.text = data.communityLike().toString()
                tv_community_comment.text = data.communityComment().toString()
                tv_community_number_share.text = data.communityShare().toString()

                Glide.with(this).load(data.userAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.image_placeholder)).into(img_community_avatar)

                Glide.with(this).load(data.communityProductImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)).into(img_community_product)
            }
        }
    }

    inner class ShareHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(v) {

        override fun populate(data: CommunityProvider) {
            super.populate(data)
            itemView.apply {
                Glide.with(this).load(data.currentUserAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.image_placeholder)).into(img_community_share_avatar)
            }
        }
    }

    interface onClickListener {
        fun onShareListenner(position: Int, item: CommunityProvider)
        fun onClickAction(position: Int, item: CommunityProvider)
        fun onSelectShare(position: Int, item: CommunityProvider)
        fun onClickProduct(position: Int, item: CommunityProvider)
    }
}