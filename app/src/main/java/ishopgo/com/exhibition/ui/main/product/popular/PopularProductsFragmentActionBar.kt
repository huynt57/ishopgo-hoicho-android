package ishopgo.com.exhibition.ui.main.product.popular

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularProductsFragmentActionBar : BaseActionBarFragment() {
    private lateinit var filterViewModel: FilterProductViewModel

    companion object {

        fun newInstance(params: Bundle): PopularProductsFragmentActionBar {
            val fragment = PopularProductsFragmentActionBar()
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
                .replace(R.id.view_main_content, PopularFragment.newInstance(Bundle()), "PopularFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm trong sản phẩm nổi bật")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.md_grey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_popularProductsFragmentActionBar_to_searchPopularProductsFragment)
        }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
        toolbar.rightButton(R.drawable.ic_filter_highlight_24dp)
        toolbar.setRightButtonClickListener {
            Navigation.findNavController(it).navigate(R.id.action_popularProductsFragmentActionBar_to_filterProductFragment2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)

        filterViewModel.getDataFilter.observe(viewLifeCycleOwner!!, Observer { c ->
            c?.let {
                val count = (it.filter?.size ?: 0) + 1
                toolbar.rightButton(R.drawable.ic_filter_highlight_24dp, count)
            }
        })
    }
}