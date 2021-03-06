package ishopgo.com.exhibition.ui.main.administrator

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_member_administrator.view.*


class AdministratorAdapter : ClickableAdapter<Administrator>() {

    companion object {
        const val CLICK_MORE = 0
        const val CLICK_USER_INFOR = 1
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_member_administrator
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Administrator> {
        return Holder(v, AdministratorConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Administrator>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.img_more.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_MORE) }
            itemView.view_name.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_USER_INFOR) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<Administrator, AdministratorProvider>) : BaseRecyclerViewAdapter.ViewHolder<Administrator>(v) {

        override fun populate(data: Administrator) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideAvatar())
                        .apply(RequestOptions
                                .circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                        ).into(view_avatar)
                view_name.text = convert.provideName()
                view_phone.setPhone(convert.providePhone(), data.phone ?: "")
            }
        }
    }

    interface AdministratorProvider {
        fun provideName(): String
        fun providePhone(): CharSequence
        fun provideAvatar(): String
    }

    class AdministratorConverter : Converter<Administrator, AdministratorProvider> {

        override fun convert(from: Administrator): AdministratorProvider {
            return object : AdministratorProvider {
                override fun providePhone(): CharSequence {
                    return from.phone ?: ""
                }

                override fun provideAvatar(): String {
                    return from.image ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

            }
        }
    }
}