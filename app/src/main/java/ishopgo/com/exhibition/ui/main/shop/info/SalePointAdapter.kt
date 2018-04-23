package ishopgo.com.exhibition.ui.main.shop.info

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_sale_point.view.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class SalePointAdapter : BaseRecyclerViewAdapter<SalePointProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SalePointProvider> {
        return SalePointHolder(v)
    }

    inner class SalePointHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<SalePointProvider>(v) {

        override fun populate(data: SalePointProvider) {
            super.populate(data)

            itemView.apply {
                view_name.text = data.provideName()
                view_distance.text = data.provdeDistance()
                view_region.text = data.provideRegion()
            }

        }
    }

}