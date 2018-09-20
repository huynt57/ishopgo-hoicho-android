package ishopgo.com.exhibition.ui.main.stamp.buystamp.add

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
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
        view_submit.setOnClickListener { toast("Đang phát triển") }
//        btn_edit_no_stamp.setOnClickListener {
//            if (isRequiredFieldsValid(edit_no_stamp_count.text.toString().toLong())) {
//                viewModel.addNoStampDetail(edit_no_stamp_name.text.toString(), edit_no_stamp_count.text.toString(), serialNumber)
//            }
//        }
    }

    private fun isRequiredFieldsValid(countStamp: Long): Boolean {
//        if (countStamp > countStampExist) {
//            toast("Số lượng tem không được lớn hơn số lượng tem còn lại")
//            edit_no_stamp_count.error = "Vui lòng nhập lại"
//            edit_no_stamp_count.requestFocus()
//            val inputMethodManager = edit_no_stamp_count.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.showSoftInput(view, 0)
//            return false
//        }

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


    }
}