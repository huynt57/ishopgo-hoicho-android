package ishopgo.com.exhibition.ui.main.home.category.product

import android.graphics.Color
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_category_child_only_text.view.*

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class CategoryChildAdapter : ClickableAdapter<Category>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_category_child_only_text
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Category> {
        return ChildHolder(v, CategoryConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Category>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class ChildHolder(v: View, private val converter: Converter<Category, CategoryProvider>) : BaseRecyclerViewAdapter.ViewHolder<Category>(v) {

        override fun populate(data: Category) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                view_name.text = convert.provideName()
                val drawable = AppCompatResources.getDrawable(context, R.drawable.bg_rounded_button)
                drawable?.let {
                    DrawableCompat.setTint(it, Color.parseColor(Const.colors[adapterPosition % Const.colors.size]))
                    background = it
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