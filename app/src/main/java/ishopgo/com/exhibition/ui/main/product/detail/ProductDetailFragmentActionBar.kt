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

    private var productScreen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        val productId = if (arguments?.containsKey(Const.TransferKey.EXTRA_ID) == true) {
            // click another product in product detail screen
            productScreen = true
            arguments!!.getLong(Const.TransferKey.EXTRA_ID)
        } else {
            productScreen = false
            requireActivity().intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
        }

        val stampCode = if (arguments?.containsKey(Const.TransferKey.EXTRA_STAMP_CODE) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_STAMP_CODE)
        } else {
            if (!productScreen)
            requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_STAMP_CODE)
            else ""
        }

        val stampId = if (arguments?.containsKey(Const.TransferKey.EXTRA_STAMP_ID) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_STAMP_ID)
        } else {
            if (!productScreen)
                requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_STAMP_ID)
            else ""
        }

        val stampType = if (arguments?.containsKey(Const.TransferKey.EXTRA_STAMP_TYPE) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_STAMP_TYPE)
        } else {
            if (!productScreen)
                requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_STAMP_TYPE)
            else ""
        }

        val stampScan = if (arguments?.containsKey(Const.TransferKey.EXTRA_SCAN_PRODUCT) == true) {
            // click another product in product detail screen
            arguments!!.getBoolean(Const.TransferKey.EXTRA_SCAN_PRODUCT)
        } else {
            if (!productScreen)
                requireActivity().intent.getBooleanExtra(Const.TransferKey.EXTRA_SCAN_PRODUCT, false)
            else false
        }

        val extra = Bundle()
        extra.putLong(Const.TransferKey.EXTRA_ID, productId)
        extra.putString(Const.TransferKey.EXTRA_STAMP_ID, stampId)
        extra.putString(Const.TransferKey.EXTRA_STAMP_CODE, stampCode)
        extra.putString(Const.TransferKey.EXTRA_STAMP_TYPE, stampType)
        extra.putBoolean(Const.TransferKey.EXTRA_SCAN_PRODUCT, stampScan)

        val fragment = ProductDetailFragment.newInstance(extra)
        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, fragment)
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

}
