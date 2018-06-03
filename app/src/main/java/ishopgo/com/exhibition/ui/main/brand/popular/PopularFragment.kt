package ishopgo.com.exhibition.ui.main.brand.popular

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularFragment : BaseListFragment<List<HighlightBrandProvider>, HighlightBrandProvider>() {

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun populateData(data: List<HighlightBrandProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<HighlightBrandProvider> {
        return HighlightBrandAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = Request()
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        // do nothing
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<HighlightBrandProvider>) {
            (adapter as ClickableAdapter<HighlightBrandProvider>).listener = object : ClickableAdapter.BaseAdapterAction<HighlightBrandProvider> {
                override fun click(position: Int, data: HighlightBrandProvider, code: Int) {
                    context?.let {
                        if (data is IdentityData) {
                            val intent = Intent(it, ProductsOfBrandActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                            intent.putExtra(Const.TransferKey.EXTRA_TITLE, data.provideName())
                            startActivity(intent)
                        }
                    }
                }
            }
        }
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)
    }

    override fun obtainViewModel(): BaseListViewModel<List<HighlightBrandProvider>> {
        return obtainViewModel(PopularBrandsViewModel::class.java, false)
    }

}
