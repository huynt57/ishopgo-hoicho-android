package ishopgo.com.exhibition.ui.main.WebViewer

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class WebViewFragmentActionBar : BaseActionBarFragment() {
    private var html = ""

    companion object {
        fun newInstance(html: String, params: Bundle): WebViewFragmentActionBar {
            val fragment = WebViewFragmentActionBar()
            fragment.arguments = params
            fragment.html = html

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
                .replace(R.id.view_main_content, WebViewFragment.newInstance(html, Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }


}