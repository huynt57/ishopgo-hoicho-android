package ishopgo.com.exhibition.ui.main.administrator.add

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class AdministratorAddFragmentActionBar : BaseActionBarFragment() {

    companion object {
        const val TAG = "AdministratorAddFragment"
        fun newInstance(params: Bundle): AdministratorAddFragmentActionBar {
            val fragment = AdministratorAddFragmentActionBar()
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
                .replace(R.id.view_main_content, AdministratorAddFragment.newInstance(Bundle()), "AdministratorAddFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm quản trị viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}