package ishopgo.com.exhibition.ui.main.salepointdetail

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class SalePointDetailFragmentActionBar : BaseActionBarFragment() {
    private var dataProduct: ProductDetail? = null

    companion object {

        fun newInstance(params: Bundle): SalePointDetailFragmentActionBar {
            val fragment = SalePointDetailFragmentActionBar()
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
                .replace(R.id.view_main_content, SalePointDetailFragment.newInstance(arguments
                        ?: Bundle()), "SalePointDetailFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Điểm bán lẻ")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }

        toolbar.rightButton(R.drawable.icon_qr_code_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(SalePointDetailFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as SalePointDetailFragment
                shareFragment.openQRCode()
            }
        }
        if (UserDataManager.currentType == "Nhân viên gian hàng" && (dataProduct != null && UserDataManager.currentBoothId == dataProduct!!.booth?.id)) {
//            val listPermission = Const.listPermission
//            if (listPermission.isNotEmpty())
//                for (i in listPermission.indices)
//                    if (Const.Permission.EXPO_BOOTH_PRODUCTION_DIARY_ADD == listPermission[i]) {
//                        toolbar.rightButton2(R.drawable.ic_delete_highlight_24dp)
//                        toolbar.setRight2ButtonClickListener {
//                            val fragment = childFragmentManager.findFragmentByTag(SalePointDetailFragment.TAG)
//                            if (fragment != null) {
//                                val shareFragment = fragment as SalePointDetailFragment
//                                shareFragment.deleteProduct()
//                            }
//                        }
//                        break
//                    }
        } else if (dataProduct != null && UserDataManager.currentUserId == dataProduct!!.booth?.id) {
            toolbar.rightButton2(R.drawable.ic_delete_highlight_24dp)
            toolbar.setRight2ButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(SalePointDetailFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as SalePointDetailFragment
                    shareFragment.deleteProduct()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        dataProduct = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }
}