package ishopgo.com.exhibition.ui.main.map

import android.graphics.Color
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asColor
import kotlinx.android.synthetic.main.item_shop_location.view.*

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoShopAdapter : ClickableAdapter<ExpoShopProvider>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_shop_location
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ExpoShopProvider> {
        return ExpoShopHolder(v)
    }

    inner class ExpoShopHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ExpoShopProvider>(v) {

        override fun populate(data: ExpoShopProvider) {
            super.populate(data)

            itemView.apply {
                view_number.text = data.provideNumber()
                view_name.text = data.provideName()
                view_region.text = data.provideRegion()
                view_region.visibility = if (data.provideRegion().isBlank()) View.GONE else View.VISIBLE
                view_name.setTextColor(if (data.provideRegion().isBlank()) Color.RED else R.color.colorPrimaryText.asColor(context))
            }
        }
    }
}