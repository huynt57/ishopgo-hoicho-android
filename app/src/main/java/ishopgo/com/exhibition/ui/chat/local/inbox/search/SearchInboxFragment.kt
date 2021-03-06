package ishopgo.com.exhibition.ui.chat.local.inbox.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchInboxRequest
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.chat.local.inbox.InboxAdapter
import ishopgo.com.exhibition.ui.chat.local.inbox.InboxProvider
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.extensions.observable
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_home_search_inbox.*
import java.util.concurrent.TimeUnit

/**
 * Created by xuanhong on 5/24/18. HappyCoding!
 */
class SearchInboxFragment : BaseListFragment<List<InboxProvider>, InboxProvider>(), BackpressConsumable {
    override fun initLoading() {
        firstLoad()
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<InboxProvider>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Không tìm thấy"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
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
                        startActivity(intent)
                    }
                }
            }

        }
        return inboxAdapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<InboxProvider>> {
        return obtainViewModel(SearchInboxViewModel::class.java)
    }

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "SearchInboxFragment"
    }

    private var searchKey: String = ""
    private var disposables = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
    }

    override fun firstLoad() {
        super.firstLoad()

        val request = SearchInboxRequest()
        request.keyword = searchKey
        request.limit = Const.PAGE_LIMIT
        request.offset = 0

        if (viewModel is SearchInboxViewModel) {
            reloadData = true
            viewModel.loadData(request)
        }
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)

        val request = SearchInboxRequest()
        request.keyword = searchKey
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount

        if (viewModel is SearchInboxViewModel) {
            viewModel.loadData(request)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_search_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { v, event -> true }
        view_back.setOnClickListener {
            val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view_search_field.windowToken, InputMethodManager.SHOW_IMPLICIT)

            activity?.onBackPressed()
        }

        disposables.add(view_search_field.observable()
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    reloadData = true
                    searchKey = it
                    firstLoad()
                })

        view_search_field.post {
            val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.showSoftInput(view_search_field, InputMethodManager.SHOW_IMPLICIT)
        }

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

    }

}

