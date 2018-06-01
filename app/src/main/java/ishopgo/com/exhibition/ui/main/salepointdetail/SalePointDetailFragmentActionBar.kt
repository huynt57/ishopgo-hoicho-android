package ishopgo.com.exhibition.ui.main.salepointdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class SalePointDetailFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): SalePointDetailFragmentActionBar {
            val fragment = SalePointDetailFragmentActionBar()
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
                .replace(R.id.view_main_content, SalePointDetailFragment.newInstance(arguments ?: Bundle()))
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Điểm bán")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}