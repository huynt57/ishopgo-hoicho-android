package ishopgo.com.exhibition.ui.main.stamp.buystamp

import android.os.Bundle
import ishopgo.com.exhibition.ui.base.BaseFragment

class BuyStampFragment : BaseFragment() {
    companion object {

        fun newInstance(params: Bundle): BuyStampFragment {
            val fragment = BuyStampFragment()
            fragment.arguments = params

            return fragment
        }
    }
}