package ishopgo.com.exhibition.ui.main.home

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class HomeFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): HomeFragmentActionBar {
            val fragment = HomeFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm kiếm")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.md_grey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        toolbar.leftButton(R.drawable.ic_drawer_toggle_24dp)
        toolbar.setLeftButtonClickListener {
            if (!drawer_layout.isDrawerVisible(Gravity.START))
                drawer_layout.openDrawer(Gravity.START);
            else drawer_layout.closeDrawer(Gravity.START);
        }
        toolbar.rightButton(R.drawable.ic_search_24dp)
        toolbar.setRightButtonClickListener {
            toast("Search screen")
        }
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