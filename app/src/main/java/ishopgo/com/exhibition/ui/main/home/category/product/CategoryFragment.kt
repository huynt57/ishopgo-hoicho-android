package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.home.HomeViewModel
import ishopgo.com.exhibition.ui.main.home.category.CategoryAdapter
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class CategoryFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    companion object {
        const val TAG = "CategoryFragment"

        fun newInstance(params: Bundle): CategoryFragment {
            val fragment = CategoryFragment()
            fragment.arguments = params

            return fragment
        }

    }
    private val categoriesAdapter = CategoryAdapter()
    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    override fun onRefresh() {
        viewModel.loadCategories()
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        setupCategories(view.context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = obtainViewModel(MainViewModel::class.java, true)

        viewModel = obtainViewModel(HomeViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.categories.observe(this, Observer { c ->
            c?.let {
                categoriesAdapter.replaceAll(it)

                swipe.isRefreshing = false
            }
        })
        viewModel.loadCategories()
    }

    private fun setupCategories(context: Context) {
        view_recyclerview.adapter = categoriesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        swipe.setOnRefreshListener(this)
        swipe.isRefreshing = true

        categoriesAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Category> {

            override fun click(position: Int, data: Category, code: Int) {
                when (code) {
                    CategoryAdapter.TYPE_CHILD -> {
                        activity?.onBackPressed()
                        mainViewModel.showCategoriedProducts(data)
                    }
                    CategoryAdapter.TYPE_PARENT -> {
                        activity?.onBackPressed()
                        mainViewModel.showCategoriedProducts(data)
                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Danh sách danh mục")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }
}