package ishopgo.com.exhibition.ui.main.stamp.buystamp.add

import android.annotation.SuppressLint
import android.app.Activity
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
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.setPhone
import ishopgo.com.exhibition.ui.main.stamp.buystamp.StampListBuyViewModel
import kotlinx.android.synthetic.main.content_buy_stamp_add.*

class BuyStampAddFragment : BaseFragment() {
    private lateinit var viewModel: StampListBuyViewModel

    companion object {

        fun newInstance(params: Bundle): BuyStampAddFragment {
            val fragment = BuyStampAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_buy_stamp_add, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_contact.setPhone("Bạn có thể đặt số lượng lớn để có giá tốt nhất sau đó bán tem cho người có nhu cầu.\n" +
                "\n" +
                "Có bán máy in tem QR Code biến đổi giá 7.000.000 đ (chi phí in tem bằng máy này ~ 30 đ/tem gồm mực + giấy)\n" +
                "\n" +
                "Tư vấn hỗ trợ : 0985771133 Mr Dương", "0985771133")

        tv_donGia.setText("200")

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
            val soLuong = if (tv_soLuong.text.toString().isNotEmpty() && !tv_soLuong.text.toString().startsWith("-"))
                tv_soLuong.text.toString().toLong()
            else {
                tv_soLuong.setText("0")
                0
            }
            if (isRequiredFieldsValid(soLuong))
                viewModel.createdBuyStamp(tv_soLuong.text.toString(), tv_ghiChu.text.toString())

        }
    }

    private fun isRequiredFieldsValid(soLuong: Long): Boolean {
        if (soLuong < 1) {
            toast("Số lượng phải >= 1")
            tv_soLuong.error = "Vui lòng nhập lại"
            tv_soLuong.requestFocus()
            val inputMethodManager = tv_soLuong.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

        viewModel.createBuyStampSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Thêm mới thành công")
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        })
    }
}