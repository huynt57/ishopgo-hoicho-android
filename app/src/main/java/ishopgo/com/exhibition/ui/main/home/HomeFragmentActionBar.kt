package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class HomeFragmentActionBar : BaseActionBarFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        return false
    }

    companion object {

        fun newInstance(params: Bundle): HomeFragmentActionBar {
            val fragment = HomeFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm kiếm")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.md_grey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener { mainViewModel.enableSearch() }
        toolbar.leftButton(R.drawable.ic_drawer_toggle_24dp)
        toolbar.setLeftButtonClickListener {
            if (!drawer_layout.isDrawerVisible(Gravity.START))
                drawer_layout.openDrawer(Gravity.START);
            else drawer_layout.closeDrawer(Gravity.START);
        }
        toolbar.rightButton(R.drawable.ic_search_24dp)
        toolbar.setRightButtonClickListener {
            mainViewModel.enableSearch()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(HomeViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, HomeFragment())
                .commit()
    }
}