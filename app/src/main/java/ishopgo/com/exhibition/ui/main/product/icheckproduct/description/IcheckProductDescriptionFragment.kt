package ishopgo.com.exhibition.ui.main.product.icheckproduct.description

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.content_icheck_description_detail.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class IcheckProductDescriptionFragment : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): IcheckProductDescriptionFragment {
            val fragment = IcheckProductDescriptionFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_icheck_description_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        val json = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE, "")
        json?.let {
            view_product_description.loadData(String.format(
                    "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                    Const.webViewCSS, json), "text/html", null)
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thông tin mô tả")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }
}