package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_home_search.*

class SearchTabFragment : BaseSearchActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_home_search
    }

    override fun triggerSearch(key: String) {
        searchKey = key
        searchViewModel.search(searchKey)
    }

    override fun searchReset() {
        searchKey = ""
        searchViewModel.search(searchKey)
    }

    override fun dismissSearch() {
        activity?.onBackPressed()
    }

    private lateinit var searchViewModel: SearchProductManagerViewModel
    private var searchKey: String = ""

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_pager.offscreenPageLimit = 4
        view_pager.adapter = ResultAdapter(childFragmentManager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel = obtainViewModel(SearchProductManagerViewModel::class.java, true)
        searchViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 4) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    SearchProductManagerFragment()
                }
                1 -> {
                    SearchPMBoothFragment()
                }
                2 -> {
                    SearchPMSalePointFragment()
                }
                3 -> {
                    SearchPMProviderFragment()
                }
                else -> {
                    Fragment()
                }
            }
        }

    }

    companion object {
        const val TAG = "SearchTabFragment"

        fun newInstance(arg: Bundle): SearchTabFragment {
            val f = SearchTabFragment()
            f.arguments = arg
            return f
        }
    }
}