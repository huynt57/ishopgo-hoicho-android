package ishopgo.com.exhibition.ui.main.stamp.nostamp.add

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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import kotlinx.android.synthetic.main.content_no_stamp_edit.*

class NoStampAddFragment : BaseFragment() {
    private var stampId = 0L
    private var countStampExist = 0
    private var serialNumber = ""
    private lateinit var viewModel: NoStampViewModel

    companion object {

        fun newInstance(params: Bundle): NoStampAddFragment {
            val fragment = NoStampAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_edit, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_no_stamp_count.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edit_no_stamp_serial.setText("${serialNumber} - ${s.toString()}")
            }

        })

        btn_edit_no_stamp.setOnClickListener {
            if (isRequiredFieldsValid(edit_no_stamp_count.text.toString().toLong())) {
                viewModel.addNoStampDetail(edit_no_stamp_name.text.toString(), edit_no_stamp_count.text.toString(), serialNumber)
            }
        }
    }

    private fun isRequiredFieldsValid(countStamp: Long): Boolean {
        if (countStamp > countStampExist) {
            toast("Số lượng tem không được lớn hơn số lượng tem còn lại")
            edit_no_stamp_count.error = "Vui lòng nhập lại"
            edit_no_stamp_count.requestFocus()
            val inputMethodManager = edit_no_stamp_count.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(NoStampViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getDataNoStampCreated.observe(this, Observer { p ->
            p?.let {
                edit_no_stamp_serial.setText(it.serialNumberPrefix)
                tv_no_stamp_count_exits.text = "Số lượng tem còn lại: ${it.calculateQuantity ?: 0}"
                countStampExist = it.calculateQuantity ?: 0
                serialNumber = it.serialNumberPrefix ?: ""
            }
        })

        viewModel.addNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.getCreatedNoStamp()
    }
}