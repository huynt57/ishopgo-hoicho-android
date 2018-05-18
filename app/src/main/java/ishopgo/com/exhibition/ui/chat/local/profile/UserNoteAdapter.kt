package ishopgo.com.exhibition.ui.chat.local.profile

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.UserNoteItem
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.user_note_item.view.*

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class UserNoteAdapter : BaseRecyclerViewAdapter<UserNoteItem>() {

    var listener: NoteInterface? = null

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.user_note_item
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<UserNoteItem> {
        return NoteHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<UserNoteItem>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.apply {
            view_ichat_note_delete.setOnClickListener {
                listener?.deleteNote(getItem(holder.adapterPosition))
            }
        }
    }

    inner class NoteHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<UserNoteItem>(v) {

        override fun populate(data: UserNoteItem) {
            super.populate(data)

            itemView.apply {
                view_ichat_note_content.text = data.content
                view_ichat_note_time.text = data.time
            }

        }
    }

    interface NoteInterface {
        fun deleteNote(note: UserNoteItem)
    }

}