package ishopgo.com.exhibition.ui.main.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.support.v7.content.res.AppCompatResources
import android.util.TypedValue
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asColor
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_expo_map2.*

class ExpoDetailFragment2 : BaseActionBarFragment() {
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var expoInfo: ExpoConfig
    private var fairId = -1L
    private lateinit var viewModel: ExpoMapShareViewModel

    companion object {
        fun newInstance(params: Bundle): ExpoDetailFragment2 {
            val f = ExpoDetailFragment2()
            f.arguments = params
            return f
        }

        const val TAB_INFO = 0
        const val TAB_BOOTH = 1

        private val TAG = "MainFragment"
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_expo_map2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setUpNavigation()
        setUpViewPager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fairId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        expoInfo = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Tìm kiếm gian hàng")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(R.color.md_grey_700.asColor(requireContext()))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener {
            if (::expoInfo.isInitialized) {
                viewModel.openSearchBoothFragment(expoInfo.id!!)
//                Navigation.findNavController(it).navigate(R.id.action_expoDetailFragment_to_searchBoothFragment, extra)
            }
        }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)

        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        toolbar.rightButton(R.drawable.icon_qr_code_highlight_24dp)
        toolbar.setRightButtonClickListener {
            viewModel.openQrCode(expoInfo)
//                        val extra = Bundle()
//            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(expoInfo))
//            Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoDetailFragment_to_qrCodeExpo, extra)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ExpoMapShareViewModel::class.java, true)
    }

    private fun setUpViewPager() {
        view_pager.setPagingEnabled(true)
        view_pager.offscreenPageLimit = 2
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
                resources.getString(R.string.tab_info),
                AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_shopping_bag),
                ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
        )

        val item2 = AHBottomNavigationItem(
                resources.getString(R.string.tab_booth),
                AppCompatResources.getDrawable(view_bottom_navigation.context, R.drawable.ic_teamwork),
                ResourcesCompat.getColor(resources, R.color.md_blue_grey_700, null)
        )

        view_bottom_navigation.addItem(item1)
        view_bottom_navigation.addItem(item2)

        view_bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        view_bottom_navigation.setOnTabSelectedListener { position, _ ->
            view_pager.currentItem = position
            true
        }
    }

    inner class MainPagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 2) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                TAB_INFO -> {
                    ExpoInfoFragment.newInstance(arguments ?: Bundle())
                }
                TAB_BOOTH -> {
                    Fragment()
                    ExpoBoothTabFragment.newInstance(arguments ?: Bundle())
                }
                else -> {
                    Fragment()
                }
            }
        }

    }
}