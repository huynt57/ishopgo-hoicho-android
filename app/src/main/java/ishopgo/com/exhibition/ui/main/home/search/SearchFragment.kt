package ishopgo.com.exhibition.ui.main.home.search

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
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.home.search.product.ProductResultsFragment
import ishopgo.com.exhibition.ui.main.home.search.brands.BrandsResultFragment
import ishopgo.com.exhibition.ui.main.home.search.sale_point.SalePointResultFragment
import ishopgo.com.exhibition.ui.main.home.search.shop.ShopResultsFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_home_search.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class SearchFragment : BaseSearchActionBarFragment(), BackpressConsumable {

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

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var searchViewModel: SearchViewModel
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

        viewModel = obtainViewModel(MainViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        searchViewModel = obtainViewModel(SearchViewModel::class.java, true)
        searchViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 3) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ProductResultsFragment()
                }
                1 -> {
                    ShopResultsFragment()
                }
//                2 -> {
//                    SalePointResultFragment()
//                }
                2 -> {
                    BrandsResultFragment()
                }
                else -> {
                    Fragment()
                }
            }
        }

    }

}