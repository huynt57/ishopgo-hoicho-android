package ishopgo.com.exhibition.ui.chat.local.group.member

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.User
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.chat_member_item.view.*

/**
 * Created by xuanhong on 4/16/18. HappyCoding!
 */
class MemberAdapter : ClickableAdapter<User>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.chat_member_item
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<User> {
        return MemberHolder(v)
    }

    inner class MemberHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<User>(v) {

        override fun populate(data: User) {
            super.populate(data)

            itemView.apply {
                Glide.with(context).load(data.image).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.avatar_placeholder)).into(view_avatar)

                view_name.text = data.name
//                view_phone.text = data.phone
//                view_phone.visibility = if (data.phone.isBlank()) View.GONE else View.VISIBLE
            }
        }
    }


}