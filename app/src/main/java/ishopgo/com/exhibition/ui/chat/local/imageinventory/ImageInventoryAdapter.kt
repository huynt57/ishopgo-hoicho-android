package ishopgo.com.exhibition.ui.chat.local.imageinventory

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ImageInventory
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_inventory.view.*

/**
 * Created by xuanhong on 12/31/17. HappyCoding!
 */
class ImageInventoryAdapter : BaseRecyclerViewAdapter<ImageInventory>() {

    val selectedImages = mutableListOf<ImageInventory>()

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_image_inventory
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ImageInventory> {
        return ImageInventoryHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ImageInventory>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener {
                val item = getItem(adapterPosition)

                it.isSelected = !it.isSelected
                if (it.isSelected)
                    selectedImages.add(item)
                else
                    selectedImages.remove(item)

                checkedItemCount += if (it.isSelected) 1 else -1
            }

            val item = getItem(adapterPosition)
            itemView.isSelected = hasSelectedImages(item.id)
        }
    }

    private fun hasSelectedImages(id: Long): Boolean {
        return selectedImages.firstOrNull { it.id == id } != null
    }

    class ImageInventoryHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ImageInventory>(v) {

        override fun populate(data: ImageInventory) {
            super.populate(data)

            itemView.apply {
                data.let {
                    Glide.with(context).load(it.link).apply(RequestOptions().placeholder(R.drawable.image_placeholder).centerCrop()).into(view_image)
                }
            }

        }

    }

}