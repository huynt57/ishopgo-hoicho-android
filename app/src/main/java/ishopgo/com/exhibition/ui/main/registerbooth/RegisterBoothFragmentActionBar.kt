package ishopgo.com.exhibition.ui.main.registerbooth

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class RegisterBoothFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): RegisterBoothFragmentActionBar {
            val fragment = RegisterBoothFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Đăng ký gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
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
                .replace(R.id.view_main_content, RegisterBoothFragment.newInstance(Bundle()))
                .commit()
    }

}