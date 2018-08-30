package ishopgo.com.exhibition.ui.main.scan

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.content_scan_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ScanResultFragment : BaseActionBarFragment() {
    companion object {
        private val TAG = "ScanResultFragment"

        fun newInstance(params: Bundle): ScanResultFragment {
            val fragment = ScanResultFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var content = ""

    private fun setupToolbars() {
        toolbar.setCustomTitle("Kết quả tìm quét mã")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_scan_result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        content = arguments?.getString(Const.TransferKey.EXTRA_CONTENT, "") ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        tv_scanResult.text = content
    }

}