package ishopgo.com.exhibition.ui.main.home.category

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_category_stage1.view.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class CategoryStage1Adapter(private var itemWidthRatio: Float = -1f) : ClickableAdapter<CategoryProvider>() {

    private var screenWidth: Int = UserDataManager.displayWidth

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_category_stage1
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CategoryProvider> {
        val categoryHolder = CategoryHolder(v)
        val layoutParams = categoryHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()

        return categoryHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<CategoryProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is CategoryHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val item = getItem(adapterPosition)

                listener?.click(adapterPosition, item)
            }
        }
    }

    inner class CategoryHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CategoryProvider>(v) {

        override fun populate(data: CategoryProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideIcon())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_image)

                view_text.text = data.provideName()
            }
        }
    }


}