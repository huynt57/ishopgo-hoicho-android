package ishopgo.com.exhibition.ui.chat.local.inbox

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_inbox.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class InboxFragment : BaseListFragment<List<InboxProvider>, InboxProvider>() {

    private lateinit var mainViewModel: MainViewModel

    override fun populateData(data: List<InboxProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else
            adapter.addAll(data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_search.setOnClickListener {
            mainViewModel.enableSearchInbox()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<InboxProvider> {
        val inboxAdapter = InboxAdapter()
        inboxAdapter.listener = object: ClickableAdapter.BaseAdapterAction<InboxProvider> {
            override fun click(position: Int, data: InboxProvider, code: Int) {
                val notifyIntent = Intent(context, ConversationActivity::class.java)
                notifyIntent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(notifyIntent)
            }

        }
        return inboxAdapter
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