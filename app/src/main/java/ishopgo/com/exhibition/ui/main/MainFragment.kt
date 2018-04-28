package ishopgo.com.exhibition.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.community.CommunityFragmentActionBar
import ishopgo.com.exhibition.ui.main.account.AccountFragmentActionBar
import ishopgo.com.exhibition.ui.main.home.HomeFragmentActionBar
import ishopgo.com.exhibition.ui.main.home.category.product.ProductsByCategoryFragment
import ishopgo.com.exhibition.ui.main.home.search.SearchFragment
import ishopgo.com.exhibition.ui.main.scan.ScanFragmentActionBar
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainFragment : BaseFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        val count = pagerAdapter.count
        for (i in 0..count) {
            val item = pagerAdapter.getItem(i)
            if (item is BackpressConsumable)
                if (item.onBackPressConsumed())
                    return true
        }

        return childFragmentManager.popBackStackImmediate()
    }

    companion object {
        const val TAB_HOME = 0
        const val TAB_COMMUNITY = 1
        const val TAB_SCAN = 2
        const val TAB_CHAT = 3
        const val TAB_ACCOUNT = 4
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MainViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.isSearchEnable.observe(this, Observer {
            if (it == true) {
                showSearch()
            }
        })
        viewModel.showCategoriedProducts.observe(this, Observer { s ->
            s?.let {
                childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.content_main_container, ProductsByCategoryFragment.newInstance(Bundle()))
                        .addToBackStack(ProductsByCategoryFragment.TAG)
                        .commit()
            }
        })
    }

    private fun showSearch() {
        val fragment = childFragmentManager.findFragmentByTag(SearchFragment.TAG)
        if (fragment == null) {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom)
                    .add(R.id.content_main_container, SearchFragment(), SearchFragment.TAG)
                    .addToBackStack(SearchFragment.TAG)
                    .commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpNavigation()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        view_pager.setPagingEnabled(true)
        view_pager.offscreenPageLimit = 5
        pagerAdapter = MainPagerAdapter(childFragmentManager)
        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                view_bottom_navigation.currentItem = position
            }
        })
    }

    private fun setUpNavigation() {
        view_bottom_navigation.defaultBackgroundColor = ResourcesCompat.getColor(resources, R.color.colorPureWhite, null)
        view_bottom_navigation.accentColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        view_bottom_navigation.inactiveColor = ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)

        val item1 = AHBottomNavigationItem(
                resources.getString(R.string.tab_home),
                AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_shopping_bag),
                ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
        )
        val item2 = AHBottomNavigationItem(
                resources.getString(R.string.tab_community),
                AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_teamwork),
                ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
        )
        val item3 = AHBottomNavigationItem(
                resources.getString(R.string.tab_scan),
                AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_qr_code),
                ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
        )
        val item4 = AHBottomNavigationItem(
                resources.getString(R.string.tab_message),
                AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_chat),
                ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
        )
        val item5 =
                AHBottomNavigationItem(
                        resources.getString(R.string.tab_profile),
                        AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_profile),
                        ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
                )

        view_bottom_navigation.addItem(item1)
        view_bottom_navigation.addItem(item2)
        view_bottom_navigation.addItem(item3)
        view_bottom_navigation.addItem(item4)
        view_bottom_navigation.addItem(item5)

        view_bottom_navigation.currentItem = 0
        view_bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        view_bottom_navigation.setOnTabSelectedListener({ position, _ ->
            view_pager.currentItem = position
            true
        })
    }

    class MainPagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 5) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                TAB_HOME -> {
                    HomeFragmentActionBar()
                }
                TAB_COMMUNITY -> {
                    CommunityFragmentActionBar.newInstance(Bundle())
                }
                TAB_SCAN -> {
                    ScanFragmentActionBar.newInstance(Bundle())
                }
                TAB_CHAT -> {
                    Fragment()
                }
                TAB_ACCOUNT -> {
                    AccountFragmentActionBar.newInstance(Bundle())
                }
                else -> {
                    Fragment()
                }
            }
        }

    }
}