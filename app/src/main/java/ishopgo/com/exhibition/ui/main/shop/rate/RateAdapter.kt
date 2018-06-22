package ishopgo.com.exhibition.ui.main.shop.rate

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ShopRate
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import kotlinx.android.synthetic.main.item_shop_rate.view.*

/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class RateAdapter : ClickableAdapter<ShopRate>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_shop_rate
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ShopRate> {
        return ProductHolder(v, ShopRateConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<ShopRate>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.view_avatar.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            itemView.view_name.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class ProductHolder(v: View, private val converter: Converter<ShopRate, ShopRateProvider>) : BaseRecyclerViewAdapter.ViewHolder<ShopRate>(v) {

        override fun populate(data: ShopRate) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                view_name.text = convert.provideName()
                view_time.text = convert.provideTime()
                view_content.text = convert.provideContent()
                view_rate_point.rating = convert.provideRating()
            }
        }
    }

    interface ShopRateProvider {
        fun provideName(): String
        fun provideAvatar(): String
        fun provideTime(): String
        fun provideContent(): String
        fun provideRating(): Float
    }

    class ShopRateConverter : Converter<ShopRate, ShopRateProvider> {
        override fun convert(from: ShopRate): ShopRateProvider {
            return object : ShopRateProvider {
                override fun provideRating(): Float {
                    return from.ratePoint?.toFloat() ?: 0.0F
                }

                override fun provideName(): String {
                    val name = from.account?.name
                    return if (name.isNullOrBlank()) "Người dùng ẩn danh" else name!!
                }

                override fun provideAvatar(): String {
                    return from.account?.image ?: ""
                }

                override fun provideTime(): String {
                    return from.time?.asDate() ?: ""
                }

                override fun provideContent(): String {
                    return from.content ?: ""
                }
            }
        }
    }
}