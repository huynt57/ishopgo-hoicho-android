package ishopgo.com.exhibition.ui.main.product.detail.exchange_diary

import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ListBGBN
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_bgbn.view.*

class BGBNAdapter : ClickableAdapter<ListBGBN>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_bgbn
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ListBGBN> {
        return ExpoShopHolder(v, ConverterBGBN())
    }

    override fun onBindViewHolder(holder: ViewHolder<ListBGBN>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }

    }

    inner class ExpoShopHolder(v: View, private val converter: Converter<ListBGBN, ExpoBGBNProvider>) : BaseRecyclerViewAdapter.ViewHolder<ListBGBN>(v) {

        override fun populate(data: ListBGBN) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                view_name.text = if (data.type == 1)
                    converted.provideBoothName()
                else converted.provideName()

                view_phone.setPhone(converted.providePhone(), data.phone ?: "")
            }
        }
    }

    interface ExpoBGBNProvider {

        fun provideBoothName(): CharSequence
        fun provideName(): CharSequence
        fun providePhone(): CharSequence
    }

    class ConverterBGBN : Converter<ListBGBN, ExpoBGBNProvider> {

        override fun convert(from: ListBGBN): ExpoBGBNProvider {
            return object : ExpoBGBNProvider {
                override fun provideBoothName(): CharSequence {
                    return from.boothName ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.phone ?: ""
                }

                override fun provideName(): CharSequence {
                    return from.name ?: ""
                }
            }
        }
    }
}