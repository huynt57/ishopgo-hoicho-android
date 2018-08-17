package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import kotlinx.android.synthetic.main.content_no_stamp_edit.*

class NoStampEditFragment : BaseFragment() {
    private var stampId = 0L
    private var countStampExist = 0
    private lateinit var viewModel: NoStampViewModel

    companion object {

        fun newInstance(params: Bundle): NoStampEditFragment {
            val fragment = NoStampEditFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stampId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_edit, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_edit_no_stamp.setOnClickListener {
            if (isRequiredFieldsValid(edit_no_stamp_count.text.toString().toInt())) {
                viewModel.editNoStampDetail(stampId, edit_no_stamp_name.text.toString(), edit_no_stamp_count.text.toString())
            }
        }
    }

    private fun isRequiredFieldsValid(countStamp: Int): Boolean {
        if (countStamp > countStampExist) {
            toast("Số lượng tem không được lớn hơn số lượng tem còn lại")
            edit_no_stamp_count.error = getString(R.string.error_field_required)
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

        viewModel.getDataNoStampDetail.observe(this, Observer { p ->
            p?.let {
                edit_no_stamp_name.setText(it.name ?: "")
                edit_no_stamp_count.setText("${it.quantity ?: 0}")
                edit_no_stamp_serial.setText(it.serialNumber)
                tv_no_stamp_count_exits.text = "Số lượng tem còn lại: ${it.calculateQuantity ?: 0}"
                countStampExist = it.calculateQuantity ?: 0
            }
        })

        viewModel.editNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.getDetailNoStamp(stampId)
    }
}