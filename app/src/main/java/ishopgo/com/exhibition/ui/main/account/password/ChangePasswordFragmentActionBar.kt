package ishopgo.com.exhibition.ui.community

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by hoangnh on 4/26/2018.
 */
class ChangePasswordFragmentActionBar : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ChangePasswordFragment())
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Đổi mật khẩu")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}