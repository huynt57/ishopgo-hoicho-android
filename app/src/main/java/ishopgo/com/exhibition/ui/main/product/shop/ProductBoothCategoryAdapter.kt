package ishopgo.com.exhibition.ui.main.product.shop

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter

class ProductBoothCategoryAdapter : ClickableAdapter<Category>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Category> {
        return RegionHodel(v, CategoryConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Category>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View, private val converter: Converter<Category, CategoryProvider>) : BaseRecyclerViewAdapter.ViewHolder<Category>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Category) {
            super.populate(data)
            val convert = converter.convert(data)
            val textView = itemView as TextView
            if (convert.provideName() == "Tất cả danh mục") {
                textView.text = convert.provideName()
            } else
                textView.text = "${convert.provideName()} (${convert.provideCount()})"
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