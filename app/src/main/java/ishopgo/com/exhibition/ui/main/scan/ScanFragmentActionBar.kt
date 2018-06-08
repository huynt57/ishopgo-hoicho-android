package ishopgo.com.exhibition.ui.main.scan

import android.os.Bundle
import android.util.Log
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ScanFragmentActionBar : BaseActionBarFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        return false
    }

    companion object {

        private val TAG = "ScanFragmentActionBar"

        fun newInstance(params: Bundle): ScanFragmentActionBar {
            val fragment = ScanFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quét mã vạch")
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ScanFragment(), ScanFragment.TAG)
                .commit()

        Log.d(TAG, "userHint: ${userVisibleHint} ${isVisible} ${isResumed} ${isHidden} ")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        Log.d(TAG, "setUserVisibleHint: isVisibleToUser = [${isVisibleToUser}]")
        super.setUserVisibleHint(isVisibleToUser)

        if (!isAdded) return
        else
            if (isVisibleToUser) {
                val fragment = childFragmentManager.findFragmentByTag(ScanFragment.TAG) as? ScanFragment
                fragment?.resumeCamera()
            } else {
                val fragment = childFragmentManager.findFragmentByTag(ScanFragment.TAG) as? ScanFragment
                fragment?.pauseCamera()
            }
    }

}