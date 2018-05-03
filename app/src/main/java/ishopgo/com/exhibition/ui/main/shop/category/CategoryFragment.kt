package ishopgo.com.exhibition.ui.main.shop.category

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class CategoryFragment : BaseListFragment<List<CategoryProvider>, CategoryProvider>() {

    override fun populateData(data: List<CategoryProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        }
        else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<CategoryProvider> {
        return CategoryAdapter()
    }

    override fun firstLoad() {
        reloadData = true
        val request = LoadMoreRequest()
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        reloadData = false
        val request = LoadMoreRequest()
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun obtainViewModel(): BaseListViewModel<List<CategoryProvider>> {
        return obtainViewModel(CategoryViewMode::class.java, false)
    }
}