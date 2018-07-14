package ishopgo.com.exhibition.ui.chat.local.inbox

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.main.MainViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_inbox.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class InboxFragment : BaseListFragment<List<InboxProvider>, InboxProvider>() {
    override fun initLoading() {
        firstLoad()
    }

    private lateinit var mainViewModel: MainViewModel


    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<InboxProvider>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

        view_search.setOnClickListener {
            mainViewModel.enableSearchInbox()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        mainViewModel.newMessage.observe(this, Observer { m ->
            m?.let {
                // update current conversation match with new incoming message
                val conversationId = m.idConversation
                if (adapter is InboxAdapter) {
                    val conversationPosition = (adapter as InboxAdapter).indexOf(conversationId)
                    if (conversationPosition != -1) {
                        val item = adapter.getItem(conversationPosition)
                        if (item is LocalConversationItem) {
                            item.lastMsgTime = m.apiTime
                            item.content = m.apiContent
                            item.unreadCount = 0 // mark unread
                            adapter.notifyItemChanged(conversationPosition)
                        }
                    }
                    else {
                        // new conversation, reload
                        firstLoad()
                    }
                }

            }
        })

    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<InboxProvider> {
        val inboxAdapter = InboxAdapter()
        inboxAdapter.listener = object : ClickableAdapter.BaseAdapterAction<InboxProvider> {
            override fun click(position: Int, data: InboxProvider, code: Int) {
                if (data is LocalConversationItem) {
                    context?.let {
                        val intent = Intent(it, ConversationActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, data.idConversions)
                        intent.putExtra(Const.TransferKey.EXTRA_TITLE, data.name)
                        startActivityForResult(intent, Const.RequestCode.RC_SHOW_DETAIL)
                    }
                }
            }

        }
        return inboxAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("InboxFragment", "onActivityResult: requestCode = [${requestCode}], resultCode = [${resultCode}], data = [${data}]")
        super.onActivityResult(requestCode, resultCode, data)

        firstLoad()
    }

    override fun obtainViewModel(): BaseListViewModel<List<InboxProvider>> {
        return obtainViewModel(InboxViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = LoadMoreRequest()
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = LoadMoreRequest()
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

}