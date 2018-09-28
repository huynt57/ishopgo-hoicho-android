package ishopgo.com.exhibition.ui.main.stamp.stampwarning

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.QrCode
import ishopgo.com.exhibition.domain.response.StampListWarning
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.stamp.QrCodeViewerActivity
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerAdapter
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class StampWarningFragment : BaseListFragment<List<StampListWarning>, StampListWarning>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun populateData(data: List<StampListWarning>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampListWarning> {
        val adapter = StampWarningAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<StampListWarning> {
            override fun click(position: Int, data: StampListWarning, code: Int) {
                when(code) {
                    CLICK_BUTTOM_THU_HOI ->{
                        context?.let {
                            if (data.statusWarning == null)
                                showDialogStampWarningEviction(data.stampId ?: 0L, data.code ?: "", data.productId ?: 0L)
                            else {
                                showDialogStampWarningRestore(data.code ?: "")
                            }
                        }
                    }

                    CLICK_ITEMVIEW ->{
                        val qrcode = QrCode()
                        qrcode.name = data.code
                        qrcode.qrCode = data.code
                        val intent = Intent(context, QrCodeViewerActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(qrcode))
                        startActivity(intent)
                    }
                }

            }
        }
        return adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as StampWarningViewModel).evictionSuccess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                firstLoad()
                toast("Thu hồi tem thành công")
            }
        })

        (viewModel as StampWarningViewModel).restoreSuccess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                firstLoad()
                toast("Phục hồi tem thành công")
            }
        })
    }

    private fun showDialogStampWarningEviction(stampId: Long, code: String, productId: Long) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Nội dung cảnh báo thu hồi $code")
                    .customView(R.layout.dialog_note_stamp_warning, false)
                    .positiveText("Thu hồi")
                    .onPositive { dialog, which ->
                        showProgressDialog()
                        val note = dialog.findViewById(R.id.edit_stamp_warning_note) as TextInputEditText
                        (viewModel as StampWarningViewModel).evictionStampWarning(stampId, code, productId, note.text.toString())
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()
            dialog.show()
        }
    }

    private fun showDialogStampWarningRestore(code: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Nội dung cảnh báo phục hồi $code")
                    .customView(R.layout.dialog_note_stamp_warning, false)
                    .positiveText("Phục hồi")
                    .onPositive { dialog, which ->
                        showProgressDialog()
                        val note = dialog.findViewById(R.id.edit_stamp_warning_note) as TextInputEditText
                        (viewModel as StampWarningViewModel).restoreStampWarning(code, note.text.toString())
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()
            dialog.show()
        }
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampListWarning>> {
        return obtainViewModel(StampWarningViewModel::class.java, false)
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

    companion object {
        const val CLICK_BUTTOM_THU_HOI = 0
        const val CLICK_ITEMVIEW = 1
        fun newInstance(params: Bundle): StampWarningFragment {
            val fragment = StampWarningFragment()
            fragment.arguments = params

            return fragment
        }
    }
}