package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.CertImages
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_only.view.*

class CertImagesAdapter : ClickableAdapter<CertImages>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_image_only
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CertImages> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CertImages>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is Holder) {
            holder.apply {
                itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            }

        }

    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CertImages>(v) {

        override fun populate(data: CertImages) {
            super.populate(data)

            itemView.apply {
                Glide.with(this)
                        .load(data.image)
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(image)
            }
        }

    }
}