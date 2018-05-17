package ishopgo.com.exhibition.ui.main.brandmanager.add

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BrandManagerAddFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): BrandManagerAddFragmentActionBar {
            val fragment = BrandManagerAddFragmentActionBar()
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
                .replace(R.id.view_main_content, BrandManagerAddFragment()).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm thương hiệu")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}