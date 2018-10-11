package ishopgo.com.exhibition.ui.main.product.detail

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.product.detail.diary_product.ProductDiaryAddFragment
import ishopgo.com.exhibition.ui.main.product.detail.exchange_diary.ProductExchangeDiaryAddFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_product_diary_add_tab.*

class DiaryTabFragment : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_product_diary_add_tab
    }

    private var TAG = "DiaryTabFragment"

    companion object {
        fun newInstance(params: Bundle): DiaryTabFragment {
            val fragment = DiaryTabFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        view_pager.offscreenPageLimit = 2
        view_pager.adapter = ResultAdapter(childFragmentManager)
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
    }

    inner class ResultAdapter(f: FragmentManager) : CountSpecificPager(f, 2) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ProductDiaryAddFragment.newInstance(arguments ?: Bundle())
                }
                1 -> {
                    ProductExchangeDiaryAddFragment.newInstance(arguments ?: Bundle())
                }

                else -> Fragment()
            }
        }

    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm nhật ký")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }
    }
}