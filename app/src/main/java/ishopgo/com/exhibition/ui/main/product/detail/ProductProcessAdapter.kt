package ishopgo.com.exhibition.ui.main.product.detail

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductProcess
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_source_description.view.*

class ProductProcessAdapter: ClickableAdapter<ProductProcess>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_source_description
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductProcess> {
        return ProcessHolder(v, ConverterProductProcess())
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductProcess>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    class ProcessHolder(v: View, private val converter: Converter<ProductProcess, ProcessProvider>): BaseRecyclerViewAdapter.ViewHolder<ProductProcess>(v) {

        override fun populate(data: ProductProcess) {
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

    class ConverterProductProcess: Converter<ProductProcess, ProcessProvider> {
        override fun convert(from: ProductProcess): ProcessProvider {
            return object: ProcessProvider {
                override fun processName(): CharSequence {
                    return from.title ?: ""
                }
            }
        }
    }

}