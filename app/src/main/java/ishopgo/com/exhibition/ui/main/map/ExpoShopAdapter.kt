package ishopgo.com.exhibition.ui.main.map

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoShop
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_shop_location.view.*

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoShopAdapter : ClickableAdapter<ExpoShop>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_shop_location
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ExpoShop> {
        return ExpoShopHolder(v, ConverterExpoShop())
    }

    override fun onBindViewHolder(holder: ViewHolder<ExpoShop>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class ExpoShopHolder(v: View, private val converter: Converter<ExpoShop, ExpoShopProvider>) : BaseRecyclerViewAdapter.ViewHolder<ExpoShop>(v) {

        override fun populate(data: ExpoShop) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                view_number.text = converted.provideNumber()
                view_name.text = converted.provideName()
                view_region.text = converted.provideRegion()
                view_region.visibility = if (converted.isSetup()) View.VISIBLE else View.GONE
            }
        }
    }

    interface ExpoShopProvider {

        fun provideName(): CharSequence
        fun provideNumber(): CharSequence
        fun provideRegion(): CharSequence
        fun isSetup(): Boolean

    }

    class ConverterExpoShop : Converter<ExpoShop, ExpoShopProvider> {

        override fun convert(from: ExpoShop): ExpoShopProvider {
            return object : ExpoShopProvider {
                override fun isSetup(): Boolean {
                    return from.booth != null
                }

                override fun provideName(): CharSequence {
                    return when (from.boothId) {
                        0L -> if (UserDataManager.currentType == "Chủ hội chợ")
                            "<font color=\"red\">Thêm gian hàng</font>".asHtml()
                        else
                            "<font color=\"red\">Mua gian này</font>".asHtml()
                        else -> from.booth?.name ?: ""
                    }
                }

                override fun provideNumber(): CharSequence {
                    return "${from.priority ?: 0}"
                }

                override fun provideRegion(): CharSequence {
                    return from.booth?.city ?: ""
                }

            }
        }


    }
}