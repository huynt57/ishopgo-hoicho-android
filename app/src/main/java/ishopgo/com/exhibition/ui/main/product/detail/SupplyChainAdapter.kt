package ishopgo.com.exhibition.ui.main.product.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Booth
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_supply_chain.view.*

class SupplyChainAdapter : ClickableAdapter<Booth>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_supply_chain
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Booth> {
        return ProcessHolder(v, ConverterProductProcess())
    }

    override fun onBindViewHolder(holder: ViewHolder<Booth>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    class ProcessHolder(v: View, private val converter: Converter<Booth, ProcessProvider>) : BaseRecyclerViewAdapter.ViewHolder<Booth>(v) {

        override fun populate(data: Booth) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                view_label_name.text = converted.provideTitle()
                view_name.text = converted.provideName()
                tv_phone.setPhone(converted.provideHotline(), data.hotline ?: "")
                tv_address.text = converted.provideAddress()
                view_product_count.text = converted.provideProductCount()
                view_rating.text = converted.provideRatingPoint()
            }
        }
    }

    interface ProcessProvider {
        fun provideName(): CharSequence
        fun provideTitle(): CharSequence
        fun provideHotline(): CharSequence
        fun provideAddress(): CharSequence
        fun provideProductCount(): CharSequence
        fun provideRatingPoint(): CharSequence
    }

    class ConverterProductProcess : Converter<Booth, ProcessProvider> {
        override fun convert(from: Booth): ProcessProvider {
            return object : ProcessProvider {
                override fun provideTitle(): CharSequence {
                    return from.title ?: "Đơn vị liên quan"
                }

                override fun provideHotline(): CharSequence {
                    return from.hotline ?: ""
                }

                override fun provideAddress(): CharSequence {
                    return if (from.address?.isNotEmpty() == true)
                        "${from.address ?: ""}, ${from.city ?: ""}"
                    else from.city ?: ""
                }

                override fun provideProductCount(): CharSequence {
                        return "<b><font color=\"#00c853\">${from.count
                                ?: 0}</font></b><br>Sản phẩm".asHtml()

                }

                override fun provideRatingPoint(): CharSequence {
                    return "<b><font color=\"red\">${from.rate?.toFloat()
                            ?: 0.0f}/5.0</font></b><br>${from.rateCount
                            ?: 0} Đánh giá".asHtml()
                }

                override fun provideName(): CharSequence {
                    return from.name ?: ""
                }
            }
        }
    }

}