package ishopgo.com.exhibition.ui.community

import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.GridLayoutManager
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
    companion object {
        const val COMMUNITY_SHARE = 0
        const val COMMUNITY_LIST = 1

        const val COMMUNITY_SHARE_CLICK = 1
        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_COMMENT_CLICK = 3
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6

        const val COMMUNITY_SHARE_PRODUCT = 0
        const val COMMUNITY_SHARE_IMAGE = 1
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
                itemView.tv_community_like.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_LIKE_CLICK) }
                itemView.tv_community_comment.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_COMMENT_CLICK) }
                itemView.tv_community_number_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_NUMBER_CLICK) }
                itemView.cv_community_share.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHARE_PRODUCT_CLICK) }
                itemView.cv_community_product.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_PRODUCT_CLICK) }
            }
        }
    }

    inner class ProductHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(v) {

        override fun populate(data: CommunityProvider) {
            super.populate(data)
            itemView.apply {
                tv_community_username.text = data.userName()
                tv_community_time.text = data.provideTime()
                tv_community_content.text = data.provideContent()
                tv_community_product_name.text = data.provideProductName()
                tv_community_product_code.text = data.provideProductCode()
                tv_community_product_price.text = data.provideProductPrice()
                tv_community_like.text = data.provideLikeCount().toString()
                tv_community_comment.text = data.provideCommentCount().toString()
                tv_community_number_share.text = data.provideShareCount().toString()


                Glide.with(this).load(data.userAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_avatar)

                if (data.provideType() == COMMUNITY_SHARE_PRODUCT.toString()) {
                    img_community_image.visibility = View.GONE
                    Glide.with(this).load(data.provideProductListImage()[0].url())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_product)
                } else if (data.provideType() == COMMUNITY_SHARE_IMAGE.toString()) {
                    cv_community_product.visibility = View.GONE
                    if (data.provideProductListImage().size > 1) {
                        rv_community_image.visibility = View.VISIBLE
                        img_community_image.visibility = View.GONE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(data.provideProductListImage())
                        rv_community_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_community_image.adapter = adapter
                    } else {
                        img_community_image.visibility = View.VISIBLE
                        rv_community_image.visibility = View.GONE

                        Glide.with(this).load(data.provideProductListImage()[0].url())
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_image)
                    }

                }
            }
        }
    }

    inner class ShareHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityProvider>(v) {

        override fun populate(data: CommunityProvider) {
            super.populate(data)
            itemView.apply {
                Glide.with(this).load(data.currentUserAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_community_share_avatar)
            }
        }
    }
}