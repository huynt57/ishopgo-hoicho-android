package ishopgo.com.exhibition.ui.main.home.category

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_category_stage1.view.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class CategoryStage1Adapter(private var itemWidthRatio: Float = -1f) : ClickableAdapter<Category>() {

    private var screenWidth: Int = UserDataManager.displayWidth

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_category_stage1
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Category> {
        val categoryHolder = CategoryHolder(v, CategoryConverter())
        val layoutParams = categoryHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()

        return categoryHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<Category>, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is CategoryHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                listener?.click(adapterPosition, item)
            }
        }
    }

    inner class CategoryHolder(v: View, private val converter: Converter<Category, CategoryProvider>) : BaseRecyclerViewAdapter.ViewHolder<Category>(v) {

        override fun populate(data: Category) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideIcon())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_image)

                view_text.text = convert.provideName()
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
                    return from.name ?: "unknown"
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