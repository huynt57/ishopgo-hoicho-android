package ishopgo.com.exhibition.ui.main.product.viewed

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.product.popular.PopularFragment
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

        val mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.showCategoriedProducts.observe(this, Observer {s ->
            s?.let {
                val params = Bundle()
                filterViewModel.getDataFilter(FilterProduct())
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.view_main_content, ViewedFragment.newInstance(params))
                        .addToBackStack(ViewedFragment.TAG)
                        .commit()
            }
        })
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm trong sản phẩm đã xem")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.md_grey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_viewedProductsFragmentActionBar_to_searchViewedProductsFragment)
        }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
        toolbar.rightButton(R.drawable.ic_filter_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val currentFilter = filterViewModel.getDataFilter.value
            val chosen = Bundle()
            currentFilter?.let {
                chosen.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            }
            Navigation.findNavController(it).navigate(R.id.action_viewedProductsFragmentActionBar_to_filterProductFragment4, chosen)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)

        filterViewModel.getDataFilter.observe(this, Observer { c ->
            c?.let {
                val count = (it.filter?.size
                        ?: 0) + if (it.sort_by?.isNotEmpty() == true && it.sort_type?.isNotEmpty() == true) 1 else 0
                toolbar.rightButton(R.drawable.ic_filter_highlight_24dp, count)
            }
        })
    }
}