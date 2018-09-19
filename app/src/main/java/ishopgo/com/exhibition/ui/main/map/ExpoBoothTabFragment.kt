package ishopgo.com.exhibition.ui.main.map

import android.arch.lifecycle.Observer
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
import kotlinx.android.synthetic.main.fragment_expo_map_booth_tab.*

class ExpoBoothTabFragment : BaseFragment() {
    private var TAG = "ExpoBoothTabFragment"
    private lateinit var viewModel: ExpoDetailViewModel

    companion object {
        fun newInstance(params: Bundle): ExpoBoothTabFragment {
            val fragment = ExpoBoothTabFragment()
            fragment.arguments = params

            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expo_map_booth_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager.offscreenPageLimit = 3
        view_pager.adapter = ResultAdapter(childFragmentManager)
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 3) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ExpoBoothAllFragment.newInstance(arguments ?: Bundle())
                }
                1 -> {
                    ExpoBoothBoughtFragment.newInstance(arguments ?: Bundle())
                }
                2 -> {
                    ExpoBoothEmptyFragment.newInstance(arguments ?: Bundle())
                }

                else -> Fragment()
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ExpoDetailViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { it ->
            it?.let { resolveError(it) }
        })
    }
}