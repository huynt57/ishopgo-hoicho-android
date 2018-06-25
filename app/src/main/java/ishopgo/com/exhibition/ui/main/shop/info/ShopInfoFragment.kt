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
import ishopgo.com.exhibition.model.SalePoint
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
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
    private fun showInfo(info: ShopDetail) {
        val convert = ShopInfoConverter().convert(info)

        view_name.text = "Chủ gian hàng: <b>${convert.provideName()}</b>".asHtml()
        view_phone.text = "SĐT: <b>${convert.provideHotline()}</b>".asHtml()
        view_product_count.text = "Số sản phẩm: <b>${convert.provideProductCount()}</b>".asHtml()
        view_joined_date.text = "Ngày tham gia: <b>${convert.provideJoinedDate()}</b>".asHtml()
        view_region.text = "Khu vực: <b>${convert.provideRegion()}</b>".asHtml()
        view_rating.text = "Đánh giá shop: <b>${convert.provideRating()}/5 điểm</b>".asHtml()
        view_click_count.text = "Số lượt click: <b>${convert.provideClickCount()}</b>".asHtml()
        view_share_count.text = "Số lượt share: <b>${convert.provideShareCount()}</b>".asHtml()
        view_follow_count.text = "Số lượt quan tâm: <b>${convert.provideFollowCount()}</b>".asHtml()
        view_visit_count.text = "Số lượt tham quan: <b>${convert.provideVisitCount()}</b>".asHtml()
        view_description.text = convert.provideDescription().asHtml()

        sharedViewModel.updateShopImage(info.id, info.follow, convert.provideImage())
        if (UserDataManager.currentUserId == info.id) {
            view_name.drawableCompat(0, 0, R.drawable.ic_edit_default_24dp, 0)
            view_name.setOnClickListener { showDialogChangeName(info.name ?: "") }
            textView11.drawableCompat(0, 0, R.drawable.ic_edit_default_24dp, 0)
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

    interface ShopInfoProvider {
        fun provideName(): String
        fun provideHotline(): String
        fun provideImage(): String
        fun provideProductCount(): Int
        fun provideJoinedDate(): String
        fun provideRegion(): String
        fun provideRating(): Int
        fun provideClickCount(): Int
        fun provideShareCount(): Int
        fun provideFollowCount(): Int
        fun provideVisitCount(): Int
        fun provideDescription(): String
        fun provideSalePoints(): List<SearchSalePoint>
    }

    class ShopInfoConverter : Converter<ShopDetail, ShopInfoProvider> {

        override fun convert(from: ShopDetail): ShopInfoProvider {
            return object : ShopInfoProvider {
                override fun provideVisitCount(): Int {
                    return from.visitCount ?: 0
                }

                override fun provideFollowCount(): Int {
                    return from.followCount ?: 0
                }

                override fun provideHotline(): String {
                    return from.hotline ?: ""
                }

                override fun provideImage(): String {
                    return from.banner ?: ""
                }

                override fun provideProductCount(): Int {
                    return from.productCount
                }

                override fun provideJoinedDate(): String {
                    return if (from.createdAt.isNullOrBlank()) "01/05/2018" else from.createdAt!!
                }

                override fun provideRegion(): String {
                    return if (from.address.isNullOrBlank()) "Đang cập nhật" else from.address!!
                }

                override fun provideRating(): Int {
                    return from.rate
                }

                override fun provideClickCount(): Int {
                    return from.clickCount
                }

                override fun provideShareCount(): Int {
                    return from.shareCount
                }

                override fun provideDescription(): String {
                    return if (from.introduction.isNullOrBlank()) "Đang cập nhật" else from.introduction!!
                }

                override fun provideSalePoints(): List<SearchSalePoint> {
                    return mutableListOf()
                }

                override fun provideName(): String {
                    return from.name ?: "Đang cập nhật"
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

        salePointAdapter.listener = object : ClickableAdapter.BaseAdapterAction<SearchSalePoint> {
            override fun click(position: Int, data: SearchSalePoint, code: Int) {
                context?.let {
                    val intent = Intent(it, SalePointDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.phone)
                    startActivity(intent)
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