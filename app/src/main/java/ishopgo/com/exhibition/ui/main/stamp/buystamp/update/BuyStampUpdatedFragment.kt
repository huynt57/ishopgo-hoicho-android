package ishopgo.com.exhibition.ui.main.stamp.buystamp.update

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.stamp.buystamp.StampListBuyViewModel
import kotlinx.android.synthetic.main.content_buy_stamp_update.*

class BuyStampUpdatedFragment : BaseFragment() {
    private var data: StampListBuy? = null
    private lateinit var viewModel: StampListBuyViewModel

    companion object {
        const val TAG = "BuyStampUpdatedFragment"
        fun newInstance(params: Bundle): BuyStampUpdatedFragment {
            val fragment = BuyStampUpdatedFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, StampListBuy::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_buy_stamp_update, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (data != null) {
            tv_khachHang.setText(data!!.userName ?: "")
            tv_soDienThoai.setText(data!!.userPhone ?: "")
            tv_email.setText(data!!.userEmail ?: "")
            tv_thoiGian.setText(data!!.createdAt?.asDateTime() ?: "")
            tv_maDonHang.setText(data!!.code ?: "")
            tv_soLuong.setText(data!!.quantity.toString())
            tv_donGia.setText(data!!.unitPrice?.toString() ?: "")
            tv_thanhTien.setText(data!!.priceTotal?.toString() ?: "")
            tv_trangThai.setText(data!!.statusName ?: "")
            tv_ghiChu.setText(data!!.note ?: "")
        }

        tv_soLuong.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val soLuong = if (tv_soLuong.text.toString().isNotEmpty() && !tv_soLuong.text.toString().startsWith("-"))
                    tv_soLuong.text.toString().toLong()
                else {
                    tv_soLuong.setText("0")
                    0
                }
                if (soLuong <= 10000) {
                    tv_donGia.setText("200")
                    tv_thanhTien.setText("${soLuong * 200}")
                    return
                }

                if (soLuong in 10001..100000) {
                    tv_donGia.setText("150")
                    tv_thanhTien.setText("${soLuong * 150}")
                    return
                }

                if (soLuong in 100001..500000) {
                    tv_donGia.setText("100")
                    tv_thanhTien.setText("${soLuong * 100}")
                    return
                }

                if (soLuong in 500001..1000000) {
                    tv_donGia.setText("80")
                    tv_thanhTien.setText("${soLuong * 80}")
                    return
                }

                if (soLuong > 1000000) {
                    tv_donGia.setText("60")
                    tv_thanhTien.setText("${soLuong * 60}")
                    return
                }
            }
        })

        view_submit.setOnClickListener {
            if (data != null)
                if (isRequiredFieldsValid(tv_soLuong.text.toString().toInt(), tv_donGia?.money
                                ?: 0)) {
                    showProgressDialog()
                    viewModel.updateBuyStamp(data!!.id, data!!.statusId
                            ?: 0, tv_ghiChuKhac.text.toString(), tv_donGia?.money
                            ?: 0, tv_soLuong.text.toString())
                }
        }
    }

    fun openHistory() {
//        val intent = Intent(context, ProductSalePointAddActivity::class.java)
//        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
//        startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
    }

    private fun isRequiredFieldsValid(soLuong: Int, donGia: Long): Boolean {
        if (soLuong < 0) {
            toast("Số lượng phải >= 0")
            tv_soLuong.error = getString(R.string.error_field_required)
            tv_soLuong.requestFocus()
            val inputMethodManager = tv_soLuong.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
            return false
        }

        if (donGia < 0) {
            toast("Đơn giá phải >= 0")
            tv_donGia.error = getString(R.string.error_field_required)
            tv_donGia.requestFocus()
            val inputMethodManager = tv_donGia.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(StampListBuyViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.updateBuyStampSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Cập nhật thành công")
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })
    }
}