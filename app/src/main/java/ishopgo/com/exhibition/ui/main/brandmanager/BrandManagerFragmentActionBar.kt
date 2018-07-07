package ishopgo.com.exhibition.ui.main.brandmanager

import android.os.Bundle
import android.view.View
import com.google.android.gms.dynamic.IFragmentWrapper
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BrandManagerFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): BrandManagerFragmentActionBar {
            val fragment = BrandManagerFragmentActionBar()
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
                .replace(R.id.view_main_content, BrandManagerFragment(), "BrandManagerFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý thương hiệu")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentType == "Quản trị viên") {
            val listPermission = Const.listPermission

            if (listPermission.isNotEmpty())
                for (i in listPermission.indices)
                    if (Const.Permission.ADD_BRAND == listPermission[i]) {
                        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
                        toolbar.setRightButtonClickListener {
                            val fragment = childFragmentManager.findFragmentByTag(BrandManagerFragment.TAG)
                            if (fragment != null) {
                                val shareFragment = fragment as BrandManagerFragment
                                shareFragment.openAddBrand()
                            }
                        }
                        break
                    }
        } else {
            toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
            toolbar.setRightButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(BrandManagerFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as BrandManagerFragment
                    shareFragment.openAddBrand()
                }
            }
        }
    }
}