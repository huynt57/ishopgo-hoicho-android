package ishopgo.com.exhibition.ui.main.ticketmanager

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
import kotlinx.android.synthetic.main.fragment_ticket_tab.*

class TicketManagerTabFragment : BaseFragment() {
    private lateinit var adapter: DetailAdapter

    companion object {
        const val TAG = "TicketManagerTabFragment"
        fun newInstance(params: Bundle): TicketManagerTabFragment {
            val fragment = TicketManagerTabFragment()
            fragment.arguments = params
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetailAdapter(childFragmentManager)
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 3
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
    }

    inner class DetailAdapter(fm: FragmentManager) : CountSpecificPager(fm, 3) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    TicketManagerFragment.newInstance(arguments ?: Bundle())
                }
                1 -> {
                    TicketManagerPaidFragment.newInstance(arguments ?: Bundle())
                }
                2 -> {
                    TicketManagerUnpaidFragment.newInstance(arguments ?: Bundle())
                }
                else -> {
                    Fragment()
                }
            }
        }

    }
}