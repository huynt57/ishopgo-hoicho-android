package ishopgo.com.exhibition.ui.main.scan.history

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_history_scan_tab.*

class HistoryScanTabFragment : BaseFragment() {
    private var TAG = "HistoryScanTabFragment"

    companion object {
        fun newInstance(params: Bundle): HistoryScanTabFragment {
            val fragment = HistoryScanTabFragment()
            fragment.arguments = params

            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_scan_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager.offscreenPageLimit = 3
        view_pager.adapter = ResultAdapter(childFragmentManager)
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 2) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    HistoryScanBarcodeFragment.newInstance(arguments ?: Bundle())
                }
                1 -> {
                    HistoryScanQrCodeFragment.newInstance(arguments ?: Bundle())
                }

                else -> Fragment()
            }
        }

    }
}