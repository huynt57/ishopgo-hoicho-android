package ishopgo.com.exhibition.ui.main.salepointdetail

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
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
                .replace(R.id.view_main_content, SalePointDetailFragment.newInstance(arguments
                        ?: Bundle()), "SalePointDetailFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Điểm bán lẻ")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }

        toolbar.rightButton(R.drawable.icon_qr_code_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(SalePointDetailFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as SalePointDetailFragment
                shareFragment.openQRCode()
            }
        }

        if (UserDataManager.currentUserPhone == arguments?.getString(Const.TransferKey.EXTRA_REQUIRE, "") ?: "") {
            toolbar.rightButton2(R.drawable.ic_delete_highlight_24dp)
            toolbar.setRight2ButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(SalePointDetailFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as SalePointDetailFragment
                    shareFragment.deleteProduct()
                }
            }
        }
    }
}