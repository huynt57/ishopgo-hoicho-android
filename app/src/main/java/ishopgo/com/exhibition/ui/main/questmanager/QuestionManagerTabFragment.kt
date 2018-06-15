package ishopgo.com.exhibition.ui.main.questmanager

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_question_tab.*

class QuestionManagerTabFragment : BaseFragment() {
    private var TAG = "QuestionManagerProcessedFragment"
    private lateinit var viewModel: QuestionSearchViewModel

    companion object {
        fun newInstance(params: Bundle): QuestionManagerTabFragment {
            val fragment = QuestionManagerTabFragment()
            fragment.arguments = params

            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))

        view_pager.adapter = ResultAdapter(childFragmentManager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                TAG = when (position) {
                    0 -> {
                        "QuestionManagerProcessedFragment"
                    }
                    1 -> {
                        "QuestionManagerPendingFragment"
                    }
                    else -> {
                        "QuestionManagerProcessedFragment"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 2) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    QuestionManagerProcessedFragment.newInstance(Bundle())
                }
                1 -> {
                    QuestionManagerPendingFragment.newInstance(Bundle())
                }

                else -> Fragment()
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(QuestionSearchViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer {
            it?.let { resolveError(it) }
        })
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý hỏi đáp")

        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener {
                viewModel.search(TAG)

        }
    }
}