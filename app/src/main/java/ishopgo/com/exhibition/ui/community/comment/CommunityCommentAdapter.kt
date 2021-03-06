package ishopgo.com.exhibition.ui.community.comment

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.CommunityImageAdapter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_community_comment.view.*

/**
 * Created by hoangnh on 4/19/2018.
 */
class CommunityCommentAdapter : ClickableAdapter<CommunityComment>() {
    companion object {
        const val COMMUNITY_REPLY = 0
        const val COMMUNITY_REPLY_CHILD = 1
        const val COMMUNITY_SHOW_CHILD = 2
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_community_comment
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityComment> {
        return CommentHodel(v, CommunityConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<CommunityComment>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.img_avatar_comment.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            itemView.tv_comment_name.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            itemView.view_reply.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_REPLY) }
            itemView.view_reply_child.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_REPLY_CHILD) }
            itemView.tv_show_child_all.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_SHOW_CHILD) }
        }
    }

    inner class CommentHodel(v: View, private val converter: Converter<CommunityComment, CommunityCommentProvider>) : BaseRecyclerViewAdapter.ViewHolder<CommunityComment>(v) {

        override fun populate(data: CommunityComment) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(this).load(convert.providerAccountImage())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_avatar_comment)

                tv_comment_content.text = convert.providerContent()
                tv_comment_name.text = convert.providerAccountName()
                tv_comment_time.text = convert.providerCreatedAt()

                if (convert.providerLastComment() != null) {
                    constraintLayout_child.visibility = View.VISIBLE
                    val child = convert.providerLastComment()!!
                    Glide.with(this).load(child.accountImage)
                            .apply(RequestOptions.circleCropTransform()
                                    .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_avatar_child)

                    tv_comment_content_child.text = child.content
                    tv_comment_name_child.text = child.accountName
                    tv_comment_time_child.text = child.createdAt

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

                if (convert.provideCommentCount() > 1) {
                    tv_show_child_all.visibility = View.VISIBLE
                } else tv_show_child_all.visibility = View.GONE

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

    interface CommunityCommentProvider {
        fun providerContent(): String
        fun providerAccountName(): String
        fun providerAccountImage(): String
        fun providerPostId(): Long
        fun providerUpdatedAt(): String
        fun providerCreatedAt(): String
        fun providerImages(): MutableList<String>
        fun provideCommentCount(): Int
        fun providerLastComment(): CommunityComment?
    }

    class CommunityConverter : Converter<CommunityComment, CommunityCommentProvider> {
        override fun convert(from: CommunityComment): CommunityCommentProvider {
            return object : CommunityCommentProvider {
                override fun providerLastComment(): CommunityComment? {
                    return from.lastComment
                }

                override fun providerAccountImage(): String {
                    return from.accountImage ?: ""
                }

                override fun providerAccountName(): String {
                    return if (from.accountName.isNullOrBlank()) "Người dùng ẩn danh" else from.accountName!!
                }

                override fun providerPostId(): Long {
                    return from.postId ?: 0
                }

                override fun providerUpdatedAt(): String {
                    return from.updatedAt?.asDateTime() ?: ""

                }

                override fun providerCreatedAt(): String {
                    return from.createdAt?.asDateTime() ?: ""
                }

                override fun providerImages(): MutableList<String> {
                    return from.images ?: mutableListOf()
                }

                override fun provideCommentCount(): Int {
                    return from.commentCount ?: 0
                }

                override fun providerContent(): String {
                    return from.content ?: ""
                }
            }
        }
    }
}