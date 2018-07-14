package ishopgo.com.exhibition.ui.chat.local.contact.search

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.SearchContactRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.contact.ContactAdapter
import ishopgo.com.exhibition.ui.chat.local.contact.ContactProvider
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.MainViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_home_search_contact.*

/**
 * Created by xuanhong on 5/24/18. HappyCoding!
 */
class SearchContactFragment : BaseListFragment<List<ContactProvider>, ContactProvider>(), BackpressConsumable {
    override fun initLoading() {
        firstLoad()
    }

    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<ContactProvider>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<ContactProvider> {
        val contactAdapter = ContactAdapter()
        contactAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ContactProvider> {
            override fun click(position: Int, data: ContactProvider, code: Int) {
                if (viewModel is SearchContactViewModel) {
                    val request = CreateConversationRequest()
                    request.type = 1
                    val members = mutableListOf<Long>()
                    members.add(UserDataManager.currentUserId)
                    if (data is IdentityData) {
                        members.add(data.id)
                    }
                    request.member = members
                    (viewModel as SearchContactViewModel).createConversation(request)
                }
            }

        }
        return contactAdapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<ContactProvider>> {
        return obtainViewModel(SearchContactViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is SearchContactViewModel) {
            (viewModel as SearchContactViewModel).conversation.observe(this, Observer { c ->
                c?.let {
                    val conv = LocalConversationItem()
                    conv.idConversions = c.id ?: ""
                    conv.name = c.name ?: ""

                    context?.let {
                        val intent = Intent(it, ConversationActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conv.idConversions)
                        intent.putExtra(Const.TransferKey.EXTRA_TITLE, conv.name)
                        startActivity(intent)
                    }
                }
            })
        }

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "SearchContactFragment"
    }

    private var searchKey: String = ""
    private val searchRunnable = Runnable {
        Log.d(TAG, "start searching: $searchKey")

        firstLoad()
    }

    override fun firstLoad() {
        super.firstLoad()

        val request = SearchContactRequest()
        request.keyword = searchKey
        request.limit = Const.PAGE_LIMIT
        request.offset = 0

        if (viewModel is SearchContactViewModel) {
            reloadData = true
            viewModel.loadData(request)
        }
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)

        val request = SearchContactRequest()
        request.keyword = searchKey
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount

        if (viewModel is SearchContactViewModel) {
            viewModel.loadData(request)
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_search_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { v, event -> true }
        view_back.setOnClickListener {
            val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view_search_field.windowToken, InputMethodManager.SHOW_IMPLICIT)

            activity?.onBackPressed()
        }

        view_search_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchKey = s.toString()
                view_search_field.handler.removeCallbacks(searchRunnable)
                view_search_field.handler.postDelayed(searchRunnable, 500)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        view_search_field.post {
            val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.showSoftInput(view_search_field, InputMethodManager.SHOW_IMPLICIT)
        }

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

}

