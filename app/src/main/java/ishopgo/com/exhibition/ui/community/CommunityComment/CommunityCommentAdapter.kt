package ishopgo.com.exhibition.ui.community.CommunityComment

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.community.CommunityCommentProvider
import ishopgo.com.exhibition.ui.community.CommunityImageAdapter
import kotlinx.android.synthetic.main.item_community_comment.view.*

/**
 * Created by hoangnh on 4/19/2018.
 */
class CommunityCommentAdapter : ClickableAdapter<CommunityCommentProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_community_comment
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityCommentProvider> {
        return CommentHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CommunityCommentProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {

        }
    }

    inner class CommentHodel(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityCommentProvider>(v) {

        override fun populate(data: CommunityCommentProvider) {
            super.populate(data)
            itemView.apply {
                Glide.with(this).load(data.providerAccountImage())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_avatar_comment)

                tv_comment_content.text = data.providerContent()
                tv_comment_name.text = data.providerAccountName()
                tv_comment_time.text = data.providerCreatedAt()

                if (data.providerImages().isNotEmpty()) {
                    if (data.providerImages().size > 1) {
                        img_comment.visibility = View.GONE
                        rv_comment_image.visibility = View.VISIBLE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(data.providerImages())
                        rv_comment_image.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_comment_image.adapter = adapter
                    } else {
                        img_comment.visibility = View.VISIBLE
                        rv_comment_image.visibility = View.GONE

                        Glide.with(this).load(data.providerImages()[0])
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_comment)
                    }
                } else {
                    img_comment.visibility = View.GONE
                    rv_comment_image.visibility = View.GONE
                }
            }
        }
    }
}