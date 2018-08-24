package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampNoList
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.add.NoStampAddActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.assign.NoStampAssignActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.edit.NoStampEditActivity
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class NoStampFragment : BaseListFragment<List<StampNoList>, StampNoList>() {
    override fun initLoading() {
        firstLoad()
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<StampNoList>) {
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

    fun openAddActivity() {
        val intent = Intent(context, NoStampAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.ADD_NO_STAMP)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampNoList> {
        return NoStampAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampNoList>> {
        return obtainViewModel(NoStampViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (adapter as NoStampAdapter).listener = object : ClickableAdapter.BaseAdapterAction<StampNoList> {
            override fun click(position: Int, data: StampNoList, code: Int) {
                dialogOptionNoStamp(data)
            }
        }
    }

    private fun dialogOptionNoStamp(data: StampNoList) {
        context?.let {
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .customView(R.layout.dialog_select_option_stamp_distribution, false)
                        .autoDismiss(false)
                        .canceledOnTouchOutside(true)
                        .build()
                val tv_edit_noStamp = dialog.findViewById(R.id.tv_edit_noStamp) as VectorSupportTextView
                tv_edit_noStamp.setOnClickListener {
                    if (data.quantity == data.quantityExists) {
                        val intent = Intent(context, NoStampEditActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivityForResult(intent, Const.RequestCode.EDIT_NO_STAMP)
                        dialog.dismiss()
                    } else {
                        toast("Lô này đã có các sản phẩm được gán bạn không thể cập nhật thông tin")
                    }
                }
                val tv_assign_noStamp = dialog.findViewById(R.id.tv_assign_noStamp) as VectorSupportTextView
                tv_assign_noStamp.setOnClickListener {
                    val intent = Intent(context, NoStampAssignActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    intent.putExtra(Const.TransferKey.EXTRA_STAMP_COUNT, data.quantityExists)
                    startActivityForResult(intent, Const.RequestCode.ASSIGN_NO_STAMP)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = LoadMoreRequest()
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = LoadMoreRequest()
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Const.RequestCode.EDIT_NO_STAMP || requestCode == Const.RequestCode.ADD_NO_STAMP || requestCode == Const.RequestCode.ASSIGN_NO_STAMP) && resultCode == Activity.RESULT_OK)
            firstLoad()
    }

    companion object {
        val TAG = "NoStampFragment"
        fun newInstance(params: Bundle): NoStampFragment {
            val fragment = NoStampFragment()
            fragment.arguments = params

            return fragment
        }
    }
}