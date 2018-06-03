package ishopgo.com.exhibition.ui.chat.local.info.single

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.chat.local.config.notification.NotificationConfigFragment
import ishopgo.com.exhibition.ui.chat.local.group.CreateGroupActivity
import ishopgo.com.exhibition.ui.chat.local.profile.ProfileActivity
import kotlinx.android.synthetic.main.content_local_chat_single_member_info.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.layout_container_local_chat_avatar.*

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class SingleMemberInfoFragment : BaseActionBarFragment() {

    companion object {

        fun newInstance(info: ConversationInfo): SingleMemberInfoFragment {
            val fragment = SingleMemberInfoFragment()
            fragment.info = info

            return fragment

        }
    }

    private lateinit var info: ConversationInfo

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_single_member_info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Chi tiết hội thoại")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        Glide.with(view.context)
                .load(info.image)
                .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .into(view_avatar)
        view_name.text = info.title
        view_description.text = "Active"

        view_config_avatar.setOnClickListener {
            val intent = Intent(view.context, ProfileActivity::class.java)
            val memberId = info.listMember?.get(0)?.id
            memberId?.let {
                intent.putExtra(Const.TransferKey.EXTRA_ID, it)
            }
            startActivity(intent)
        }
        view_config_notification.setOnClickListener {
            val fragment = NotificationConfigFragment.newInstance(Bundle())
            fragment.show(childFragmentManager, "NotificationConfigFragment")
        }
        view_config_show_profile.setOnClickListener {
            val intent = Intent(view.context, ProfileActivity::class.java)
            val memberId = info.listMember?.get(0)?.id
            memberId?.let {
                intent.putExtra(Const.TransferKey.EXTRA_ID, it)
            }
            startActivity(intent)
        }
        view_config_create_room.setOnClickListener {
            val intent = Intent(view.context, CreateGroupActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, 0L)
            startActivity(intent)
        }
    }

}