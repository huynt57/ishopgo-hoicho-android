package ishopgo.com.exhibition.ui.main.product.detail.diary_product

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductDiaryRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDiaryAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductDiaryFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var productId = -1L
    private var adapterDiary = ProductDiaryAdapter()
    private lateinit var viewModel: ProductDetailViewModel

    companion object {
        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
        const val DIARY_DELETE_CLICK = 2
    }

    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()
        setupDiaryProducts(view.context)
        swipe.setOnRefreshListener(this)
        swipe.setOnClickListener(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.productDiary.observe(this, Observer { p ->
            p?.let {

                if (reloadData) {
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE

                    adapterDiary.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                } else {
                    adapterDiary.addAll(it)
                }

                swipe.isRefreshing = false
            }
        })

        viewModel.deleteProductDiary.observe(this, Observer { p ->
            p.let {
                hideProgressDialog()
                toast("Xoá nhật ký thành công")
                firstLoad()
            }
        })

        reloadData = true
        swipe.isRefreshing = true
        firstLoad()
    }

    private fun setupDiaryProducts(context: Context) {
        view_recyclerview.adapter = adapterDiary
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = true
        view_recyclerview.setHasFixedSize(false)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)

        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(totalItemsCount)
            }
        })

        adapterDiary.listener = object : ClickableAdapter.BaseAdapterAction<DiaryProduct> {
            override fun click(position: Int, data: DiaryProduct, code: Int) {
                when (code) {
                    DIARY_IMAGE_CLICK -> {
                        if (data.images?.isNotEmpty() == true) {
                            val listImage = ArrayList<String>()
                            for (i in data.images!!.indices)
                                listImage.add(data.images!![i].image ?: "")
                            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                            startActivity(intent)
                        } else toast("Không thể mở ảnh này")
                    }

                    DIARY_USER_CLICK -> {
                        val intent = Intent(context, MemberProfileActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                        startActivity(intent)
                    }

                    DIARY_DELETE_CLICK -> {
                        context?.let {
                            MaterialDialog.Builder(it)
                                    .content("Bạn có muốn xoá nhật ký này không?")
                                    .positiveText("Có")
                                    .onPositive { _, _ ->
                                        viewModel.deleteProductDiary(data.id, productId)
                                        showProgressDialog()
                                    }
                                    .negativeText("Không")
                                    .onNegative { dialog, _ -> dialog.dismiss() }
                                    .show()
                        }
                    }
                }
            }
        }
    }

    private fun setupToolbars() {
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        toolbar.setCustomTitle("Nhật ký sản xuất")
    }

    fun firstLoad() {
        reloadData = true
        val firstLoad = ProductDiaryRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.productId = productId
        viewModel.getProductDiary(firstLoad)
        view_recyclerview.scheduleLayoutAnimation()
    }

    fun loadMore(currentCount: Int) {
        reloadData = false
        val firstLoad = ProductDiaryRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = currentCount
        firstLoad.productId = productId
        viewModel.getProductDiary(firstLoad)
    }
}