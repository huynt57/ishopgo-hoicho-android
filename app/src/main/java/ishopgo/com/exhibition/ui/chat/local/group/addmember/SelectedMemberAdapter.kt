package ishopgo.com.exhibition.ui.chat.local.group.addmember

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.container_selected_member_item.view.*

/**
 * Created by xuanhong on 4/17/18. HappyCoding!
 */
class SelectedMemberAdapter : ClickableAdapter<IMemberView>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.container_selected_member_item
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<IMemberView> {
        return MemberHolder(v)
    }

    inner class MemberHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<IMemberView>(v) {

        override fun populate(data: IMemberView) {
            super.populate(data)

            itemView.apply {
                Glide.with(context).load(data.memberAvatar())
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder))
                        .into(view_avatar)
            }
        }
    }
}