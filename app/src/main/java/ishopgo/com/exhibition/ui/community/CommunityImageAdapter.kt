package ishopgo.com.exhibition.ui.community

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.shop.info.SalePointProvider
import kotlinx.android.synthetic.main.item_image_only.view.*
import kotlinx.android.synthetic.main.item_sale_point.view.*

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityImageAdapter : BaseRecyclerViewAdapter<CommunityImageProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_image_only
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CommunityImageProvider> {
        return ImageHolder(v)
    }

    inner class ImageHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CommunityImageProvider>(v) {

        override fun populate(data: CommunityImageProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(this).load(data.url())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(image)
            }

        }
    }
}