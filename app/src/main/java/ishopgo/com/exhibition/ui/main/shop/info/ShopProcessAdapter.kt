package ishopgo.com.exhibition.ui.main.shop.info

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ShopProcess
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_source_description.view.*

class ShopProcessAdapter: ClickableAdapter<ShopProcess>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_source_description
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ShopProcess> {
        return ProcessHolder(v, ConverterShopProcess())
    }

    override fun onBindViewHolder(holder: ViewHolder<ShopProcess>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    class ProcessHolder(v: View, private val converter: Converter<ShopProcess, ProcessProvider>): BaseRecyclerViewAdapter.ViewHolder<ShopProcess>(v) {

        override fun populate(data: ShopProcess) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                process_name.text = converted.processName()
            }
        }
    }

    interface ProcessProvider {
        fun processName(): CharSequence
    }

    class ConverterShopProcess: Converter<ShopProcess, ProcessProvider> {
        override fun convert(from: ShopProcess): ProcessProvider {
            return object: ProcessProvider {
                override fun processName(): CharSequence {
                    return from.title ?: ""
                }
            }
        }
    }

}