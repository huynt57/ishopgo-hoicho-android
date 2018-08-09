package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterResult
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.BoothAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.BrandsAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.CategoryAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_search_product_filter.*

class FilterSearchFragment : BaseActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    private lateinit var viewModel: SearchProductManagerViewModel
    private lateinit var productViewModel: ProductManagerViewModel
    private var dataFilter = mutableListOf<FilterResult>()

    companion object {
        const val TAG = "FilterSearchFragment"

        fun newInstance(params: Bundle): FilterSearchFragment {
            val fragment = FilterSearchFragment()
            fragment.arguments = params

            return fragment
        }

        const val TYPE_THUONGHIEU = 0
        const val TYPE_DANHMUC = 1
        const val TYPE_GIANHANG = 2
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_search_product_filter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataFilter = Toolbox.gson.fromJson<ArrayList<FilterResult>>(arguments?.getString(Const.TransferKey.EXTRA_JSON), object : TypeToken<ArrayList<FilterResult>>() {}.type)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        edit_product_thuongHieu.setOnClickListener { getBrands(edit_product_thuongHieu) }
        edit_product_gianHang.setOnClickListener { getBooth(edit_product_gianHang) }
        edit_product_danhMuc.setOnClickListener { getCategory(edit_product_danhMuc) }
        if (dataFilter.isNotEmpty()) {
            for (i in dataFilter.indices) {
                if (dataFilter[i].type == TYPE_THUONGHIEU) {
                    edit_product_thuongHieu.setText(dataFilter[i].name ?: "")
                }
                if (dataFilter[i].type == TYPE_DANHMUC) {
                    edit_product_danhMuc.setText(dataFilter[i].name ?: "")
                }
                if (dataFilter[i].type == TYPE_GIANHANG) {
                    edit_product_gianHang.setText(dataFilter[i].name ?: "")
                }
            }
        }
        btn_filter_product.setOnClickListener {
            viewModel.getFilterSp(dataFilter)
            activity?.onBackPressed()
        }

        linearLayout.setOnClickListener(null)
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
        productViewModel.dataBrands.observe(this, Observer { p ->
            p.let {
                if (reloadData) {
                    it?.let { it1 -> adapterBrands.replaceAll(it1) }
                } else {
                    it?.let { it1 -> adapterBrands.addAll(it1) }
                }
            }
        })

        productViewModel.dataBooth.observe(this, Observer { p ->
            p.let {
                if (reloadProvider) {
                    it?.let { it1 -> adapterBooth.replaceAll(it1) }
                } else {
                    it?.let { it1 -> adapterBooth.addAll(it1) }
                }
            }
        })

        productViewModel.categories.observe(this, Observer { p ->
            p?.let {
                adapterCategory.replaceAll(it)
            }
        })
        firstLoadBrand()
        firstLoadProvider()
        firstLoadCategory()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Lọc sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

    private val adapterBrands = BrandsAdapter()

    private fun getBrands(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thương hiệu")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBrands
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreBrands(totalItemsCount)
                }
            })

            adapterBrands.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
                override fun click(position: Int, data: Brand, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        val filter = FilterResult()
                        filter.id = data.id
                        filter.name = data.name
                        filter.type = TYPE_THUONGHIEU
                        dataFilter.add(filter)
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private val adapterBooth = BoothAdapter()

    private fun getBooth(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()


            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBooth

            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        val filter = FilterResult()
                        filter.id = data.id
                        filter.name = data.boothName
                        filter.type = TYPE_GIANHANG
                        dataFilter.add(filter)
                        view.text = data.boothName ?: ""
                        view.error = null
                    }
                }
            }

            dialog.show()
        }
    }

    private val adapterCategory = CategoryAdapter()

    private fun getCategory(view: TextView) {
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

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterCategory
            adapterCategory.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                override fun click(position: Int, data: Category, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        val filter = FilterResult()
                        filter.id = data.id
                        filter.name = data.name
                        filter.type = TYPE_DANHMUC
                        dataFilter.add(filter)
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()

        }
    }

    private fun firstLoadCategory() {
        productViewModel.loadCategories()
    }

    private var reloadProvider = false

    private fun firstLoadProvider() {
        reloadProvider = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        productViewModel.getBooth(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadProvider = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        productViewModel.getBooth(loadMore)
    }

    private fun firstLoadBrand() {
        reloadData = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        productViewModel.getBrand(firstLoad)
    }

    private fun loadMoreBrands(currentCount: Int) {
        reloadData = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        productViewModel.getBrand(loadMore)
    }
}