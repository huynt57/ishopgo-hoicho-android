package ishopgo.com.exhibition.ui.main.product.shop

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.BoothCategoriesRequest
import ishopgo.com.exhibition.domain.request.SameShopProductsRequest
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_products.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductsFragment : BaseListFragment<List<Product>, Product>() {
    override fun initLoading() {
        firstLoad()
    }

    private val adapterCategory = ProductBoothCategoryAdapter()

    companion object {
        fun newInstance(params: Bundle): ProductsFragment {
            val fragment = ProductsFragment()
            fragment.arguments = params
            return fragment
        }

        const val SORT_VALUE_CREATE_AT = "created_at"
        const val SORT_VALUE_PRICE = "price"
        const val SORT_BY_ASC = "asc"
        const val SORT_BY_DESC = "desc"

        const val CLICK_ITEMVIEW = 0
        const val CLICK_ADD_PRODUCT = 1
        const val CLICK_DELETE_PRODUCT = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    private var boothId = 0L
    private var categoryId: Long = 0
    private var sortValue: String = ""
    private var sortBy: String = ""
    private lateinit var shopViewModel: ShopDetailViewModel
    private var listProduct = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boothId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
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
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Product> {
        return ProducBoothAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = SameShopProductsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        loadMore.boothId = boothId
        loadMore.categoryId = categoryId
        loadMore.sortValue = sortValue
        loadMore.sortBy = sortBy
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = SameShopProductsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.boothId = boothId
        loadMore.categoryId = categoryId
        loadMore.sortValue = sortValue
        loadMore.sortBy = sortBy
        viewModel.loadData(loadMore)
    }

    fun firstLoadBoothCategory() {
        val request = BoothCategoriesRequest()
        request.boothId = boothId
        (viewModel as ProductsOfShopViewModel).loadBoothCategory(request)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_category.text = "Tất cả danh mục"
        tv_category.setOnClickListener {
            showBoothCategory(tv_category)
        }

        tv_filter.text = "Mới nhất"
        tv_filter.setOnClickListener {
            showDialogFilter(tv_filter)
        }

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<Product>)
            (adapter as ClickableAdapter<Product>).listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    when (code) {
                        CLICK_ITEMVIEW -> {
                            context?.let {
                                val intent = Intent(it, ProductDetailActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                                startActivity(intent)
                            }
                        }
                        CLICK_ADD_PRODUCT -> {
                            listProduct.add(data)
                            shopViewModel.updateProductCount(listProduct.size)
                        }

                        CLICK_DELETE_PRODUCT -> {
                            listProduct.remove(data)
                            shopViewModel.updateProductCount(listProduct.size)
                        }
                    }

                }
            }
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)
    }

    private fun showBoothCategory(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn danh mục")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager.isAutoMeasureEnabled = true
            rv_search.layoutManager = layoutManager
            rv_search.adapter = adapterCategory

            adapterCategory.listener = object : ClickableAdapter.BaseAdapterAction<Category> {

                override fun click(position: Int, data: Category, code: Int) {
                    categoryId = data.id
                    view.text = data.name ?: ""
                    dialog.dismiss()
                    firstLoad()
                }

            }

            val window = dialog.window
            if (window != null) {
                window.attributes.windowAnimations = R.style.BottomDialog
                window.setGravity(Gravity.BOTTOM)
            }
            dialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogFilter(view: TextView) {
        context?.let {
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .title("Sắp xếp theo")
                        .customView(R.layout.dialog_filter_products, false)
                        .negativeText("Huỷ")
                        .onNegative { dialog, _ -> dialog.dismiss() }
                        .autoDismiss(false)
                        .canceledOnTouchOutside(false)
                        .build()

                val tv_filter_news = dialog.findViewById(R.id.tv_filter_news) as TextView
                val tv_filter_down_to_up = dialog.findViewById(R.id.tv_filter_down_to_up) as TextView
                val tv_filter_up_to_down = dialog.findViewById(R.id.tv_filter_up_to_down) as TextView

                tv_filter_news.setOnClickListener {
                    view.text = "Mới nhất"
                    sortValue = SORT_VALUE_CREATE_AT
                    sortBy = ""
                    firstLoad()
                    dialog.dismiss()
                }
                tv_filter_down_to_up.setOnClickListener {
                    view.text = "Giá từ thấp tới cao"
                    sortValue = SORT_VALUE_PRICE
                    sortBy = SORT_BY_ASC
                    firstLoad()
                    dialog.dismiss()
                }
                tv_filter_up_to_down.setOnClickListener {
                    view.text = "Giá từ cao tới thấp"
                    sortValue = SORT_VALUE_PRICE
                    sortBy = SORT_BY_DESC
                    firstLoad()
                    dialog.dismiss()
                }

                val window = dialog.window
                if (window != null) {
                    window.attributes.windowAnimations = R.style.BottomDialog
                    window.setGravity(Gravity.BOTTOM)
                }
                dialog.show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as ProductsOfShopViewModel).dataBoothCategory.observe(this, Observer { p ->
            p.let {
                it?.let { it1 -> adapterCategory.replaceAll(it1) }
                val category = Category()
                category.id = 0
                category.name = "Tất cả danh mục"
                adapterCategory.addData(0, category)
            }
        })

        shopViewModel = obtainViewModel(ShopDetailViewModel::class.java, true)

        firstLoadBoothCategory()
    }

    override fun obtainViewModel(): BaseListViewModel<List<Product>> {
        return obtainViewModel(ProductsOfShopViewModel::class.java, false)
    }
}
