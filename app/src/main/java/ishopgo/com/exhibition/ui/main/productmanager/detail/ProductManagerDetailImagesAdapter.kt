package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_horizontal.view.*

class ProductManagerDetailImagesAdapter : ClickableAdapter<String>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_image_horizontal
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<String> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<String>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<String>(v) {

        override fun populate(data: String) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data)
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_image)
                btn_delete.visibility = View.GONE

            }
        }

    }
}