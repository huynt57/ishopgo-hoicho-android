package ishopgo.com.exhibition.ui.main.map

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_expo_map.*

class ExpoDetailTabFragment : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_expo_map
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
//        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
//        view_pager.offscreenPageLimit = 3
//        view_pager.adapter = ResultAdapter(childFragmentManager)
//    }
//
//    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 3) {
//        override fun getItem(position: Int): Fragment {
//            return when (position) {
//                0 -> {
//                    ExpoDetailFragment()
//                }
//                1 -> {
//                    ShopResultsFragment()
//                }
//                2 -> {
//                    SalePointResultFragment()
//                }
//                else -> {
//                    Fragment()
//                }
//            }
//        }
//
//    }
}