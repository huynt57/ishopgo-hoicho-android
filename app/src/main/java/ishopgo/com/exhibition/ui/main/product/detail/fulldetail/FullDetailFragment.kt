package ishopgo.com.exhibition.ui.main.product.detail.fulldetail

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class FullDetailFragment : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): FullDetailFragment {
            val fragment = FullDetailFragment()
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
                .replace(R.id.view_main_content, WebViewFragment.newInstance(arguments
                        ?: Bundle()))
                .commit()
    }

    private fun setupToolbars() {
        val title = requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_TITLE)
        toolbar.setCustomTitle(title ?: "Thông tin chi tiết")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }


}