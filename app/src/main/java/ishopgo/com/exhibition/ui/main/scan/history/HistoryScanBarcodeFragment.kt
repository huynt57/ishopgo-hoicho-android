package ishopgo.com.exhibition.ui.main.scan.history

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.HistoryScan
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.update.IcheckUpdateProductActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_history_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class HistoryScanBarcodeFragment : BaseFragment() {
    private val adapter = HistoryBarCodeAdapter()

    companion object {
        fun newInstance(params: Bundle): HistoryScanBarcodeFragment {
            val fragment = HistoryScanBarcodeFragment()
            fragment.arguments = params

            return fragment
        }

        const val CLICK_PRODUCT = 0
        const val CLICK_BARCODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_history_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listBarCode = Toolbox.gson.fromJson<MutableList<HistoryScan>>(UserDataManager.currentBarCode, object : TypeToken<MutableList<HistoryScan>>() {}.type)
        if (listBarCode == null || listBarCode.isEmpty()) {
            view_empty_result_notice.visibility = View.VISIBLE
            view_empty_result_notice.text = "Nội dung trống"
        } else {
            view_empty_result_notice.visibility = View.GONE
            adapter.replaceAll(listBarCode)
        }

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<HistoryScan> {
            override fun click(position: Int, data: HistoryScan, code: Int) {
                when (code) {
                    CLICK_PRODUCT -> {
                        context?.let {
                            val intent = Intent(context, IcheckProductActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data.icheckProduct))
                            startActivity(intent)
                        }
                    }
                    CLICK_BARCODE -> {
                        if (data.code.isNotEmpty()) {
                            showDialogNoResult(data.code)
                        } else toast("Có lỗi xảy ra")
                    }
                }

            }
        }

        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    private fun showDialogNoResult(code: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thông báo")
                    .content("Không tìm thấy thông tin sản phẩm trên hệ thống. Bạn vui lòng giúp cộng đồng đóng góp thông tin\n")
                    .positiveText("Đồng ý")
                    .onPositive { dialog, _ ->
                        val intent = Intent(context, IcheckUpdateProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, code)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ bỏ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            dialog.show()
        }
    }
}