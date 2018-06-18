package ishopgo.com.exhibition.ui.main.map.config

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_expo_config.view.*

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoConfigAdapter : ClickableAdapter<ExpoConfig>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_expo_config
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ExpoConfig> {
        return ExpoShopHolder(v, ConverterExpoConfig())
    }

    override fun onBindViewHolder(holder: ViewHolder<ExpoConfig>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class ExpoShopHolder(v: View, private val converter: ConverterExpoConfig) : BaseRecyclerViewAdapter.ViewHolder<ExpoConfig>(v) {

        override fun populate(data: ExpoConfig) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(converted.avatar())
                        .apply(RequestOptions().placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_avatar)
                view_name.text = converted.name()
                view_time.text = converted.time()
                view_address.text = converted.address()
            }
        }
    }
}