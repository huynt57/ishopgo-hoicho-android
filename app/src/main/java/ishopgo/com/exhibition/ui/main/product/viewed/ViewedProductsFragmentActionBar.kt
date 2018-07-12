package ishopgo.com.exhibition.ui.main.product.viewed

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ViewedProductsFragmentActionBar : BaseActionBarFragment() {
    private lateinit var filterViewModel: FilterProductViewModel

    companion object {

        fun newInstance(params: Bundle): ViewedProductsFragmentActionBar {
            val fragment = ViewedProductsFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ViewedFragment.newInstance(Bundle()), "ViewedFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Sản phẩm đã xem")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }

        toolbar.rightButton(R.drawable.ic_filter_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(ViewedFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as ViewedFragment
                shareFragment.openFilterFragment()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)

        filterViewModel.getDataFilter.observe(this, Observer { c ->
            c?.let {
                val count = (it.filter?.size ?: 0) + 1
                toolbar.rightButton(R.drawable.ic_filter_24dp, count)
            }
        })
    }
}