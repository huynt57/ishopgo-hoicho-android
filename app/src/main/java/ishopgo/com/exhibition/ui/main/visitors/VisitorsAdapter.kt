package ishopgo.com.exhibition.ui.main.visitors

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Visitor
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_visitor.view.*

class VisitorsAdapter : ClickableAdapter<Visitor>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_visitor
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Visitor> {
        return Holder(v, VisitorsConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Visitor>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<Visitor, VisitorsProvider>) : BaseRecyclerViewAdapter.ViewHolder<Visitor>(v) {

        override fun populate(data: Visitor) {
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
                view_phone.setPhone(convert.providePhone(), data.phone ?: "")
                view_region.text = convert.provideRegion()
            }
        }
    }

    interface VisitorsProvider {
        fun provideName(): CharSequence
        fun provideAvatar(): CharSequence
        fun providePhone(): CharSequence
        fun provideEmail(): CharSequence
        fun provideRegion(): CharSequence
    }

    class VisitorsConverter : Converter<Visitor, VisitorsProvider> {
        override fun convert(from: Visitor): VisitorsProvider {
            return object : VisitorsProvider {
                override fun provideRegion(): CharSequence {
                    return from.city ?: ""
                }

                override fun provideName(): CharSequence {
                    return from.name ?: ""
                }

                override fun provideAvatar(): CharSequence {
                    return from.image ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.phone ?: ""
                }

                override fun provideEmail(): CharSequence {
                    return from.email ?: ""
                }
            }
        }
    }
}