package ishopgo.com.exhibition.ui.main.product.detail

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductDetailFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): ProductDetailFragmentActionBar {
            val fragment = ProductDetailFragmentActionBar()
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

        val productId = if (arguments?.containsKey(Const.TransferKey.EXTRA_ID) == true) {
            // click another product in product detail screen
            arguments!!.getLong(Const.TransferKey.EXTRA_ID)
        } else {
            requireActivity().intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
        }

        val stampCode = if (arguments?.containsKey(Const.TransferKey.EXTRA_STAMP_CODE) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_STAMP_CODE)
        } else {
            requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_STAMP_CODE)
        }

        val stampId = if (arguments?.containsKey(Const.TransferKey.EXTRA_STAMP_ID) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_STAMP_ID)
        } else {
            requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_STAMP_ID)
        }

        val extra = Bundle()
        extra.putLong(Const.TransferKey.EXTRA_ID, productId)
        extra.putString(Const.TransferKey.EXTRA_STAMP_ID, stampId)
        extra.putString(Const.TransferKey.EXTRA_STAMP_CODE, stampCode)

        val fragment = ProductDetailFragment.newInstance(extra)
        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, fragment)
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}
