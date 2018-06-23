package ishopgo.com.exhibition.ui.main.account

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.AccountMenuItem
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_account_menu_child.view.*
import kotlinx.android.synthetic.main.item_account_menu_parent.view.*

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class AccountMenuAdapter : ClickableAdapter<AccountMenuItem>() {

    companion object {
        const val TYPE_PARENT = 1
        const val TYPE_CHILD = 2

        const val CODE_CLICK_CHILD = 1
        const val CODE_CLICK_PARENT = 2
    }

    /**
     * Running state of which positions are currently checked
     */
    private var mExpanding = mutableListOf<AccountMenuItem>()

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
        val isParent = getItem(position).isParent
        return if (isParent) TYPE_PARENT else TYPE_CHILD
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<AccountMenuItem> {
        return when (viewType) {
            TYPE_PARENT -> {
                ParentHolder(v, AccountMenuConverter())
            }
            else -> {
                ChildHolder(v, AccountMenuConverter())
            }
        }

    }

    override fun replaceAll(data: List<AccountMenuItem>) {
        mExpanding.clear()

        super.replaceAll(data)
    }

    override fun onBindViewHolder(holder: ViewHolder<AccountMenuItem>, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is ParentHolder) {
            holder.itemView.view_parent_expand.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                if (mExpanding.any { it == item }) {
                    item.childs?.let {
                        // collapse parent, remove childs
                        mData.subList(adapterPosition + 1, adapterPosition + 1 + item.childs.size).clear()

                        notifyItemRangeRemoved(adapterPosition + 1, item.childs.size)

                        mExpanding.remove(item)
                    }
                } else {
                    item.childs?.let {
                        // expand childs
                        mData.addAll(adapterPosition + 1, item.childs)

                        notifyItemRangeInserted(adapterPosition + 1, item.childs.size)

                        mExpanding.add(item)
                    }
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

    inner class ParentHolder(v: View, private val converter: Converter<AccountMenuItem, AccountMenuProvider>) : BaseRecyclerViewAdapter.ViewHolder<AccountMenuItem>(v) {

        override fun populate(data: AccountMenuItem) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideIcon())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_finger_highlight_24dp)
                                .error(R.drawable.ic_finger_highlight_24dp))
                        .into(view_parent_icon)

                view_parent_text.text = convert.provideName()

                if (convert.provideChilds().isEmpty()) {
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

    inner class ChildHolder(v: View, private val converter: Converter<AccountMenuItem, AccountMenuProvider>) : BaseRecyclerViewAdapter.ViewHolder<AccountMenuItem>(v) {

        override fun populate(data: AccountMenuItem) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideIcon())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_finger_highlight_24dp)
                                .error(R.drawable.ic_finger_highlight_24dp))
                        .into(view_child_icon)

                view_child_text.text = convert.provideName()
            }
        }
    }

    interface AccountMenuProvider {

        fun provideIcon(): Int

        fun provideName(): String

        fun provideChilds(): List<AccountMenuItem>

        fun provideIsParent(): Boolean

        fun provideAction(): Int

        fun provideTextColor(): Int

    }

    class AccountMenuConverter : Converter<AccountMenuItem, AccountMenuProvider> {

        override fun convert(from: AccountMenuItem): AccountMenuProvider {
            return object : AccountMenuProvider {
                override fun provideTextColor(): Int {
                    return from.textColor
                }

                override fun provideIcon(): Int {
                    return from.icon
                }

                override fun provideName(): String {
                    return from.name
                }

                override fun provideChilds(): List<AccountMenuItem> {
                    return from.childs ?: listOf()
                }

                override fun provideIsParent(): Boolean {
                    return from.isParent
                }

                override fun provideAction(): Int {
                    return from.action
                }
            }
        }
    }
}