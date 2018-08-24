package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class IcheckProductFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): IcheckProductFragmentActionBar {
            val fragment = IcheckProductFragmentActionBar()
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
        val icheckProduct = if (arguments?.containsKey(Const.TransferKey.EXTRA_JSON) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_JSON)
        } else {
            requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
        }

        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, icheckProduct)

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, IcheckProductFragment.newInstance(extra), "IcheckProductFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }

        toolbar.rightButton(R.drawable.ic_edit_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(IcheckProductFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as IcheckProductFragment
                shareFragment.openUpdateProduct()
            }
        }
    }
}