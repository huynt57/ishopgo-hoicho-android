package ishopgo.com.exhibition.ui.main.account

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class AccountFragmentActionBar : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    companion object {

        fun newInstance(params: Bundle): AccountFragmentActionBar {
            val fragment = AccountFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thông tin tài khoản")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, AccountFragment())
                .commit()
    }
}