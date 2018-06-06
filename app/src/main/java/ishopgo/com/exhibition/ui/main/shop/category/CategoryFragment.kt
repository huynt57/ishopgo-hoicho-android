package ishopgo.com.exhibition.ui.main.shop.category

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.BoothCategoriesRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.shop.ShopDetailShareViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class CategoryFragment : BaseListFragment<List<CategoryProvider>, CategoryProvider>() {

    companion object {
        fun newInstance(params: Bundle): CategoryFragment {
            val fragment = CategoryFragment()
            fragment.arguments = params
            return fragment
        }
    }

    private var boothId = 0L
    private lateinit var sharedViewModel : ShopDetailShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boothId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = obtainViewModel(ShopDetailShareViewModel::class.java, true)
        sharedViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<CategoryProvider>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<CategoryProvider> {
        val categoryAdapter = CategoryAdapter()
        categoryAdapter.listener = object: ClickableAdapter.BaseAdapterAction<CategoryProvider> {
            override fun click(position: Int, data: CategoryProvider, code: Int) {
                when (code) {
                    CategoryAdapter.TYPE_CHILD -> {
                        sharedViewModel.showCategoriedProducts(data)
                    }
                    CategoryAdapter.TYPE_PARENT -> {
                        sharedViewModel.showCategoriedProducts(data)
                    }
                    else -> {

                    }
                }

            }

        }
        return categoryAdapter
    }

    override fun firstLoad() {
        reloadData = true
        val request = BoothCategoriesRequest()
        request.boothId = boothId
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        // no support loadmore
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing, true, false))
    }

    override fun obtainViewModel(): BaseListViewModel<List<CategoryProvider>> {
        return obtainViewModel(CategoryViewModel::class.java, false)
    }
}