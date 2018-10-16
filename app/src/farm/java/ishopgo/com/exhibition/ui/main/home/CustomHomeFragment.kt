package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_home.view.*

class CustomHomeFragment : HomeFragmentOverwrite() {
    private val adapter = ScuentificCouncilsAdapter(0.45f)

    override fun handleInOtherFlavor(rootView: View, viewModel: HomeViewModel, fragment: BaseFragment) {
        viewModel.loadScuentificCouncils()

        rootView.apply {
            container_scientific_councils.visibility = View.VISIBLE

            view_list_scientific_councils.adapter = adapter
            view_list_scientific_councils.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            view_list_scientific_councils.isNestedScrollingEnabled = false
            view_list_scientific_councils.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        }

        viewModel.scuentificCouncils.observe(fragment, Observer { p ->
            p?.let {
                adapter.replaceAll(it)
            }
        })
    }
}