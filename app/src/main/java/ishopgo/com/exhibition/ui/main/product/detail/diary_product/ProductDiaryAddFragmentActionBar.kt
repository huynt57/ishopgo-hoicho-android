package ishopgo.com.exhibition.ui.main.product.detail.diary_product

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductDiaryAddFragmentActionBar : BaseActionBarFragment() {
    private var data = ProductDetail()
    companion object {
        fun newInstance(params: Bundle): ProductDiaryAddFragmentActionBar {
            val fragment = ProductDiaryAddFragmentActionBar()
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

        val json = if (arguments?.containsKey(Const.TransferKey.EXTRA_JSON) == true) {
            // click another product in product detail screen
            arguments!!.getString(Const.TransferKey.EXTRA_JSON)
        } else {
                requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
        }

        data = Toolbox.gson.fromJson(json, ProductDetail::class.java)

        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))

        val fragment = ProductDiaryAddFragment.newInstance(extra)
        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, fragment)
                .commit()
    }

    private fun setupToolbars() {
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        toolbar.setCustomTitle("Thêm nhật ký sản xuất")
    }
}