package ishopgo.com.exhibition.ui.main.shop.info.list_sale_point_all

import android.content.Intent
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.salepoint.add.SalePointAddActivity
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ListSalePointFragmentActionBar : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    companion object {

        fun newInstance(params: Bundle): ListSalePointFragmentActionBar {
            val fragment = ListSalePointFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private var boothId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boothId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Danh sách điểm bán của gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentUserId == boothId) {
            toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
            toolbar.setRightButtonClickListener {
                val intent = Intent(context, SalePointAddActivity::class.java)
                startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
            }
        }

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ListSalePointFragment.newInstance(arguments
                        ?: Bundle())).commit()
    }
}