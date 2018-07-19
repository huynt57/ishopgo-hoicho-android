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
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.chat.local.ChatFragment
import ishopgo.com.exhibition.ui.chat.local.contact.search.SearchContactFragment
import ishopgo.com.exhibition.ui.chat.local.inbox.search.SearchInboxFragment
import ishopgo.com.exhibition.ui.community.CommunityFragmentActionBar
import ishopgo.com.exhibition.ui.community.SearchCommunityFragmentActionBar
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.filterproduct.FilterProductFragment
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.login.require.RequireLoginFragment
import ishopgo.com.exhibition.ui.main.account.AccountFragmentActionBar
import ishopgo.com.exhibition.ui.main.home.HomeFragmentActionBar
import ishopgo.com.exhibition.ui.main.home.category.product.ProductsByCategoryFragment
import ishopgo.com.exhibition.ui.main.home.category.product.search.SearchProductsOfCategoryFragment
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

        private val TAG = "MainFragment"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var filterViewModel: FilterProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)
        filterViewModel.showFragmentFilter.observe(this, Observer {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.content_main_container, FilterProductFragment.newInstance(extra))
                    .addToBackStack(FilterProductFragment.TAG)
                    .commit()
        })

        viewModel = obtainViewModel(MainViewModel::class.java, true)

        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.isSearchEnable.observe(this, Observer {
            if (it == true) {
                showSearch()
            }
        })
        viewModel.isSearchCommunityEnable.observe(this, Observer {
            if (it == true) {
                showSearchCommunity()
            }
        })
        viewModel.openSearchInCategory.observe(this, Observer {
            it?.let {
                showSearchInCategory(it)
            }
        })
        viewModel.enableSearchInbox.observe(this, Observer {
            if (it == true) {
                showSearchInbox()
            }
        })
        viewModel.enableSearchContact.observe(this, Observer {
            if (it == true) {
                showSearchContact()
            }
        })

        viewModel.showCategoriedProducts.observe(this, Observer { s ->
            s?.let {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.content_main_container, ProductsByCategoryFragment.newInstance(params))
                        .addToBackStack(ProductsByCategoryFragment.TAG)
                        .commit()
            }
        })
        viewModel.notificationCount.observe(this, Observer { c ->
            c?.let {
                if (c > 0) {
                    view_bottom_navigation.setNotification(c.toString(), 1)
                } else {
                    view_bottom_navigation.setNotification("", 1)
                }
            }
        })
        viewModel.inboxUnreadCount.observe(this, Observer { c ->
            c?.let {
                if (c > 0) {
                    view_bottom_navigation.setNotification(c.toString(), 3)
                } else {
                    view_bottom_navigation.setNotification("", 3)
                }
            }
        })

        if (UserDataManager.currentType == "Quản trị viên" || UserDataManager.currentType == "Nhân viên gian hàng") {
            viewModel.loadPermission()
        }
    }

    override fun onResume() {
        super.onResume()

        val isUserLoggedIn = UserDataManager.currentUserId > 0
        if (isUserLoggedIn) {
            viewModel.loadUnreadNotificationCount()
            viewModel.loadUnreadInboxCount()
        }
    }

    private fun showSearchInCategory(cat: Category) {
        val fragment = childFragmentManager.findFragmentByTag(SearchProductsOfCategoryFragment.TAG)
        if (fragment == null) {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(cat))
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom)
                    .add(R.id.content_main_container, SearchProductsOfCategoryFragment.newInstance(extra), SearchProductsOfCategoryFragment.TAG)
                    .addToBackStack(SearchProductsOfCategoryFragment.TAG)
                    .commit()
        }
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

    private fun showSearchCommunity() {
        val fragment = childFragmentManager.findFragmentByTag(SearchFragment.TAG)
        if (fragment == null) {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom)
                    .add(R.id.content_main_container, SearchCommunityFragmentActionBar(), SearchCommunityFragmentActionBar.TAG)
                    .addToBackStack(SearchCommunityFragmentActionBar.TAG)
                    .commit()
        }
    }

    private fun showSearchInbox() {
        val fragment = childFragmentManager.findFragmentByTag(SearchInboxFragment.TAG)
        if (fragment == null) {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom)
                    .add(R.id.content_main_container, SearchInboxFragment(), SearchInboxFragment.TAG)
                    .addToBackStack(SearchInboxFragment.TAG)
                    .commit()
        }
    }

    private fun showSearchContact() {
        val fragment = childFragmentManager.findFragmentByTag(SearchContactFragment.TAG)
        if (fragment == null) {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom)
                    .add(R.id.content_main_container, SearchContactFragment(), SearchContactFragment.TAG)
                    .addToBackStack(SearchContactFragment.TAG)
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
                    if (UserDataManager.currentUserId > 0)
                        ChatFragment.newInstance(Bundle())
                    else
                        RequireLoginFragment()
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