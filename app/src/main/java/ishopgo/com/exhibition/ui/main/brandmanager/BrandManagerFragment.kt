package ishopgo.com.exhibition.ui.main.brandmanager

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.brandmanager.add.BrandManagerAddActivity
import ishopgo.com.exhibition.ui.main.brandmanager.update.BrandManagerUpdateActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class BrandManagerFragment : BaseListFragment<List<BrandManagerProvider>, BrandManagerProvider>() {
    override fun populateData(data: List<BrandManagerProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<BrandManagerProvider> {
        val adapter = BrandManagerAdapter()
        adapter.addData(Brand())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<BrandManagerProvider>> {
        return obtainViewModel(BrandManagerViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<BrandManagerProvider>) {
            (adapter as ClickableAdapter<BrandManagerProvider>).listener = object : ClickableAdapter.BaseAdapterAction<BrandManagerProvider> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: BrandManagerProvider, code: Int) {
                    when (code) {
                        BRAND_EDIT_CLICK -> {
                            if (data is Brand) {
                                val intent = Intent(context, BrandManagerUpdateActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                                startActivityForResult(intent, Const.RequestCode.BRAND_MANAGER_UPDATE)
                            }
                        }
                        BRAND_FEATURED_CLICK -> {
                            if (data is Brand) {
                                val isFeatured = if (data.provideIsFeatured()) BRAND_NOT_FEATURED else BRAND_FEATURED
                                if (viewModel is BrandManagerViewModel) (viewModel as BrandManagerViewModel).updateBrand(data.id, data.provideName(), "", isFeatured.toString())
                            }
                        }
                    }

                }

            }
        }
    }

    fun openAddBrand() {
        val intent = Intent(context, BrandManagerAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.BRAND_MANAGER_ADD)
    }


    companion object {
        const val TAG = "BrandManagerFragment"
        fun newInstance(params: Bundle): BrandManagerFragment {
            val fragment = BrandManagerFragment()
            fragment.arguments = params

            return fragment
        }

        const val BRAND_EDIT_CLICK = 1
        const val BRAND_FEATURED_CLICK = 2

        const val BRAND_FEATURED = 1
        const val BRAND_NOT_FEATURED = 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.BRAND_MANAGER_UPDATE && resultCode == RESULT_OK)
            firstLoad()
        if (requestCode == Const.RequestCode.BRAND_MANAGER_ADD && resultCode == RESULT_OK)
            firstLoad()
    }
}