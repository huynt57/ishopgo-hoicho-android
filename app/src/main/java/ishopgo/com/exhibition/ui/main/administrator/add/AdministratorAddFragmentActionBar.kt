package ishopgo.com.exhibition.ui.main.administrator.add

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
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
                .replace(R.id.view_main_content, AdministratorAddFragment.newInstance(arguments
                        ?: Bundle()), "AdministratorAddFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm quản trị viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        val data = Toolbox.gson.fromJson(json, Administrator::class.java)
        if (data != null) {
            toolbar.rightButton(R.drawable.ic_delete_highlight_24dp)
            toolbar.setRightButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(AdministratorAddFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as AdministratorAddFragment
                    shareFragment.deleleAdministrator()
                }
            }
        }
    }
}