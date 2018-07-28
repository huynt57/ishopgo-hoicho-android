package ishopgo.com.exhibition.ui.main.shop.info.list_sale_point_all

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailActivity
import ishopgo.com.exhibition.ui.main.shop.info.SalePointAdapter
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class ListSalePointFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        reloadData = true
        viewModel.loadInfo(shopId)
    }

    private val adapter = SalePointAdapter()
    private var shopId = -1L

    companion object {

        fun newInstance(params: Bundle): ListSalePointFragment {
            val fragment = ListSalePointFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_swipable_recyclerview, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        view_recyclerview.setHasFixedSize(false)
        view_recyclerview.adapter = adapter


        adapter.listener = object : ClickableAdapter.BaseAdapterAction<SearchSalePoint> {
            override fun click(position: Int, data: SearchSalePoint, code: Int) {
                context?.let {
                    val intent = Intent(it, SalePointDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.phone)
                    startActivityForResult(intent, Const.RequestCode.UPDATE_PRODUCT_SALE_POINT)
                }
            }
        }

        swipe.setOnRefreshListener(this)
    }

    private lateinit var viewModel: ShopInfoViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ShopInfoViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.listSalePoint.observe(this, Observer { i ->
            i?.let {
                if (it.isEmpty()) {
                    view_empty_result_notice.visibility = View.VISIBLE
                    view_empty_result_notice.text = "Nội dung trống"
                } else view_empty_result_notice.visibility = View.GONE

                adapter.replaceAll(it)
                swipe.isRefreshing = false
            }
        })
        swipe.isRefreshing = true
        viewModel.loadInfo(shopId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Const.RequestCode.SALE_POINT_ADD || requestCode == Const.RequestCode.UPDATE_PRODUCT_SALE_POINT) && resultCode == Activity.RESULT_OK) {
            viewModel.loadInfo(shopId)
            activity?.setResult(Activity.RESULT_OK)
        }
    }
}