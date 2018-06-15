package ishopgo.com.exhibition.ui.chat.local.info.multi

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.chat.local.config.notification.NotificationConfigFragment
import ishopgo.com.exhibition.ui.chat.local.group.addmember.AddMemberActivity
import ishopgo.com.exhibition.ui.chat.local.group.member.MembersActivity
import ishopgo.com.exhibition.ui.chat.local.info.MemberInfoViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.content_local_chat_multi_member_info.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.layout_container_local_chat_avatar.*

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class MultiMemberInfoFragment : BaseActionBarFragment() {

    companion object {

        fun newInstance(info: ConversationInfo): MultiMemberInfoFragment {
            val fragment = MultiMemberInfoFragment()
            fragment.info = info

            return fragment

        }
    }

    private lateinit var info: ConversationInfo
    private lateinit var viewModel: MemberInfoViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_multi_member_info
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(MemberInfoViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.infoUpdated.observe(this, Observer { i ->
            i?.let {
                info.title = it.title

                view_name.text = info.title

                activity?.setResult(Activity.RESULT_OK)
            }
        })

        viewModel.info.observe(this, Observer { i ->
            i?.let {
                info = it
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Chi tiết hội thoại")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        Glide.with(view.context)
                .load(info.image)
                .apply(RequestOptions().circleCrop()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                ).into(view_avatar)
        view_name.text = info.title
        view_description.text = "Active"

        view_config_avatar.setOnClickListener {
            val intent = Intent(view.context, MembersActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(info))
            startActivityForResult(intent, Const.RequestCode.RC_SHOW_DETAIL)
        }
        view_config_change_name.setOnClickListener {
            context?.let {
                MaterialDialog.Builder(it)
                        .title("Đổi tên group")
                        .input("Tên mới", null, false) { dialog, input ->
                            info.conversationId?.let {
                                viewModel.changeName(it, input, null)
                            }
                        }
                        .positiveText("OK")
                        .negativeText("Huỷ")
                        .show()
            }
        }
        view_config_notification.setOnClickListener {
            val fragment = NotificationConfigFragment.newInstance(Bundle())
            fragment.show(childFragmentManager, "NotificationConfigFragment")
        }
        view_config_add_member.setOnClickListener {
            val intent = Intent(view.context, AddMemberActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(info))
            startActivityForResult(intent, Const.RequestCode.RC_ADD_NEW)
        }
        view_config_show_members.setOnClickListener {
            val intent = Intent(view.context, MembersActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(info))
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        info.conversationId?.let {
            viewModel.getConversationInfo(it)
        }
    }
}