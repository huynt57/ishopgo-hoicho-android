package ishopgo.com.exhibition.ui.main.shop.info

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ShopDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.home.search.sale_point.SearchSalePointProvider
import ishopgo.com.exhibition.ui.main.salepoint.add.SalePointAddActivity
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailViewModel
import kotlinx.android.synthetic.main.fragment_shop_info.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopInfoFragment : BaseFragment() {

    private lateinit var viewModel: ShopInfoViewModel
    private lateinit var sharedViewModel: ShopDetailViewModel
    private val salePointAdapter = SalePointAdapter()
    private var shopId = -1L

    companion object {
        fun newInstance(params: Bundle): ShopInfoFragment {
            val f = ShopInfoFragment()
            f.arguments = params

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = obtainViewModel(ShopDetailViewModel::class.java, true)
        sharedViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        sharedViewModel.editSusscess.observe(this, Observer {
            viewModel.loadInfo(shopId)
            toast("Cập nhật thành công")
        })

        viewModel = obtainViewModel(ShopInfoViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.info.observe(this, Observer { i ->
            i?.let {
                showInfo(it)
            }
        })

        viewModel.listSalePoint.observe(this, Observer { i ->
            i?.let {
                salePointAdapter.replaceAll(it)
            }
        })

        viewModel.loadInfo(shopId)
    }

    @SuppressLint("SetTextI18n")
    private fun showInfo(info: ShopInfoProvider) {

        view_name.text = "Chủ gian hàng: <b>${info.provideName()}</b>".asHtml()
        view_product_count.text = "Số sản phẩm: <b>${info.provideProductCount()}</b>".asHtml()
        view_joined_date.text = "Ngày tham gia: <b>${info.provideJoinedDate()}</b>".asHtml()
        view_region.text = "Khu vực: <b>${info.provideRegion()}</b>".asHtml()
        view_rating.text = "Đánh giá shop: <b>${info.provideRating()}/5 điểm</b>".asHtml()
        view_click_count.text = "Số lượt click: <b>${info.provideClickCount()}</b>".asHtml()
        view_share_count.text = "Số lượt share: <b>${info.provideShareCount()}</b>".asHtml()
        view_description.text = info.provideDescription().asHtml()

        if (info is ShopDetail) {
            sharedViewModel.updateShopImage(info.id, info.follow, info.provideImage())
            if (UserDataManager.currentUserId == info.id) {
                view_name.drawableCompat(0, 0, R.drawable.ic_edit_black_24dp, 0)
                view_name.setOnClickListener { showDialogChangeName(info.name ?: "") }
                textView11.drawableCompat(0, 0, R.drawable.ic_edit_black_24dp, 0)
                textView11.setOnClickListener {
                    showDialogChangeDescription(info.introduction ?: "")
                }
                img_add_sale_point.visibility = View.VISIBLE
                img_add_sale_point.setOnClickListener {
                    val intent = Intent(context, SalePointAddActivity::class.java)
                    startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
                }
            }
        }
    }

    private fun showDialogChangeName(name: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thay đổi tên gian hàng")
                    .customView(R.layout.dialog_change_shop_name, false)
                    .positiveText("Thay đổi")
                    .onPositive { dialog, which ->
                        val edit_shop_name = dialog.findViewById(R.id.edit_shop_name) as TextInputEditText
                        if (checkRequireFieldsName(edit_shop_name)) {
                            sharedViewModel.editConfigBooth(edit_shop_name.text.toString(), "", "")
                            dialog.dismiss()
                        }
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_shop_name = dialog.findViewById(R.id.edit_shop_name) as TextInputEditText
            edit_shop_name.setText(name)

            dialog.show()
        }
    }

    private fun showDialogChangeDescription(description: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thay đổi mô tả gian hàng")
                    .customView(R.layout.dialog_change_shop_description, false)
                    .positiveText("Thay đổi")
                    .onPositive { dialog, which ->
                        val edit_shop_description = dialog.findViewById(R.id.edit_shop_description) as TextInputEditText
                        if (checkRequireFieldsDescription(edit_shop_description)) {
                            sharedViewModel.editConfigBooth("", edit_shop_description.text.toString(), "")
                            dialog.dismiss()
                        }
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_shop_description = dialog.findViewById(R.id.edit_shop_description) as TextInputEditText
            edit_shop_description.setText(description)

            dialog.show()
        }
    }

    private fun checkRequireFieldsName(view: TextInputEditText): Boolean {
        if (view.text.trim().isEmpty()) {
            toast("Tên gian hàng không được để trống")
            view.error = "Trường này còn trống"
            return false
        }
        return true
    }

    private fun checkRequireFieldsDescription(view: TextInputEditText): Boolean {
        if (view.text.trim().isEmpty()) {
            toast("Mô tả gian hàng không được để trống")
            view.error = "Trường này còn trống"
            return false
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.adapter = salePointAdapter
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false

        salePointAdapter.listener = object : ClickableAdapter.BaseAdapterAction<SearchSalePointProvider> {
            override fun click(position: Int, data: SearchSalePointProvider, code: Int) {
                context?.let {
                    if (data is SearchSalePoint) {
                        val intent = Intent(it, SalePointDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.phone)
                        startActivity(intent)
                    }
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == RESULT_OK)
            viewModel.loadInfo(shopId)
    }
}