package ishopgo.com.exhibition.ui.main.visitors

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_visitor.view.*

class VisitorsAdapter : ClickableAdapter<VisitorsProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_visitor
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<VisitorsProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<VisitorsProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<VisitorsProvider>(v) {

        override fun populate(data: VisitorsProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideAvatar())
                        .apply(RequestOptions()
                                .circleCrop()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                view_name.text = data.provideName()
                view_phone.text = data.providePhone()
                view_region.text = data.provideRegion()
            }
        }
    }
}