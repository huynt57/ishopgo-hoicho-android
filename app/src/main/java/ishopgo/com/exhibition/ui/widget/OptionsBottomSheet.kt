package ishopgo.com.exhibition.ui.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.bottom_sheet_options.*
import kotlinx.android.synthetic.main.item_bottom_sheet_option.view.*

class OptionsBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private val TAG = "OptionsBottomSheetBuilder"
        const val CODE_CANCEL = Int.MAX_VALUE
    }

    class Builder {
        private var items: MutableList<Option> = mutableListOf()
        private var listener: BottomSheetOptionListener? = null

        fun options(items: List<Option>): Builder {
            this.items.clear()
            this.items.addAll(items)
            return this
        }

        fun listener(l: BottomSheetOptionListener): Builder {
            this.listener = l
            return this
        }

        fun build(): OptionsBottomSheet {
            val f = OptionsBottomSheet()
            f.listener = listener
            f.adapter.replaceAll(items)

            return f
        }
    }


    private var listener: BottomSheetOptionListener? = null
    private val adapter = OptionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option_cancel.setOnClickListener {
            dismiss()
            listener?.click(CODE_CANCEL)
        }
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Option> {
            override fun click(position: Int, data: Option, code: Int) {
                dismiss()
                listener?.click(position)
            }

        }
        view_options.adapter = adapter
        view_options.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_options.layoutManager = layoutManager
    }

    data class Option(var text: CharSequence = "", var icon: Int = 0)

    class OptionAdapter : ClickableAdapter<Option>() {
        override fun getChildLayoutResource(viewType: Int): Int {
            return R.layout.item_bottom_sheet_option
        }

        override fun createHolder(v: View, viewType: Int): ViewHolder<Option> {
            return OptionHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder<Option>, position: Int) {
            super.onBindViewHolder(holder, position)

            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }

        class OptionHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<Option>(v) {
            override fun populate(data: Option) {
                super.populate(data)

                itemView.apply {
                    Glide.with(context)
                            .load(data.icon)
                            .into(view_icon)

                    view_text.text = data.text
                }
            }
        }

    }
}