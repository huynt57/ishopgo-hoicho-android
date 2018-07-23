package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.configbooth.ConfigBoothActivity
import ishopgo.com.exhibition.ui.main.home.category.product.ProductsByCategoryFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ShopDetailFragmentActionBar : BaseActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    companion object {
        fun newInstance(params: Bundle): ShopDetailFragmentActionBar {
            val fragment = ShopDetailFragmentActionBar()
            fragment.arguments = params
            return fragment
        }
    }

    private lateinit var shareViewModel: ShopDetailShareViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ShopDetailFragment.newInstance(arguments
                        ?: Bundle()), ShopDetailFragment.TAG)
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shareViewModel = obtainViewModel(ShopDetailShareViewModel::class.java, true)
        shareViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        shareViewModel.showCategoriedProducts.observe(this, Observer { s ->
            s?.let {
                if (it is Category) {
                    val params = Bundle()
                    params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                    childFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                            .add(R.id.frame_main_content, ProductsByCategoryFragment.newInstance(params))
                            .addToBackStack(ProductsByCategoryFragment.TAG)
                            .commit()
                }
            }
        })
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        // tam thoi bo xoa gian hang theo yeu cau cua Sep
//        if (UserDataManager.currentType == "Chủ hội chợ") {
//            toolbar.rightButton(R.drawable.ic_delete_highlight_24dp)
//            toolbar.setRightButtonClickListener {
//                val fragment = childFragmentManager.findFragmentByTag(ShopDetailFragment.TAG)
//                if (fragment != null) {
//                    val shareFragment = fragment as ShopDetailFragment
//                    shareFragment.deleleBooth()
//                }
//            }
//        } else if (UserDataManager.currentType == "Quản trị viên") {
//            val listPermission = Const.listPermission
//
//            if (listPermission.isNotEmpty())
//                for (i in listPermission.indices)
//                    if (Const.Permission.DELETE_PROVIDER == listPermission[i]) {
//                        toolbar.rightButton(R.drawable.ic_delete_highlight_24dp)
//                        toolbar.setRightButtonClickListener {
//                            val fragment = childFragmentManager.findFragmentByTag(ShopDetailFragment.TAG)
//                            if (fragment != null) {
//                                val shareFragment = fragment as ShopDetailFragment
//                                shareFragment.deleleBooth()
//                            }
//                        }
//                        break
//                    }
//        }

        val boothId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
        if (UserDataManager.currentType == "Chủ gian hàng" && UserDataManager.currentUserId == boothId)
        toolbar.rightButton(R.drawable.ic_setting_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val intent = Intent(it.context, ConfigBoothActivity::class.java)
            it.context.startActivity(intent)
        }

        toolbar.rightButton2(R.drawable.icon_qr_code_highlight_24dp)
        toolbar.setRight2ButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(ShopDetailFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as ShopDetailFragment
                shareFragment.openQRCodeBooth()
            }
        }
    }

}