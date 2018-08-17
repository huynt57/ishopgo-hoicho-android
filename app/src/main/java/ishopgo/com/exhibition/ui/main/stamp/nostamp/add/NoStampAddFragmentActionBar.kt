package ishopgo.com.exhibition.ui.main.stamp.nostamp.add

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class NoStampAddFragmentActionBar: BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): NoStampAddFragmentActionBar {
            val fragment = NoStampAddFragmentActionBar()
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
                .replace(R.id.view_main_content, NoStampAddFragment.newInstance(arguments
                        ?: Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm lô tem")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}