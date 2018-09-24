package ishopgo.com.exhibition.ui.main.map.choosebooth

import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asColor
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_shop_location.view.*

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoBoothAdapter : ClickableAdapter<BoothManager>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_shop_location
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothManager> {
        return ExpoShopHolder(v, ConverterExpoShop())
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothManager>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class ExpoShopHolder(v: View, private val converter: Converter<BoothManager, ExpoBoothProvider>) : BaseRecyclerViewAdapter.ViewHolder<BoothManager>(v) {

        override fun populate(data: BoothManager) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                view_name.setTextColor(R.color.colorPrimaryText.asColor(context))

                view_number.text = converted.provideNumber()
                view_name.text = converted.provideName()
                view_region.setPhone(converted.provideRegion(), data.phone ?: "")
                view_region.visibility = View.VISIBLE
                view_delete_salePoint.visibility = View.GONE
                view_phone.visibility = View.GONE
            }
        }
    }

    interface ExpoBoothProvider {

        fun provideName(): CharSequence
        fun provideNumber(): CharSequence
        fun provideRegion(): CharSequence

    }

    class ConverterExpoShop : Converter<BoothManager, ExpoBoothProvider> {

        override fun convert(from: BoothManager): ExpoBoothProvider {
            return object : ExpoBoothProvider {

                override fun provideName(): CharSequence {
                    return if (from.boothName.isNullOrBlank())
                        from.name ?: ""
                    else
                        from.boothName ?: ""
                }

                override fun provideNumber(): CharSequence {
                    return ""
                }

                override fun provideRegion(): CharSequence {
                    return "${from.phone} - ${from.region
                            ?: ""}"
                }

            }
        }


    }
}