package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_product_comment.view.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductCommentAdapter : ClickableAdapter<ProductCommentProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_comment
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductCommentProvider> {
        return ProductHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductCommentProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.view_avatar.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            itemView.view_name.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class ProductHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ProductCommentProvider>(v) {

        override fun populate(data: ProductCommentProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                view_name.text = data.provideName()
                view_time.text = data.provideTime()
                view_content.text = data.provideContent()
            }
        }
    }


}