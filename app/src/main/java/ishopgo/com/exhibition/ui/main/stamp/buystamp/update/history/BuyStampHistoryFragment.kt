package ishopgo.com.exhibition.ui.main.stamp.buystamp.update.history

import android.os.Bundle
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox

class BuyStampHistoryFragment : BaseFragment() {
    private var data: StampListBuy? = null

    companion object {
        const val TAG = "BuyStampHistoryFragment"
        fun newInstance(params: Bundle): BuyStampHistoryFragment {
            val fragment = BuyStampHistoryFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, StampListBuy::class.java)
    }
}