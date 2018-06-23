package ishopgo.com.exhibition.ui.main.home.category.product.search

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.item_search_product.view.*

/**
 * Created by xuanhong on 6/19/18. HappyCoding!
 */
class SearchProductsOfCategoryFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "SearchProducts"

        fun newInstance(arg: Bundle): SearchProductsOfCategoryFragment {
            val f = SearchProductsOfCategoryFragment()
            f.arguments = arg
            return f
        }
    }

    private var searchKey = ""
    private lateinit var adapter: SearchProductAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: SearchProductViewModel
    private lateinit var currentCategory: Category

    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_search_swipable_recyclerview
    }

    override fun triggerSearch(key: String) {
        searchKey = key
        firstLoad()
    }

    override fun dismissSearch() {
        activity?.onBackPressed()
    }

    override fun cancelSearch() {
        searchKey = ""
        firstLoad()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm kiếm trong ${currentCategory.name}"

        adapter = SearchProductAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let {
                    val intent = Intent(it, ProductDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    startActivity(intent)
                }
            }

        }
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(totalItemsCount)
            }
        }
        view_recyclerview.addOnScrollListener(scrollListener)

        swipe.setOnRefreshListener(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.dataReturned.observe(this, Observer { data ->
            data?.let {
                populateData(it)

                finishLoading()
            }
        })
        viewModel.total.observe(this, Observer { p ->
            p.let {
                if (it != null) {
                    search_total.text = "${it} kết quả"
                }
            }
        })

        firstLoad()
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val firstLoad = SearchProductRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.keyword = searchKey
        firstLoad.categoryId = currentCategory.id
        viewModel.loadData(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = SearchProductRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.keyword = searchKey
        loadMore.categoryId = currentCategory.id
        viewModel.loadData(loadMore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(SearchProductViewModel::class.java, false)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        currentCategory = Toolbox.gson.fromJson(json, Category::class.java)
    }

    private fun populateData(data: List<Product>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

    class SearchProductAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<Product>() {
        var screenWidth: Int = UserDataManager.displayWidth
        var screenHeight: Int = UserDataManager.displayHeight

        override fun getChildLayoutResource(viewType: Int): Int {
            return R.layout.item_search_product
        }

        override fun createHolder(v: View, viewType: Int): ViewHolder<Product> {
            val productHolder = ProductHolder(v, ConverterSearchProduct())
            val layoutParams = productHolder.itemView.layoutParams

            if (itemWidthRatio > 0)
                layoutParams.width = (screenWidth * itemWidthRatio).toInt()
            if (itemHeightRatio > 0)
                layoutParams.height = (screenHeight * itemHeightRatio).toInt()

            return productHolder
        }

        override fun onBindViewHolder(holder: ViewHolder<Product>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }

        internal inner class ProductHolder(view: View, private val converter: Converter<Product, SearchProductProvider>) : BaseRecyclerViewAdapter.ViewHolder<Product>(view) {

            override fun populate(data: Product) {
                super.populate(data)

                val converted = converter.convert(data)
                itemView.apply {
                    Glide.with(itemView.context).load(converted.provideImage())
                            .apply(RequestOptions
                                    .placeholderOf(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder)
                            )
                            .into(iv_thumb)
                    view_name.text = converted.provideName()
                    view_code.text = converted.provideCode()
                }
            }
        }
    }

    interface SearchProductProvider {
        fun provideImage(): String
        fun provideName(): String
        fun provideCode(): String
    }

    internal class ConverterSearchProduct : Converter<Product, SearchProductProvider> {

        override fun convert(from: Product): SearchProductProvider {
            return object : SearchProductProvider {
                override fun provideImage(): String {
                    return from.image ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideCode(): String {
                    return from.code ?: ""
                }

            }
        }

    }
}