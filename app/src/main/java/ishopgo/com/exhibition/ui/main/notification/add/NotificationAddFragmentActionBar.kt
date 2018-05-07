package ishopgo.com.exhibition.ui.main.notification.add

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationAddFragmentActionBar : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, NotificationAddFragment()).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm thông báo")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}