package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_tracking.view.*

class TrackingAdapter : ClickableAdapter<Tracking>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_tracking
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Tracking> {
        return Holder(v, TrackingConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Tracking>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }


    inner class Holder(v: View, private val converter: Converter<Tracking, TrackingProvider>) : BaseRecyclerViewAdapter.ViewHolder<Tracking>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Tracking) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                view_stt.text = "${adapterPosition + 1}"
                view_shop_name.text = convert.provideShop()
                view_shop_address.text = convert.provideAddress()
                view_shop_title.text = convert.provideTitle()
                view_shop_phone.setPhone(convert.providePhone(), data.valuePhone ?: "")

                view_shop_title.visibility = if (convert.provideTitle().isNotEmpty()) View.VISIBLE else View.GONE
                view_shop_name.visibility = if (convert.provideShop().isNotEmpty()) View.VISIBLE else View.GONE
                view_shop_address.visibility = if (convert.provideAddress().isNotEmpty()) View.VISIBLE else View.GONE
                view_shop_phone.visibility = if (convert.providePhone().isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    interface TrackingProvider {
        fun providePhone(): CharSequence
        fun provideTitle(): CharSequence
        fun provideShop(): CharSequence
        fun provideAddress(): CharSequence
    }

    class TrackingConverter : Converter<Tracking, TrackingProvider> {
        override fun convert(from: Tracking): TrackingProvider {
            return object : TrackingProvider {
                override fun provideTitle(): CharSequence {
                    return from.title ?: ""
                }

                override fun provideShop(): CharSequence {
                    return from.valueName ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.valuePhone ?: ""
                }

                override fun provideAddress(): CharSequence {
                    return from.valueAddress ?: ""
                }
            }
        }
    }
}