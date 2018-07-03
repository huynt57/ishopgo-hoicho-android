package ishopgo.com.exhibition.ui.community.comment.child

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.item_community_comment_child.view.*

class CommentChildAdapter : ClickableAdapter<CommunityComment>() {
    companion object {
        const val COMMUNITY_PARENT = 0
        const val COMMUNITY_LIST = 1

        const val COMMUNITY_REPLY = 0
        const val COMMUNITY_REPLY_CHILD = 1
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == COMMUNITY_PARENT) R.layout.item_community_comment else R.layout.item_community_comment_child
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == COMMUNITY_PARENT) COMMUNITY_PARENT else COMMUNITY_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityComment> {
        return if (viewType == COMMUNITY_PARENT) ParentHolder(v, CommunityConverter()) else ChildHolder(v, CommunityConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<CommunityComment>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ParentHolder) {
            holder.apply {
                itemView.img_avatar_comment.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
                itemView.tv_comment_name.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
                itemView.view_reply.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_REPLY) }
            }

        } else if (holder is ChildHolder) {
            holder.apply {
                itemView.img_avatar_comment_child.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
                itemView.tv_name_comment_child.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
                itemView.view_reply_comment_child.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), COMMUNITY_REPLY_CHILD) }
            }
        }
    }

    inner class ChildHolder(v: View, private val converter: Converter<CommunityComment, CommunityCommentProvider>) : BaseRecyclerViewAdapter.ViewHolder<CommunityComment>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: CommunityComment) {
            super.populate(data)
            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(this).load(convert.providerAccountImage())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder).error(R.drawable.avatar_placeholder)).into(img_avatar_comment_child)

                tv_content_comment_child.text = convert.providerContent()
                tv_name_comment_child.text = convert.providerAccountName()
                tv_time_comment_child.text = convert.providerCreatedAt()

                if (convert.providerImages().isNotEmpty()) {
                    if (convert.providerImages().size > 1) {
                        image_comment_child.visibility = View.GONE
                        rv_img_comment_child.visibility = View.VISIBLE

                        val adapter = CommunityImageAdapter()
                        adapter.replaceAll(convert.providerImages())
                        rv_img_comment_child.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                        rv_img_comment_child.adapter = adapter
                    } else {
                        image_comment_child.visibility = View.VISIBLE
                        rv_img_comment_child.visibility = View.GONE

                        Glide.with(this).load(convert.providerImages()[0])
                                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(image_comment_child)
                    }
                } else {
                    image_comment_child.visibility = View.GONE
                    rv_img_comment_child.visibility = View.GONE
                }
            }
        }
    }

    inner class ParentHolder(v: View, private val converter: Converter<CommunityComment, CommunityCommentProvider>) : BaseRecyclerViewAdapter.ViewHolder<CommunityComment>(v) {

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

                constraintLayout_child.visibility = View.GONE

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