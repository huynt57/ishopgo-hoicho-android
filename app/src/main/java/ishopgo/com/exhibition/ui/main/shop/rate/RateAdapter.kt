package ishopgo.com.exhibition.ui.main.shop.rate

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_shop_rate.view.*

/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class RateAdapter : BaseRecyclerViewAdapter<ShopRateProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_shop_rate
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ShopRateProvider> {
        return ProductHolder(v)
    }

    inner class ProductHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ShopRateProvider>(v) {

        override fun populate(data: ShopRateProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                view_name.text = data.provideName()
                view_time.text = data.provideTime()
                view_content.text = data.provideContent()
            }
        }
    }


}