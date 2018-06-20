package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductCommentsRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailProvider
import ishopgo.com.exhibition.ui.main.product.detail.RatingProductViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_comment_product.*

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommentProductFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    private var adapter = ProductCommentAdapter()
    private lateinit var scroller: LinearSmoothScroller
    private lateinit var viewModel: ProductCommentViewModel
    private lateinit var ratingViewModel: RatingProductViewModel
    private var dataProduct: ProductDetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comment_product, container, false)
    }

    companion object {
        fun newInstance(params: Bundle): CommentProductFragment {
            val f = CommentProductFragment()
            f.arguments = params

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        dataProduct = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (UserDataManager.currentUserId > 0) {
            linearLayout.visibility = View.VISIBLE
            edt_comment.setOnClickListener {
                dataProduct?.let { it1 -> ratingViewModel.enableCommentRating(it1) }
            }
            linearLayout.setOnClickListener {
                dataProduct?.let { it1 -> ratingViewModel.enableCommentRating(it1) }
            }
        } else {
            linearLayout.visibility = View.GONE
        }

        scroller = object : LinearSmoothScroller(context) {

            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.adapter = adapter
        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore()
            }
        })

        swipe.setOnRefreshListener(this)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductCommentProvider> {
            override fun click(position: Int, data: ProductCommentProvider, code: Int) {
                if (data is ProductComment) {
                    val intent = Intent(context, MemberProfileActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                    startActivity(intent)
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ratingViewModel = obtainViewModel(RatingProductViewModel::class.java, true)
        ratingViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        ratingViewModel.isSusscess.observe(this, Observer {
            firstLoad()
            view_recyclerview.post {
                scroller.targetPosition = 0
                view_recyclerview.layoutManager.startSmoothScroll(scroller)
            }
        })

        viewModel = obtainViewModel(ProductCommentViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.dataReturned.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE

                    adapter.replaceAll(it)
                    hideProgressDialog()
                } else {
                    adapter.addAll(it)
                }
                swipe.isRefreshing = false
            }
        })

        reloadData = true
        swipe.isRefreshing = true
        firstLoad()
    }

    fun firstLoad() {
        reloadData = true
        val loadMore = ProductCommentsRequest()
        loadMore.lastId = -1L
        loadMore.parentId = -1L
        loadMore.productId = dataProduct?.id ?: -1L
        loadMore.limit = Const.PAGE_LIMIT
        viewModel.loadData(loadMore)
        view_recyclerview.scheduleLayoutAnimation()
    }

    fun loadMore() {
        reloadData = false
        val loadMore = ProductCommentsRequest()
        val item = adapter.getItem(adapter.itemCount - 1)
        if (item is IdentityData) {
            loadMore.lastId = item.id
            loadMore.parentId = -1L
            loadMore.productId = dataProduct?.id ?: -1L
            loadMore.limit = Const.PAGE_LIMIT
            viewModel.loadData(loadMore)
        }
    }
}