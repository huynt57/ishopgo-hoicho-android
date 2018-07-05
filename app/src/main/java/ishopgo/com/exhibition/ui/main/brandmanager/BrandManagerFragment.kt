package ishopgo.com.exhibition.ui.main.brandmanager

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.brandmanager.add.BrandManagerAddActivity
import ishopgo.com.exhibition.ui.main.brandmanager.update.BrandManagerUpdateActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class BrandManagerFragment : BaseListFragment<List<Brand>, Brand>() {
    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<Brand>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<Brand> {
        val adapter = BrandManagerAdapter()
        adapter.addData(Brand())
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
            @SuppressLint("SetTextI18n")
            override fun click(position: Int, data: Brand, code: Int) {
                when (code) {
                    BRAND_EDIT_CLICK -> {
                        if (UserDataManager.currentType == "Quản trị viên") {
                            val listPermission = Toolbox.gson.fromJson<ArrayList<String>>(UserDataManager.listPermission, object : TypeToken<ArrayList<String>>() {}.type)

                            if (listPermission.isNotEmpty())
                                for (i in listPermission.indices)
                                    if (Const.Permission.EDIT_BRAND == listPermission[i]) {
                                        val intent = Intent(context, BrandManagerUpdateActivity::class.java)
                                        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                                        startActivityForResult(intent, Const.RequestCode.BRAND_MANAGER_UPDATE)
                                        break
                                    }
                        } else {
                            val intent = Intent(context, BrandManagerUpdateActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                            startActivityForResult(intent, Const.RequestCode.BRAND_MANAGER_UPDATE)
                        }
                    }
                }
            }
        }

        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<Brand>> {
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