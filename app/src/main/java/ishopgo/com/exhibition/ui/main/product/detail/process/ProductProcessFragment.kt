package ishopgo.com.exhibition.ui.main.product.detail.process

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.domain.response.ProductProcess
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.ProductProcessAdapter
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import kotlinx.android.synthetic.main.fragment_question_tab.*
import kotlinx.android.synthetic.main.fragment_shop_info.*

class ProductProcessFragment: BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    override fun requireInput(): List<String> {
        return listOf(
                Const.TransferKey.EXTRA_JSON
        )
    }

    private lateinit var productProcessAdapter: ProductProcessAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        productProcessAdapter = ProductProcessAdapter()

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        val productDetail = Toolbox.gson.fromJson(json, ProductDetail::class.java)
        productProcessAdapter.replaceAll(productDetail.process ?: listOf())

        productProcessAdapter.listener = object: ClickableAdapter.BaseAdapterAction<ProductProcess> {
            override fun click(position: Int, data: ProductProcess, code: Int) {
                val intent = Intent(requireContext(), FullDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_TITLE, data.title?: "")
                intent.putExtra(Const.TransferKey.EXTRA_JSON, data.description ?: "")
                startActivity(intent)
            }
        }
        view_recyclerview.adapter = productProcessAdapter
        val lm = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = lm
        val divider = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        view_recyclerview.addItemDecoration(divider)
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Mô tả sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

}