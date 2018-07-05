package ishopgo.com.exhibition.ui.main.boothmanager

import android.os.Bundle
import android.view.View
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BoothManagerFragmentActionBar : BaseActionBarFragment() {

    companion object {
        const val TAG = "BoothManagerFragment"
        fun newInstance(params: Bundle): BoothManagerFragmentActionBar {
            val fragment = BoothManagerFragmentActionBar()
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
                .replace(R.id.view_main_content, BoothManagerFragment.newInstance(Bundle()), "BoothManagerFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentType == "Quản trị viên") {
            val listPermission = Toolbox.gson.fromJson<ArrayList<String>>(UserDataManager.listPermission, object : TypeToken<ArrayList<String>>() {}.type)

            if (listPermission.isNotEmpty())
                for (i in listPermission.indices)
                    if (Const.Permission.ADD_PROVIDER == listPermission[i]) {
                        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
                        toolbar.setRightButtonClickListener {
                            val fragment = childFragmentManager.findFragmentByTag(BoothManagerFragment.TAG)
                            if (fragment != null) {
                                val shareFragment = fragment as BoothManagerFragment
                                shareFragment.openAddBoothManager()
                            }
                        }
                        break
                    }
        } else {
            toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
            toolbar.setRightButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(BoothManagerFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as BoothManagerFragment
                    shareFragment.openAddBoothManager()
                }
            }
        }
    }
}