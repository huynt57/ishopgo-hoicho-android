package ishopgo.com.exhibition.ui.main.salepoint.add

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class SalePointAddFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): SalePointAddFragmentActionBar {
            val fragment = SalePointAddFragmentActionBar()
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
                .replace(R.id.view_main_content, SalePointAddFragment.newInstance(Bundle()),"SalePointAddFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm điểm bán")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}