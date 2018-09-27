package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.QrCode
import ishopgo.com.exhibition.domain.response.StampNoList
import ishopgo.com.exhibition.domain.response.StampNoListNew
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.stamp.QrCodeViewerActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.add.NoStampAddActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.assign.NoStampAssignActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.edit.NoStampEditActivity
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class NoStampFragment : BaseListFragment<List<StampNoListNew>, StampNoListNew>() {
    override fun initLoading() {
        firstLoad()
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<StampNoListNew>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampNoListNew> {
        return NoStampAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampNoListNew>> {
        return obtainViewModel(NoStampViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (adapter as NoStampAdapter).listener = object : ClickableAdapter.BaseAdapterAction<StampNoListNew> {
            override fun click(position: Int, data: StampNoListNew, code: Int) {
                dialogOptionNoStamp(data)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as NoStampViewModel).errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        (viewModel as NoStampViewModel).downloadSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Lô tem đã được gửi vào email, vui lòng kiểm tra lại")
        })
    }

    private fun dialogOptionNoStamp(data: StampNoListNew) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_select_option_stamp_distribution, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tv_edit_noStamp = dialog.findViewById(R.id.tv_edit_noStamp) as VectorSupportTextView
            tv_edit_noStamp.setOnClickListener {
                val qrcode = QrCode()
                qrcode.name = data.code
                qrcode.qrCode = data.code
                val intent = Intent(context, QrCodeViewerActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(qrcode))
                startActivityForResult(intent, Const.RequestCode.STAMP_ORDER_UPDATE)
                //                if (data.quantity == data.quantityExists) {
//                    val intent = Intent(context, NoStampEditActivity::class.java)
//                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
//                    startActivityForResult(intent, Const.RequestCode.EDIT_NO_STAMP)
//                    dialog.dismiss()
//                } else {
//                    toast("Lô này đã có các sản phẩm được gán bạn không thể cập nhật thông tin")
//                }
            }
            val tv_assign_noStamp = dialog.findViewById(R.id.tv_assign_noStamp) as VectorSupportTextView
            tv_assign_noStamp.setOnClickListener {
                dialogDownloadNoStamp(data)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun dialogDownloadNoStamp(data: StampNoListNew) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_download_no_stamp, false)
                    .title("Tải xuống lô tem")
                    .autoDismiss(false)
                    .positiveText("Tải xuống")
                    .onPositive { dialog, which ->
                        showProgressDialog()
                        val edit_number_stamp = dialog.findViewById(R.id.edit_number_stamp) as TextInputEditText
                        (viewModel as NoStampViewModel).downloadStamp(data.id, edit_number_stamp.text.toString())
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, which -> dialog.dismiss() }
                    .canceledOnTouchOutside(true)
                    .build()

            dialog.show()
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
        const val TAG = "NoStampFragment"
        fun newInstance(params: Bundle): NoStampFragment {
            val fragment = NoStampFragment()
            fragment.arguments = params

            return fragment
        }
    }
}