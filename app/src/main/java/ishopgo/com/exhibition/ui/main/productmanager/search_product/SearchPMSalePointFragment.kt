package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductManagerRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.ProductManagerRelatedAdapter
import kotlinx.android.synthetic.main.base_fragment_search.*
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class SearchPMSalePointFragment : BaseListFragment<List<Product>, Product>() {
    companion object {
        const val TAG = "SearchPMSalePointFragment"

        fun newInstance(arg: Bundle): SearchPMSalePointFragment {
            val f = SearchPMSalePointFragment()
            f.arguments = arg
            return f
        }

        const val TYPE_SP_LIENQUAN: Int = 0
        const val TYPE_SP_VATTU: Int = 1
        const val TYPE_SP_GIAIPHAP: Int = 2
    }

    private var type = TYPE_SP_LIENQUAN
    private lateinit var searchViewModel: SearchProductManagerViewModel

    private var keyword = ""

    private fun search(key: String) {

        if (keyword == key)
            return
        else {
            keyword = key
            firstLoad()
        }
    }

    override fun populateData(data: List<Product>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Product> {
        val searchProductAdapter = ProductManagerRelatedAdapter()
        searchProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (type == TYPE_SP_LIENQUAN)
                    searchViewModel.getDataSpLienQuan(data)
                if (type == TYPE_SP_VATTU)
                    searchViewModel.getDataSpVatTu(data)
                if (type == TYPE_SP_GIAIPHAP)
                    searchViewModel.getDataSpGiaiPhap(data)
                val inputMethodManager = view_search_field.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputMethodManager?.hideSoftInputFromWindow(view_search_field.windowToken, InputMethodManager.SHOW_IMPLICIT)
                activity?.onBackPressed()
            }

        }
        return searchProductAdapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<Product>> {
        return obtainViewModel(ProductManagerViewModel::class.java, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_search_swipable_recyclerview, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt(Const.TransferKey.EXTRA_REQUIRE, TYPE_SP_LIENQUAN) ?: TYPE_SP_LIENQUAN
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel = obtainViewModel(SearchProductManagerViewModel::class.java, true)
        searchViewModel.searchKey.observe(this, Observer { k ->
            k?.let {
                search(it)
            }
        })

        (viewModel as ProductManagerViewModel).totalProduct.observe(this, Observer { p ->
            p.let {
                search_total.visibility = View.VISIBLE
                search_total.text = "${it ?: 0} kết quả được tìm thấy"
            }
        })
    }

    override fun firstLoad() {
        reloadData = true
        val firstLoad = ProductManagerRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = keyword
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        reloadData = false
        val loadMore = ProductManagerRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.name = keyword
        viewModel.loadData(loadMore)
    }

    override fun initLoading() {
        firstLoad()
    }
}