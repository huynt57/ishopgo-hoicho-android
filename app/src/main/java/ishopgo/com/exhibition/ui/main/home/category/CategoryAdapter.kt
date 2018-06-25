package ishopgo.com.exhibition.ui.main.home.category

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_category_child.view.*
import kotlinx.android.synthetic.main.item_category_parent.view.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class CategoryAdapter : ClickableAdapter<Category>() {

    companion object {
        const val TYPE_PARENT = 1
        const val TYPE_CHILD = 2

        const val CODE_CLICK_CHILD = 1
        const val CODE_CLICK_PARENT = 2
    }

    /**
     * Running state of which positions are currently checked
     */
    private var mExpanding = mutableListOf<Category>()

    override fun getChildLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TYPE_PARENT -> {
                R.layout.item_category_parent
            }
            else -> {
                R.layout.item_category_child
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val isParent = getItem(position).level == 1
        return if (isParent) TYPE_PARENT else TYPE_CHILD
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Category> {
        return when (viewType) {
            TYPE_PARENT -> {
                ParentHolder(v, CategoryConverter())
            }
            else -> {
                ChildHolder(v, CategoryConverter())
            }
        }

    }

    override fun replaceAll(data: List<Category>) {
        mExpanding.clear()

        super.replaceAll(data)
    }

    override fun onBindViewHolder(holder: ViewHolder<Category>, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is ParentHolder) {
            holder.itemView.view_parent_expand.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                if (mExpanding.any { it == item }) {
                    if (item.subs != null && item.subs!!.isNotEmpty()) {

                        // collapse parent, remove childs
                        mData.subList(adapterPosition + 1, adapterPosition + 1 + item.subs!!.size).clear()

                        notifyItemRangeRemoved(adapterPosition + 1, item.subs!!.size)

                        mExpanding.remove(item)
                    }
                } else {
                    if (item.subs != null && item.subs!!.isNotEmpty()) {
                        // expand childs
                        mData.addAll(adapterPosition + 1, item.subs!!)

                        notifyItemRangeInserted(adapterPosition + 1, item.subs!!.size)

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

    inner class ParentHolder(v: View, private val converter: Converter<Category, CategoryProvider>) : BaseRecyclerViewAdapter.ViewHolder<Category>(v) {

        override fun populate(data: Category) {
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

    inner class ChildHolder(v: View, private val converter: Converter<Category, CategoryProvider>) : BaseRecyclerViewAdapter.ViewHolder<Category>(v) {

        override fun populate(data: Category) {
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

    interface CategoryProvider {

        fun provideIcon(): String

        fun provideName(): String

        fun provideChilds(): List<Category>

        fun provideIsParent(): Boolean

        fun provideLevel(): Int

        fun provideCount(): Int
    }

    class CategoryConverter : Converter<Category, CategoryProvider> {

        override fun convert(from: Category): CategoryProvider {
            return object : CategoryProvider {
                override fun provideCount(): Int {
                    return from.count ?: 0
                }

                override fun provideLevel(): Int {
                    return from.level
                }

                override fun provideIcon(): String {
                    return from.image ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: "Tên danh mục"
                }

                override fun provideChilds(): List<Category> {
                    return from.subs ?: mutableListOf()
                }

                override fun provideIsParent(): Boolean {
                    return from.level == 1
                }
            }
        }
    }

}