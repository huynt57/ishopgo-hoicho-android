package ishopgo.com.exhibition.ui.main.productmanager

import android.os.Bundle
import android.view.View
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductManagerFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): ProductManagerFragmentActionBar {
            val fragment = ProductManagerFragmentActionBar()
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
                .replace(R.id.view_main_content, ProductManagerFragment.newInstance(Bundle()), "ProductManagerFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }


        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(ProductManagerFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as ProductManagerFragment
                    shareFragment.performSearching()
            }
        }

        if (UserDataManager.currentType == "Quản trị viên" || UserDataManager.currentType == "Nhân viên gian hàng") {
            val listPermission = Const.listPermission

            if (listPermission.isNotEmpty())
                for (i in listPermission.indices)
                    if (Const.Permission.ADD_PRODUCT == listPermission[i]) {
                        toolbar.rightButton2(R.drawable.ic_add_highlight_24dp)
                        toolbar.setRight2ButtonClickListener {
                            val fragment = childFragmentManager.findFragmentByTag(ProductManagerFragment.TAG)
                            if (fragment != null) {
                                val shareFragment = fragment as ProductManagerFragment
                                shareFragment.openAddProductManager()
                            }
                        }
                        break
                    }
        } else {
            toolbar.rightButton2(R.drawable.ic_add_highlight_24dp)
            toolbar.setRight2ButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(ProductManagerFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as ProductManagerFragment
                    shareFragment.openAddProductManager()
                }
            }
        }
    }
}