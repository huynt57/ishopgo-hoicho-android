package ishopgo.com.exhibition.ui.main.brandmanager

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BrandManagerFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): BrandManagerFragmentActionBar {
            val fragment = BrandManagerFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, BrandManagerFragment(), "BrandManagerFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý thương hiệu")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_add_green_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(BrandManagerFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as BrandManagerFragment
                    shareFragment.openAddBrand()
            }
        }
    }
}