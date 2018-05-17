package ishopgo.com.exhibition.ui.main.questmanager

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
import kotlinx.android.synthetic.main.fragment_question_tab.*

class QuestionManagerTabFragment : BaseFragment() {

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
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_pager.adapter = ResultAdapter(childFragmentManager)
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 2) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> QuestionManagerProcessedFragment.newInstance(Bundle())
                1 -> QuestionManagerPeddingFragment.newInstance(Bundle())

                else -> Fragment()
            }
        }

    }
}