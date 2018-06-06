package ishopgo.com.exhibition.ui.main.boothmanager

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.boothmanager.add_booth.BoothManagerAddActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class BoothManagerFragment : BaseListFragment<List<BoothManagerProvider>, BoothManagerProvider>() {
    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<BoothManagerProvider>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<BoothManagerProvider> {
        val adapter = BoothManagerAdapter()
        adapter.addData(BoothManager())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<BoothManagerProvider>> {
        return obtainViewModel(BoothManagerViewModel::class.java, false)
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
        if (adapter is ClickableAdapter<BoothManagerProvider>) {
            (adapter as ClickableAdapter<BoothManagerProvider>).listener = object : ClickableAdapter.BaseAdapterAction<BoothManagerProvider> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: BoothManagerProvider, code: Int) {
                    if (data is BoothManager) {
                        val boothId = data.id
                        val intent = Intent(context, ShopDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
                        startActivityForResult(intent, Const.RequestCode.BOOTH_MANAGER_DELETE)
                    }
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun openAddBoothManager() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_select_register, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()

            val tv_register_member = dialog.findViewById(R.id.tv_register_member) as TextView
            val tv_register_store = dialog.findViewById(R.id.tv_register_store) as TextView
            tv_register_member.text = "Thêm gian hàng từ thành viên"
            tv_register_store.text = "Thêm gian hàng mới"
            tv_register_member.setOnClickListener {
                toast("Đang phát triển")
            }

            tv_register_store.setOnClickListener {
                val intent = Intent(context, BoothManagerAddActivity::class.java)
                startActivityForResult(intent, Const.RequestCode.BOOTH_MANAGER_ADD)
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Const.RequestCode.BOOTH_MANAGER_ADD || requestCode == Const.RequestCode.BOOTH_MANAGER_DELETE) && resultCode == RESULT_OK)
            firstLoad()
    }

    companion object {
        const val TAG = "BoothManagerFragment"
        fun newInstance(params: Bundle): BoothManagerFragment {
            val fragment = BoothManagerFragment()
            fragment.arguments = params

            return fragment
        }
    }
}