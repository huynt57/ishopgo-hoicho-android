package ishopgo.com.exhibition.ui.main.home.search

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.home.search.product.ProductResultsFragment
import ishopgo.com.exhibition.ui.main.home.search.shop.ShopResultsFragment
import kotlinx.android.synthetic.main.fragment_home_search.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class SearchFragment : BaseFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var searchViewModel: SearchViewModel
    private var searchKey: String = ""
    private val searchRunnable = Runnable { Log.d(TAG, "start searching: $searchKey"); searchViewModel.search(searchKey) }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { v, event -> true }
        view_back.setOnClickListener {
            viewModel.disableSearch()
        }

        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_pager.adapter = ResultAdapter(childFragmentManager)

        view_search_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "afterTextChanged: s = [${s}]")
                searchKey = s.toString()
                view_search_field.handler.removeCallbacks(searchRunnable)
                view_search_field.handler.postDelayed(searchRunnable, 500)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

//        val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.showSoftInput(view_search_field, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MainViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        searchViewModel = obtainViewModel(SearchViewModel::class.java, true)
        searchViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    inner class ResultAdapter(f: FragmentManager) : FragmentStatePagerAdapter(f) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ProductResultsFragment()
                }
                1 -> {
                    ShopResultsFragment()
                }
                else -> {
                    Fragment()
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }

    }

}