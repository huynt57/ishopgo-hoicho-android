package ishopgo.com.exhibition.ui.main.product.icheckproduct.shop

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
import kotlinx.android.synthetic.main.content_icheck_shop_info.*

class IcheckShopInfoFragment : BaseFragment() {
    private lateinit var viewModel: IcheckProductViewModel
    private var shopId = 0L

    companion object {
        const val TAG = "IcheckShopInfoFragment"
        fun newInstance(params: Bundle): IcheckShopInfoFragment {
            val fragment = IcheckShopInfoFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_icheck_shop_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(IcheckProductViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataShopInfo.observe(this, Observer { i ->
            i?.let {
                ten_gian_hang.text = "Tên: <b>${it.name ?: ""}</b>".asHtml()
                hotline.setPhone("Điện thoại: <b>${it.phone ?: ""}</b>".asHtml(), it.phone ?: "")
                so_san_pham.text = "Số sản phẩm: <b>${it.productCount ?: 0}</b>".asHtml()
                dia_chi.text =  "Địa chỉ: <b>${it.address ?: ""}</b>".asHtml()
                danh_gia.text = "Đánh giá: <b>${it.star ?: 0.0F}</b>".asHtml()
                luot_quan_tam.text = "Số lượt quét: <b>${it.scanCount ?: 0}<b>".asHtml()
            }
        })
        val request = String.format("https://core.icheck.com.vn/vendors/%s", shopId)
        viewModel.loadIcheckShopInfo(request)
    }
}