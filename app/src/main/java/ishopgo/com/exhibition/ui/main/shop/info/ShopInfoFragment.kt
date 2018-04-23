package ishopgo.com.exhibition.ui.main.shop.info

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
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

        viewModel = obtainViewModel(ShopInfoViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.info.observe(this, Observer { i ->
            i?.let {
                showInfo(it)
            }
        })

        viewModel.loadInfo(shopId)
    }

    private fun showInfo(info: ShopInfoProvider) {
        sharedViewModel.updateShopImage(info.provideImage())

        view_name.text = "Tên: <b>${info.provideName()}</b>".asHtml()
        view_pic.text = "Người phụ trách: <b>${info.providePIC()}</b>".asHtml()
        view_product_count.text = "Số sản phẩm: <b>${info.provideProductCount()} sản phẩm</b>".asHtml()
        view_joined_date.text = "Ngày tham gia: <b>${info.provideJoinedDate()}</b>".asHtml()
        view_region.text = "Khu vực: <b>${info.provideRegion()}</b>".asHtml()
        view_rating.text = "Đánh giá shop: <b>${info.provideRating()}/5 điểm</b>".asHtml()
        view_click_count.text = "Số lượt click: <b>${info.provideClickCount()} lượt</b>".asHtml()
        view_share_count.text = "Số lượt share: <b>${info.provideShareCount()} lượt</b>".asHtml()
        view_description.text = info.provideDescription()

        salePointAdapter.replaceAll(info.provideSalePoints())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.adapter = salePointAdapter
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false


    }
}