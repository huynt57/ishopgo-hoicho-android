package ishopgo.com.exhibition.ui.main.configbooth

import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_shop_relate.view.*

class BoothRelateAdapter : ClickableAdapter<BoothManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_shop_relate
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothManager> {
        return ProductHolder(v, BoothRelateConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION)
                listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class ProductHolder(v: View, private val converter: Converter<BoothManager, ShopRelateProvider>) : BaseRecyclerViewAdapter.ViewHolder<BoothManager>(v) {

        override fun populate(data: BoothManager) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                view_function.text = convert.provideFunction()
                view_name.text = convert.provideName()
                view_phone.setPhone(convert.providePhone(), data.hotline ?: "")
                view_place.text = convert.providePlace()
            }
        }
    }

    interface ShopRelateProvider {
        fun provideName(): String
        fun provideAvatar(): String
        fun providePlace(): String
        fun providePhone(): String
        fun provideFunction(): String
    }

    class BoothRelateConverter : Converter<BoothManager, ShopRelateProvider> {
        override fun convert(from: BoothManager): ShopRelateProvider {
            return object : ShopRelateProvider {
                override fun provideFunction(): String {
                    return from.content ?: ""
                }

                override fun providePlace(): String {
                    return from.address ?: ""
                }

                override fun providePhone(): String {
                    return from.hotline ?: ""
                }

                override fun provideName(): String {
                    return from.boothName ?: ""
                }

                override fun provideAvatar(): String {
                    return from.banner ?: ""
                }
            }
        }
    }
}