package ishopgo.com.exhibition.ui.main.brandmanager.update

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BrandManagerUpdateFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): BrandManagerUpdateFragmentActionBar {
            val fragment = BrandManagerUpdateFragmentActionBar()
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
                .replace(R.id.view_main_content, BrandManagerUpdateFragment.newInstance(arguments ?: Bundle()), "BrandManagerUpdateFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Cập nhật thương hiệu")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_delete_green_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(BrandManagerUpdateFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as BrandManagerUpdateFragment
                shareFragment.deleteBrand()
            }
        }
    }
}