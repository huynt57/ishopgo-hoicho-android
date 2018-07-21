package ishopgo.com.exhibition.ui.main.membermanager.deletedmember

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_member_manager.view.*

class DeletedMemberAdapter : ClickableAdapter<MemberManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_member_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<MemberManager> {
        return Holder(v, MemberConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<MemberManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_restore.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class Holder(v: View, private val converter: Converter<MemberManager, MemberManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<MemberManager>(v) {

        override fun populate(data: MemberManager) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideAvatar())
                        .apply(RequestOptions()
                                .circleCrop()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(img_member_avatar)
                tv_member_manager_name.text = convert.provideName()
                tv_member_manager_phone.text = convert.providePhone().setPhone(data.phone ?: "")
                tv_member_manager_phone.movementMethod = LinkMovementMethod.getInstance()
                tv_member_manager_region.text = convert.provideRegion()
                btn_restore.visibility = View.VISIBLE
            }
        }

    }

    interface MemberManagerProvider {
        fun provideName(): String
        fun providePhone(): CharSequence
        fun provideEmail(): String
        fun provideRegion(): String
        fun provideBooth(): String
        fun provideAvatar(): String
    }

    class MemberConverter : Converter<MemberManager, MemberManagerProvider> {

        override fun convert(from: MemberManager): MemberManagerProvider {
            return object : MemberManagerProvider {
                override fun provideAvatar(): String {
                    return from.image ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.phone ?: ""
                }

                override fun provideEmail(): String {
                    return from.email ?: ""
                }

                override fun provideRegion(): String {
                    return from.region?.trim() ?: ""
                }

                override fun provideBooth(): String {
                    return ""
                }
            }
        }

    }
}