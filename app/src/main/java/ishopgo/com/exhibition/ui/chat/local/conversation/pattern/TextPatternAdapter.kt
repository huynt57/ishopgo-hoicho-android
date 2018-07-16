package ishopgo.com.exhibition.ui.chat.local.conversation.pattern

import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.content_text_pattern_item.view.*

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class TextPatternAdapter : ClickableAdapter<TextPattern>() {

    companion object {
        const val CODE_CLICK = 1
        const val CODE_EDIT = 2
        const val CODE_REMOVE = 3

    }

    override fun getChildLayoutResource(viewType: Int): Int = R.layout.content_text_pattern_item

    override fun createHolder(v: View, viewType: Int): ViewHolder<TextPattern> = PatternHolder(v)

    override fun onBindViewHolder(holder: ViewHolder<TextPattern>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.apply {
            setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                listener?.click(adapterPosition, getItem(adapterPosition), CODE_CLICK)
            }

            view_pattern_edit.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                listener?.click(adapterPosition, getItem(adapterPosition), CODE_EDIT)
            }

            view_pattern_remove.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                listener?.click(adapterPosition, getItem(adapterPosition), CODE_REMOVE)
            }
        }
    }

    class PatternHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<TextPattern>(view) {

        override fun populate(data: TextPattern) {
            super.populate(data)

            itemView.apply {
                view_pattern_text.text = data.content
            }

        }

    }
}