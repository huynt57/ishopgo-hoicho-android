package ishopgo.com.exhibition.ui.main.account

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_account_menu_child.view.*
import kotlinx.android.synthetic.main.item_account_menu_parent.view.*

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class AccountMenuAdapter : ClickableAdapter<AccountMenuProvider>() {

    companion object {
        const val TYPE_PARENT = 1
        const val TYPE_CHILD = 2

        const val CODE_CLICK_CHILD = 1
        const val CODE_CLICK_PARENT = 2
    }

    /**
     * Running state of which positions are currently checked
     */
    private var mExpanding = mutableListOf<AccountMenuProvider>()

    override fun getChildLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TYPE_PARENT -> {
                R.layout.item_account_menu_parent
            }
            else -> {
                R.layout.item_account_menu_child
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val isParent = getItem(position).provideIsParent()
        return if (isParent) TYPE_PARENT else TYPE_CHILD
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<AccountMenuProvider> {
        return when (viewType) {
            TYPE_PARENT -> {
                ParentHolder(v)
            }
            else -> {
                ChildHolder(v)
            }
        }

    }

    override fun replaceAll(data: List<AccountMenuProvider>) {
        mExpanding.clear()

        super.replaceAll(data)
    }

    override fun onBindViewHolder(holder: ViewHolder<AccountMenuProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is ParentHolder) {
            holder.itemView.view_parent_expand.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                if (mExpanding.any { it == item }) {
                    // collapse parent, remove childs
                    mData.subList(adapterPosition + 1, adapterPosition + 1 + item.provideChilds().size).clear()

                    notifyItemRangeRemoved(adapterPosition + 1, item.provideChilds().size)

                    mExpanding.remove(item)
                } else {
                    // expand childs
                    mData.addAll(adapterPosition + 1, item.provideChilds())

                    notifyItemRangeInserted(adapterPosition + 1, item.provideChilds().size)

                    mExpanding.add(item)
                }

                notifyItemChanged(adapterPosition)
            }

            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                listener?.click(adapterPosition, item, CODE_CLICK_PARENT)
            }

        }

        if (holder is ChildHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                listener?.click(adapterPosition, item, CODE_CLICK_CHILD)
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
                                .placeholder(R.drawable.ic_finger_highlight_24dp)
                                .error(R.drawable.ic_finger_highlight_24dp))
                        .into(view_parent_icon)

                view_parent_text.text = data.provideName()

                if (data.provideChilds().isEmpty()) {
                    view_parent_expand.setImageResource(0)
                } else {
                    if (mExpanding.any { it == data })
                        view_parent_expand.setImageResource(R.drawable.ic_expand_less_neutral_24dp)
                    else
                        view_parent_expand.setImageResource(R.drawable.ic_expand_more_neutral_24dp)
                }
            }
        }
    }

    inner class ChildHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<AccountMenuProvider>(v) {

        override fun populate(data: AccountMenuProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideIcon())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_finger_highlight_24dp)
                                .error(R.drawable.ic_finger_highlight_24dp))
                        .into(view_child_icon)

                view_child_text.text = data.provideName()
            }
        }
    }


}