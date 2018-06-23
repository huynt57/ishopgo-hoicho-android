package ishopgo.com.exhibition.ui.main.salepointdetail

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_sale_point_detail.*

class SalePointDetailFragment : BaseFragment() {

    companion object {
        fun newInstance(params: Bundle): SalePointDetailFragment {
            val fragment = SalePointDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: SalePointDetailViewModel
    private val productsAdapter = ProductAdapter(0.4f)
    private var phone: String = ""
    private var productId: Long = 0
    private var dataProduct: ProductDetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sale_point_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            rv_product_sale_point.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            rv_product_sale_point.isNestedScrollingEnabled = false
            rv_product_sale_point.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        dataProduct = Toolbox.gson.fromJson(json, ProductDetail::class.java)
        phone = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE, "") ?: ""
    }

    private fun openActivtyLogin() {
        val intent = Intent(context, LoginSelectOptionActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
        startActivity(intent)
    }


    private fun showDetail(data: ManagerSalePointDetail) {
        if (data.salePoint != null) {
            val salePoint = data.salePoint
            tv_sale_point_name.text = salePoint?.name ?: ""
            tv_sale_point_phone.text = salePoint?.phone ?: ""
            tv_sale_point_address.text = salePoint?.address ?: ""
            linear_footer_call.setOnClickListener {
                val call = Uri.parse("tel:${salePoint?.phone}")
                val intent = Intent(Intent.ACTION_DIAL, call)
                if (intent.resolveActivity(it.context.packageManager) != null)
                    startActivity(intent)
            }

            val chatId = data.salePoint?.chatId ?: 0L
            val hasValidChatId = chatId != 0L
            linear_footer_message.setOnClickListener {
                if (UserDataManager.currentUserId <= 0) {
                    openActivtyLogin()
                    return@setOnClickListener
                }
                if (!hasValidChatId) {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.type = "text/plain"
                    intent.data = Uri.parse("smsto:${salePoint?.phone}")
                    dataProduct?.let {
                        intent.putExtra("sms_body", "Sản phẩm: ${it.provideProductName()}\n")
                    }
                    context?.let {
                        if (intent.resolveActivity(it.packageManager) != null)
                            it.startActivity(intent)
                    }
                } else {
                    val request = CreateConversationRequest()
                    request.type = 1
                    val members = mutableListOf<Long>()
                    members.add(UserDataManager.currentUserId)
                    members.add(chatId)
                    request.member = members
                    viewModel.createConversation(request)
                }
//                val call = Uri.parse("tel:${salePoint?.providePhone()}")
//                val intent = Intent(Intent.ACTION_DIAL, call)
//                if (intent.resolveActivity(it.context.packageManager) != null)
//                    startActivity(intent)
            }
        }

        if (data.products != null) {
            context?.let {
                val product = data.products!!
                product.data?.let { productsAdapter.replaceAll(it) }
                rv_product_sale_point.adapter = productsAdapter
                productsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                    override fun click(position: Int, data: Product, code: Int) {
                        context?.let {
                                productId = data.id

                                val productDetail = ProductDetail()
                                productDetail.image = data.image
                                productDetail.name = data.name
                                productDetail.price = data.price
                                productDetail.code = data.code
                                dataProduct = productDetail
                                viewModel.loadData(phone, productId)
                        }
                    }
                }
            }
        }

        if (dataProduct != null) {
            linear_product_current.visibility = View.VISIBLE
            Glide.with(context).load(dataProduct!!.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_product)
            tv_product.text = dataProduct!!.name
            tv_product_price.text = dataProduct!!.price.asMoney()
            tv_product_code.text = dataProduct!!.code

        } else linear_product_current.visibility = View.GONE

        tv_product_detail.setOnClickListener { openProductDetail(productId) }
    }

    private fun openProductDetail(productId: Long) {
        val intent = Intent(context, ProductDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
        startActivity(intent)
    }

    override
    fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(SalePointDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.conversation.observe(this, Observer { c ->
            c?.let {
                val conv = LocalConversationItem()
                conv.idConversions = c.id ?: ""
                conv.name = c.name ?: ""

                context?.let {
                    val intent = Intent(it, ConversationActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conv.idConversions)
                    intent.putExtra(Const.TransferKey.EXTRA_TITLE, conv.name)
                    startActivity(intent)
                }
            }
        })

        viewModel.getData.observe(this, Observer { p ->
            p.let {
                it?.let { it1 -> showDetail(it1) }
            }
        })

        if (dataProduct != null) {
            productId = dataProduct!!.id
        }

        viewModel.loadData(phone, productId)
    }
}