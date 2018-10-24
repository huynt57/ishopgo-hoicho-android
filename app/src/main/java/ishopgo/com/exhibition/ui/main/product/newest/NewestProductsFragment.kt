package ishopgo.com.exhibition.ui.main.product.newest

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CategoriedProductsRequest
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.home.category.product.CategoryChildAdapter
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_product_by_category.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class NewestProductsFragment : BaseListFragment<List<Product>, Product>() {
    private val childAdapter = CategoryChildAdapter()
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()
    private var city = ""
    private var district = ""
    private var categoryId = -1L
    private lateinit var category: Category
    override fun initLoading() {
        // do nothing, it will apply default filter
    }

    private lateinit var filterViewModel: FilterProductViewModel
    private var filterProduct = FilterProduct()

    companion object {
        const val TAG = "NewestProductsFragment"
        fun newInstance(params: Bundle): NewestProductsFragment {
            val fragment = NewestProductsFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = if (arguments?.containsKey(Const.TransferKey.EXTRA_JSON) == true) {
            // click another product in product detail screen
            val json = arguments!!.getString(Const.TransferKey.EXTRA_JSON)
            Toolbox.gson.fromJson(json, Category::class.java)
        } else {
            Category()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_by_category, container, false)
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
        return ProductAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = CategoriedProductsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        if (category.id > 0)
            loadMore.categoryId = category.id
        if (city.isNotEmpty())
            loadMore.city = city
        if (district.isNotEmpty())
            loadMore.district = district
        loadMore.sort_by = filterProduct.sort_by
        loadMore.sort_type = filterProduct.sort_type
        loadMore.type_filter = filterProduct.filter ?: mutableListOf()
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = CategoriedProductsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        if (category.id > 0)
            loadMore.categoryId = category.id
        if (city.isNotEmpty())
            loadMore.city = city
        if (district.isNotEmpty())
            loadMore.district = district
        loadMore.sort_by = filterProduct.sort_by
        loadMore.sort_type = filterProduct.sort_type
        loadMore.type_filter = filterProduct.filter ?: mutableListOf()
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChildCategories(view)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<Product>) {
            (adapter as ClickableAdapter<Product>).listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    context?.let {
                        val intent = Intent(it, ProductDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivity(intent)
                    }
                }
            }
        }

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)

        view_region.setOnClickListener {
            getRegion(view_region)
        }

        view_category.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_popularProductsFragmentActionBar_to_categoryFragment, Bundle())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)
        filterViewModel.getDataFilter.observe(this, Observer { p ->
            p?.let {
                filterProduct = it
                firstLoad()
            }
        })

        // default loading
        if (filterViewModel.getDataFilter.value == null) {
            filterViewModel.getDataFilter(FilterProduct())
        }

        (viewModel as NewestProductsViewModel).loadRegion.observe(this, Observer { p ->
            p?.let {
                val region = Region()
                region.name = "Tất cả khu vực"
                it.add(0, region)
                adapterRegion.replaceAll(it)
            }
        })

        (viewModel as NewestProductsViewModel).loadDistrict.observe(this, Observer { p ->
            p?.let {
                val district = District()
                district.name = "Tất cả quận huyện"
                it.add(0, district)
                adapterDistrict.replaceAll(it)
            }
        })

        (viewModel as NewestProductsViewModel).childCategories.observe(this, Observer { c ->
            c?.let {
                childAdapter.replaceAll(it)
            }
        })

        (viewModel as NewestProductsViewModel).categories.observe(this, Observer { c ->
            c?.let {
                view_child_categories.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                childAdapter.replaceAll(it)
            }
        })

        (viewModel as NewestProductsViewModel).loadRegion()
        if (category.id > 0) {
            (viewModel as NewestProductsViewModel).loadChildCategory(category)
            view_category.text = category.name ?: ""
        } else {
            view_category.text = "Chọn danh mục"
            (viewModel as NewestProductsViewModel).loadCategories()
        }

        (viewModel as NewestProductsViewModel).loadRegion()
        if (category.id > 0) {
            (viewModel as NewestProductsViewModel).loadChildCategory(category)
            view_category.text = category.name ?: ""
        } else {
            view_category.text = "Chọn danh mục"
            (viewModel as NewestProductsViewModel).loadCategories()
        }
    }

    private fun setupChildCategories(view: View) {
        view_child_categories.adapter = childAdapter
        view_child_categories.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view_child_categories.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        childAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
            override fun click(position: Int, data: Category, code: Int) {
                category = data
                view_category.text = data.name
                categoryId = data.id
                (viewModel as NewestProductsViewModel).loadChildCategory(category)
                firstLoad()
            }

        }
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn khu vực")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        if (position == 0) {
                            city = ""
                            district = ""
                            view.text = data.name
                            firstLoad()
                        } else {
                            data.provinceid?.let { it1 -> (viewModel as NewestProductsViewModel).loadDistrict(it1) }
                            getDistrict(view, data.name)
                        }

                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getDistrict(view: TextView, parentName: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn quận huyện")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterDistrict
            adapterDistrict.listener = object : ClickableAdapter.BaseAdapterAction<District> {
                override fun click(position: Int, data: District, code: Int) {
                    dialog.dismiss()
                    if (position == 0) {
                        view.text = parentName
                        city = parentName
                        firstLoad()
                    } else {
                        district = data.name ?: ""
                        view.text = data.name
                        firstLoad()
                    }
                    view.error = null
                }
            }
            dialog.show()
        }
    }

    override fun obtainViewModel(): BaseListViewModel<List<Product>> {
        return obtainViewModel(NewestProductsViewModel::class.java, false)
    }

}