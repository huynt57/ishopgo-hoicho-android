package ishopgo.com.exhibition.ui.main.product

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExchangeDiaryImage
import ishopgo.com.exhibition.model.diary.DiaryImages
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_only.view.*

class ProductExchangeDiaryImageAdapter : ClickableAdapter<ExchangeDiaryImage>() {
    private var size = 0
    private var dataList = mutableListOf<ExchangeDiaryImage>()
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_image_only
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ExchangeDiaryImage> {
        return ImageHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ExchangeDiaryImage>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class ImageHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ExchangeDiaryImage>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: ExchangeDiaryImage) {
            super.populate(data)
//            if (adapterPosition <= 3) {
            itemView.apply {
                if (adapterPosition == 3)
                    if (size != 0) {
                        tv_number_image.text = "+$size"
                        tv_number_image.visibility = View.VISIBLE
                    }

                Glide.with(this).load(data.image)
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(image)
            }
        }
    }

    override fun replaceAll(data: List<ExchangeDiaryImage>) {
        super.replaceAll(data)
        dataList.addAll(data)

        if (dataList.size >= 4) {
            size = dataList.size - 4
        }

        for (i in dataList.indices)
            if (i >= 4) {
                dataList.removeAt(4)
            }

        clear()
        addAll(dataList)
        notifyDataSetChanged()
    }
}