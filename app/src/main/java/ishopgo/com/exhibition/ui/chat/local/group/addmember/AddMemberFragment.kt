package ishopgo.com.exhibition.ui.chat.local.group.addmember

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_local_chat_add_member.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/9/18. HappyCoding!
 */
class AddMemberFragment : BaseActionBarFragment() {

    private lateinit var viewModel: MemberViewModel
    private var adapter = MemberAdapter()
    private var selectedAdapter = SelectedMemberAdapter()
    private var searchName = ""
    private lateinit var info: ConversationInfo
    private val handler = Handler()
    private val searchRunnable = object : Runnable {
        override fun run() {
            handler.removeCallbacks(this)

            reloadData = true
            viewModel.loadContacts(0, searchName)
        }

    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_add_member
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setCustomTitle("Thêm thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        view_member_name.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()

                searchName = query
                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable, 300)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        view_recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                reloadData = false
                viewModel.loadContacts(totalItemsCount, searchName)
            }

        })
        adapter.listener = object : MemberAdapter.MemberListener {
            override fun onMemberSelected(totalSelected: Int, member: IMemberView) {
                selectedAdapter.addData(0, member)
                view_container_added_members.visibility = if (totalSelected > 0) View.VISIBLE else View.GONE
            }

            override fun onMemberUnSelected(totalSelected: Int, member: IMemberView) {
                selectedAdapter.remove(member)
                view_container_added_members.visibility = if (totalSelected > 0) View.VISIBLE else View.GONE
            }

        }

        view_added_members.adapter = selectedAdapter
        view_added_members.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        selectedAdapter.listener = object : ClickableAdapter.BaseAdapterAction<IMemberView> {
            override fun click(position: Int, data: IMemberView, code: Int) {
                adapter.unselected(data)
                selectedAdapter.remove(data)
            }

        }

        view_container_added_members.visibility = View.GONE

        view_submit.setOnClickListener {
            val selectedMembers = selectedAdapter.getData()

            if (::info.isInitialized)
                info.conversationId?.let { viewModel.addMember(it, selectedMembers) }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MemberViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.contacts.observe(this, Observer { c ->
            c?.let {
                if (reloadData) {
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE

                    adapter.replaceAll(it)
                    adapter.resetSelection()

                    selectedAdapter.clear()
                } else
                    adapter.addAll(it)
            }
        })
        viewModel.addOK.observe(this, Observer { add ->
            add?.let {
                toast("Thêm thành công")
                activity?.let {
                    it.setResult(Activity.RESULT_OK)
                    it.finish()
                }
            }
        })

        reloadData = true
        viewModel.loadContacts(0, searchName)

        activity?.let {
            val json = it.intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
            info = try {
                Toolbox.gson.fromJson(json, ConversationInfo::class.java)
            } catch (e: Exception) {
                throw RuntimeException("sai dinh dang")
            }
        }
    }

}