package ishopgo.com.exhibition.ui.main.ticket

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class TicketFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): TicketFragmentActionBar {
            val fragment = TicketFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Vé tham quan của tôi")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, TicketFragment.newInstance(Bundle()))
                .commit()
    }
}