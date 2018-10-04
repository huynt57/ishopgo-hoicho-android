package ishopgo.com.exhibition.ui.main.scan.history

import android.os.Bundle
import ishopgo.com.exhibition.ui.base.BaseFragment

class HistoryScanBarcodeFragment : BaseFragment() {
    companion object {
        fun newInstance(params: Bundle): HistoryScanBarcodeFragment {
            val fragment = HistoryScanBarcodeFragment()
            fragment.arguments = params

            return fragment
        }
    }
}