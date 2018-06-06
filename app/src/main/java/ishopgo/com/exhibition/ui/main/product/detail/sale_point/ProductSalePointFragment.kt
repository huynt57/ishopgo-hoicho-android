package ishopgo.com.exhibition.ui.main.product.detail.sale_point

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.ProductSalePoint
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.detail.ProductSalePointAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductSalePointProvider
import ishopgo.com.exhibition.ui.main.product.detail.add_sale_point.ProductSalePointAddActivity
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_product_sale_point_detail.*

class ProductSalePointFragment : BaseListFragment<List<ProductSalePointProvider>, ProductSalePointProvider>() {
    private lateinit var data: ProductDetail

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = ProductSalePointRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.productId = data.id
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ProductSalePointRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.productId = data.id
        viewModel.loadData(loadMore)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<ProductSalePointProvider>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ProductSalePointProvider> {
        val adapter = ProductSalePointAdapter()
        adapter.addData(ProductSalePoint())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<ProductSalePointProvider>> {
        return obtainViewModel(ProductSalePointViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        (adapter as  ProductSalePointAdapter).listener = object : ClickableAdapter.BaseAdapterAction<ProductSalePointProvider> {
            override fun click(position: Int, dataSalePoint: ProductSalePointProvider, code: Int) {
                if (dataSalePoint is ProductSalePoint) {
                    val intent = Intent(context, SalePointDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, dataSalePoint.phone)
                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                    startActivity(intent)
                }
            }
        }
        Glide.with(context).load(data.image)
                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(img_product)

        tv_product.text = data.name
        tv_product_price.text = data.price.asMoney()
        tv_product_code.text = data.code
    }

    fun openAddSalePoint() {
        val intent = Intent(context, ProductSalePointAddActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
        startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == Activity.RESULT_OK) {
            firstLoad()
            activity?.setResult(Activity.RESULT_OK)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_sale_point_detail, container, false)
    }

    companion object {
        const val TAG = "ProductSalePointFragment"

        fun newInstance(params: Bundle): ProductSalePointFragment {
            val fragment = ProductSalePointFragment()
            fragment.arguments = params

            return fragment
        }
    }
}