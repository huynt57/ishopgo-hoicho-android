package ishopgo.com.exhibition.ui.main.productfollow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.RequestCode.PRODUCT_FOLLOW
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class ProductFollowFragment : BaseListFragment<List<Product>, Product>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<Product>) {
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
        hideProgressDialog()
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Product> {
        return ProductAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<Product>> {
        return obtainViewModel(ProductFollowViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<Product>) {
            (adapter as ClickableAdapter<Product>).listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    val intent = Intent(context, ProductDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    startActivityForResult(intent, PRODUCT_FOLLOW)
                }
            }
        }
    }

    companion object {
        const val TAG = "ProductFollowFragment"
        fun newInstance(params: Bundle): ProductFollowFragment {
            val fragment = ProductFollowFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PRODUCT_FOLLOW && resultCode == Activity.RESULT_OK)
            firstLoad()
    }
}