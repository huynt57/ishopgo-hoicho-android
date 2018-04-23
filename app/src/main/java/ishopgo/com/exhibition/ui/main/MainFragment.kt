package ishopgo.com.exhibition.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.home.MainHomeFragment
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpNavigation()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        view_pager.setPagingEnabled(true)
        view_pager.offscreenPageLimit = 5
        view_pager.adapter = MainPagerAdapter(childFragmentManager)
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

    class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    MainHomeFragment()
                }
                1 -> {
                    MainHomeFragment()
                }
                2 -> {
                    MainHomeFragment()
                }
                3 -> {
                    MainHomeFragment()
                }
                else -> {
                    Fragment()
                }
            }
        }

        override fun getCount(): Int = 4

    }
}