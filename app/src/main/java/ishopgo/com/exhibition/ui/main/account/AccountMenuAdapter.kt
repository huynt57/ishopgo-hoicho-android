package ishopgo.com.exhibition.ui.main.account

import android.support.v4.content.res.ResourcesCompat
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_account_menu.view.*

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class AccountMenuAdapter : ClickableAdapter<AccountMenuProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_account_menu
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<AccountMenuProvider> {
        return ParentHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<AccountMenuProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is ParentHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                listener?.click(adapterPosition, item, 0)
            }

        }

    }

    inner class ParentHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<AccountMenuProvider>(v) {

        override fun populate(data: AccountMenuProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideIcon())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_finger)
                                .error(R.drawable.ic_finger))
                        .into(view_parent_icon)

                view_parent_text.text = data.provideName()
                view_parent_text.setTextColor(ResourcesCompat.getColor(resources, data.provideTextColor(), null))

            }
        }
    }

}