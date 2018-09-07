package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.CertImages
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class CertImagesConfigFragment : BaseActionBarFragment(), BackpressConsumable, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var viewModel: SearchProductManagerViewModel
    private lateinit var productViewModel: ProductManagerViewModel
    private val adapter = CertImagesAdapter()
    private var providerId = 0L

    companion object {
        const val TAG = "CertImagesConfigFragment"

        fun newInstance(params: Bundle): CertImagesConfigFragment {
            val fragment = CertImagesConfigFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onRefresh() {
        loadData()
    }

    private fun loadData() {
        swipe.isRefreshing = false
        productViewModel.getCertImagesConfig(providerId)
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        providerId = arguments?.getLong(Const.TransferKey.EXTRA_ID) ?: 0L

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        context?.let {
            view_recyclerview.layoutManager = GridLayoutManager(it, 2, GridLayoutManager.VERTICAL, false)
            view_recyclerview.adapter = adapter
            view_recyclerview.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            adapter.listener = object : ClickableAdapter.BaseAdapterAction<CertImages> {
                override fun click(position: Int, data: CertImages, code: Int) {
                    viewModel.getCertImages(data)
                    activity?.onBackPressed()
                }
            }
        }
        swipe.setOnRefreshListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(SearchProductManagerViewModel::class.java, true)
        productViewModel = obtainViewModel(ProductManagerViewModel::class.java, false)
        productViewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        productViewModel.dataCertImages.observe(this, Observer { p ->
            p.let {
                if (it != null && it.isEmpty()) {
                    view_empty_result_notice.visibility = View.VISIBLE
                    view_empty_result_notice.text = "Nội dung trống"
                } else view_empty_result_notice.visibility = View.GONE

                adapter.replaceAll(it ?: mutableListOf())
            }
        })

        loadData()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chứng nhận chất lượng từ gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }
}