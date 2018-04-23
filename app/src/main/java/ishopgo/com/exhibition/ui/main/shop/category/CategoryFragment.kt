package ishopgo.com.exhibition.ui.main.shop.category

import ishopgo.com.exhibition.domain.request.LoadMoreRequestParams
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.category.CategoryProvider

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class CategoryFragment : BaseListFragment<List<CategoryProvider>, CategoryProvider>() {
    override fun populateData(data: List<CategoryProvider>) {
        if (reloadData)
            adapter.replaceAll(data)
        else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<CategoryProvider> {
        return CategoryAdapter()
    }

    override fun firstLoad() {
        reloadData = true
        val request = LoadMoreRequestParams()
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        reloadData = false
        val request = LoadMoreRequestParams()
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)

    }

    override fun obtainViewModel(): BaseListViewModel<List<CategoryProvider>> {
        return obtainViewModel(CategoryViewMode::class.java, false)
    }
}