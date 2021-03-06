package ishopgo.com.exhibition.ui.main.product.shop

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_category_booth_parent.view.*

class ProductBoothCategoryAdapter : ClickableAdapter<Category>() {
    val childAdapter = ProductBoothCategoryChildAdapter()

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_category_booth_parent
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Category> {
        return RegionHodel(v, CategoryConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Category>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.tv_category_parent.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }

            itemView.view_parent_expand.setOnClickListener {
                if (itemView.rv_category_child.visibility == View.GONE) {
                    itemView.rv_category_child.visibility = View.VISIBLE
                    itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_less_neutral_24dp)
                } else {
                    itemView.rv_category_child.visibility = View.GONE
                    itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_more_neutral_24dp)
                }
            }
        }
    }

    internal inner class RegionHodel(itemView: View, private val converter: Converter<Category, CategoryProvider>) : BaseRecyclerViewAdapter.ViewHolder<Category>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Category) {
            super.populate(data)
            itemView.apply {
                val convert = converter.convert(data)
                if (convert.provideName() == "Tất cả danh mục") {
                    tv_category_parent.text = convert.provideName()
                } else
                    tv_category_parent.text = "${convert.provideName()} (${convert.provideCount()})"

                rv_category_child.visibility = View.GONE

                if (convert.provideChilds().isNotEmpty()) {
                    childAdapter.replaceAll(convert.provideChilds())
                    rv_category_child.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rv_category_child.isNestedScrollingEnabled = false
                    rv_category_child.setHasFixedSize(false)
                    rv_category_child.adapter = childAdapter
                    (childAdapter as ClickableAdapter<Category>).listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                        override fun click(position: Int, data: Category, code: Int) {
                            listener?.click(position, data)
                        }
                    }

                    if (rv_category_child.visibility == View.GONE) {
                        view_parent_expand.setImageResource(R.drawable.ic_expand_more_neutral_24dp)
                    } else {
                        view_parent_expand.setImageResource(R.drawable.ic_expand_less_neutral_24dp)
                    }
                }

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