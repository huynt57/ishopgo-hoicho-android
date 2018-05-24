package ishopgo.com.exhibition.ui.chat.local.contact.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchInboxRequest
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.contact.ContactAdapter
import ishopgo.com.exhibition.ui.chat.local.contact.ContactProvider
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_home_search_contact.*

/**
 * Created by xuanhong on 5/24/18. HappyCoding!
 */
class SearchContactFragment : BaseListFragment<List<ContactProvider>, ContactProvider>(), BackpressConsumable {
    override fun populateData(data: List<ContactProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        }
        else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ContactProvider> {
        return ContactAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<ContactProvider>> {
        return obtainViewModel(SearchContactViewModel::class.java)
    }

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "SearchContactFragment"
    }

    private var searchKey: String = ""
    private val searchRunnable = Runnable {
        Log.d(TAG, "start searching: $searchKey");

        val request = SearchInboxRequest()
        request.keyword = searchKey

        if (viewModel is SearchContactViewModel) {
            reloadData = true
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

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

//        val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.showSoftInput(view_search_field, InputMethodManager.SHOW_IMPLICIT)
    }

}

