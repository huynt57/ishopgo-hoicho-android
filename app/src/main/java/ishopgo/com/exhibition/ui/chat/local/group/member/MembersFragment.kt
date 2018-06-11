package ishopgo.com.exhibition.ui.chat.local.group.member

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.User
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.group.addmember.AddMemberActivity
import ishopgo.com.exhibition.ui.chat.local.info.MemberInfoViewModel
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/9/18. HappyCoding!
 */
class MembersFragment : BaseActionBarFragment() {

    private lateinit var info: ConversationInfo
    private val adapter = MemberAdapter()
    private lateinit var viewModel: MemberInfoViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setCustomTitle("Thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
        toolbar.rightButton(R.drawable.ic_person_add_green_24dp)
        toolbar.setRightButtonClickListener {
            if (::info.isInitialized) {
                val intent = Intent(it.context, AddMemberActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(info))
                startActivity(intent)
            }
        }

        activity?.let {
            val json = it.intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
            info = try {
                Toolbox.gson.fromJson(json, ConversationInfo::class.java)
            } catch (e: Exception) {
                throw RuntimeException("sai dinh dang")
            }

            adapter.listener = object : ClickableAdapter.BaseAdapterAction<User> {
                override fun click(position: Int, data: User, code: Int) {
                    val intent = Intent(view.context, MemberProfileActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    intent.putExtra(Const.TransferKey.EXTRA_ENABLE_CREATE_GROUP, false)
                    startActivity(intent)
                }

            }
            view_recyclerview.adapter = adapter
            view_recyclerview.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            if (info.listMember != null && info.listMember!!.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(info.listMember ?: mutableListOf())
        }

        swipe.setOnRefreshListener {
            info.conversationId?.let {
                viewModel.getConversationInfo(it)
            }

            swipe.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MemberInfoViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.info.observe(this, Observer { i ->
            i?.let {
                info = it

                if (info.listMember != null && info.listMember!!.isEmpty()) {
                    view_empty_result_notice.visibility = View.VISIBLE
                    view_empty_result_notice.text = "Nội dung trống"
                } else view_empty_result_notice.visibility = View.GONE

                adapter.replaceAll(info.listMember ?: mutableListOf())
            }
        })
    }

}