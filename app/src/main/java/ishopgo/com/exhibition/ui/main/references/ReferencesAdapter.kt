package ishopgo.com.exhibition.ui.main.references

import android.content.Intent
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asStylePhoneNumber
import kotlinx.android.synthetic.main.item_visitor.view.*

class ReferencesAdapter : ClickableAdapter<MemberManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_visitor
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<MemberManager> {
        return Holder(v, MemberManagersConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<MemberManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<MemberManager, MemberManagersProvider>) : BaseRecyclerViewAdapter.ViewHolder<MemberManager>(v) {

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
                        .into(view_avatar)
                view_name.text = convert.provideName()
                view_phone.text = convert.providePhone()
                view_phone.setOnClickListener {
                    val uri = Uri.parse("tel:${convert.providePhone()}")
                    val i = Intent(Intent.ACTION_DIAL, uri)
                    it.context.startActivity(i)
                }
                view_region.text = convert.provideRegion()
            }
        }
    }

    interface MemberManagersProvider {
        fun provideName(): CharSequence
        fun provideAvatar(): CharSequence
        fun providePhone(): CharSequence
        fun provideEmail(): CharSequence
        fun provideRegion(): CharSequence
    }

    class MemberManagersConverter : Converter<MemberManager, MemberManagersProvider> {
        override fun convert(from: MemberManager): MemberManagersProvider {
            return object : MemberManagersProvider {
                override fun provideRegion(): CharSequence {
                    return from.region ?: ""
                }

                override fun provideName(): CharSequence {
                    return from.name ?: ""
                }

                override fun provideAvatar(): CharSequence {
                    return from.image ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.phone?.asStylePhoneNumber()?.asHtml() ?: ""
                }

                override fun provideEmail(): CharSequence {
                    return from.email ?: ""
                }
            }
        }
    }
}