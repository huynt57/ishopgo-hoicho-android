package ishopgo.com.exhibition.ui.main.notification

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.notification.add.NotificationAddFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationFragmentActionBar : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, NotificationFragment(), "NotificationAddFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý thông báo")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentType == "Chủ hội chợ") {

            toolbar.rightButton(R.drawable.ic_add_green_24dp)
            toolbar.setRightButtonClickListener {

                val fragment = childFragmentManager.findFragmentByTag(NotificationFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as NotificationFragment
                    shareFragment.openNotificationAdd()
                }
            }
        }


        toolbar.rightButton2(R.drawable.ic_done_all_black_24dp)
        toolbar.setRight2ButtonClickListener {
        }
    }
}