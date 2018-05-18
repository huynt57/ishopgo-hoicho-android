package ishopgo.com.exhibition.ui.chat.local.group.addmember

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.content_checkable_contact_item.view.*

/**
 * Created by xuanhong on 4/17/18. HappyCoding!
 */
class MemberAdapter : BaseRecyclerViewAdapter<IMemberView>() {

    var listener: MemberListener? = null

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.content_checkable_contact_item
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<IMemberView> {
        return ContactHolder(v)
    }

    fun unselected(memer: IMemberView) {
        mData.forEachIndexed { index, iMemberView ->
            if (iMemberView.equals(memer) && checkedItems.get(index)) {
                checkedItems.delete(index)
                checkedItemCount--

                notifyItemChanged(index)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<IMemberView>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            val currentState = checkedItems[adapterPosition]
            if (!currentState)
                checkedItems.put(adapterPosition, !currentState)
            else
                checkedItems.delete(adapterPosition)
            checkedItemCount += if (!currentState) +1 else -1

            notifyItemChanged(adapterPosition)

            listener?.let {
                val member = getItem(adapterPosition)
                if (!currentState) {
                    it.onMemberSelected(checkedItemCount, member)
                } else
                    it.onMemberUnSelected(checkedItemCount, member)
            }

        }
    }

    inner class ContactHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<IMemberView>(v) {

        override fun populate(data: IMemberView) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.memberAvatar())
                        .apply(RequestOptions
                                .circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                view_name.text = data.memberName()
                view_phone.text = data.memberPhone()
                view_selection.isChecked = checkedItems.get(adapterPosition)
            }
        }
    }

    interface MemberListener {
        fun onMemberSelected(totalSelected: Int, member: IMemberView)
        fun onMemberUnSelected(totalSelected: Int, member: IMemberView)
    }
}