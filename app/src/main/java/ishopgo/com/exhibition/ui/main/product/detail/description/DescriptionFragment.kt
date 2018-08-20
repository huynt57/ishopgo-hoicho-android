package ishopgo.com.exhibition.ui.main.product.detail.description

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class DescriptionFragment : BaseActionBarFragment() {
    companion object {
        fun newInstance(params: Bundle): DescriptionFragment {
            val fragment = DescriptionFragment()
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
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết mô tả")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}