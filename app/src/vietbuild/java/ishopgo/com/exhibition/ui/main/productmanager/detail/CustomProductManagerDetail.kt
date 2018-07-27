package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductManagerRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.InfoProduct
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.ProductManagerRelatedAdapter
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointProductAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_product_manager_detail.view.*

class CustomProductManagerDetail : ProductManagerDetailOverwrite() {

    private var viewModel: ProductManagerViewModel? = null
    private var product_Id: Long = 0L
    private var adapterProductRelatedImage = SalePointProductAdapter(0.4f)
    private var listProductRelated = ArrayList<Product>()
    private var adapterDialogProduct = ProductManagerRelatedAdapter()

    companion object {
        const val DELETE_PRODUCT = 1
    }

    override fun handleActivityCreated(viewModel: ProductManagerViewModel, fragment: BaseFragment) {
        this.viewModel = viewModel

        viewModel.dataReturned.observe(fragment, Observer { p ->
            p?.let {
                if (reloadData) {
                    adapterDialogProduct.replaceAll(it)
                    fragment.hideProgressDialog()
                } else {
                    adapterDialogProduct.addAll(it)
                }
            }
        })
    }

    override fun handleOnCreate(productId: Long) {
        product_Id = productId
    }

    override fun handleViewCreated(rootView: View, context: Context, listProductRelated: ArrayList<Product>, listVatTu: ArrayList<Product>, listGiaiPhap: ArrayList<Product>) {
        rootView.apply {
            rv_product_related_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        }
        this.listProductRelated = listProductRelated
    }

    override fun handleStartEdit(rootView: View) {
        rootView.apply {
            edit_product_scale.isFocusable = true
            edit_product_scale.isFocusableInTouchMode = true
            edit_product_quantity.isFocusable = true
            edit_product_quantity.isFocusableInTouchMode = true
            img_add_related_product.visibility = View.VISIBLE

            loadSanPhamLienQuan(this)
        }
    }

    override fun handleEndEdit(rootView: View) {
        rootView.apply {
            edit_product_scale.isFocusable = false
            edit_product_scale.isFocusableInTouchMode = false
            edit_product_quantity.isFocusable = false
            edit_product_quantity.isFocusableInTouchMode = false
            img_add_related_product.visibility = View.GONE
        }
    }

    override fun handleInOtherFlavor(rootView: View, detail: ProductManagerDetail, fragment: BaseFragment) {
        rootView.apply {
            val convert = ProductManagerConverter().convert(detail)

            edit_product_scale.setText(convert.providerScale())
            edit_product_quantity.setText(convert.providerQuantity())

            img_add_related_product.setOnClickListener {
                performSearchingProduct(rootView, fragment)
            }

            if (convert.providerInfo().isNotEmpty()) {
                val listInfo = convert.providerInfo()
                for (i in listInfo.indices) {
                    if (listInfo[i].name == "Sản phẩm liên quan") {
                        val productsRelatedAdapter = ProductAdapter(0.4f)
                        if (listInfo[i].products?.data?.isNotEmpty() == true) {
                            listInfo[i].products?.data?.let {
                                productsRelatedAdapter.replaceAll(it)
                                listProductRelated.addAll(it)
                                adapterProductRelatedImage.replaceAll(listProductRelated)
                            }

                            rv_product_related_products.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                            rv_product_related_products.adapter = productsRelatedAdapter
                            rv_product_related_products.isNestedScrollingEnabled = false
                            rv_product_related_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))


                        }
                        break
                    }
                }
            }
        }
    }

    interface ProductManagerDetailProvider {
        fun providerScale(): String
        fun providerQuantity(): String
        fun providerInfo(): List<InfoProduct>
    }

    class ProductManagerConverter : Converter<ProductManagerDetail, ProductManagerDetailProvider> {

        override fun convert(from: ProductManagerDetail): ProductManagerDetailProvider {
            return object : ProductManagerDetailProvider {
                override fun providerScale(): String {
                    return from.quyMo ?: ""
                }

                override fun providerQuantity(): String {
                    return from.sanLuong ?: ""
                }

                override fun providerInfo(): List<InfoProduct> {
                    return from.info ?: mutableListOf()
                }
            }
        }
    }

    private fun loadSanPhamLienQuan(rootView: View) {
        rootView.apply {
            context?.let {
                rv_product_related_products.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                rv_product_related_products.adapter = adapterProductRelatedImage
                adapterProductRelatedImage.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                    override fun click(position: Int, data: Product, code: Int) {
                        when (code) {
                            DELETE_PRODUCT -> {
                                listProductRelated.remove(data)
                                adapterProductRelatedImage.replaceAll(listProductRelated)
                            }
                        }
                    }
                }
            }
        }
    }

    private var keyWord: String = ""
    private var code: String = ""
    private var reloadData = true

    private fun firstLoadProductRelated() {
        if (viewModel != null) {
            reloadData = true
            val firstLoad = ProductManagerRequest()
            firstLoad.limit = Const.PAGE_LIMIT
            firstLoad.offset = 0
            firstLoad.name = keyWord
            firstLoad.code = code
            firstLoad.productId = product_Id
            viewModel!!.loadData(firstLoad)
        }
    }

    private fun loadMoreProductRelated(currentCount: Int) {
        if (viewModel != null) {
            reloadData = false
            val loadMore = ProductManagerRequest()
            loadMore.limit = Const.PAGE_LIMIT
            loadMore.offset = currentCount
            loadMore.name = keyWord
            loadMore.code = code
            loadMore.productId = product_Id
            viewModel!!.loadData(loadMore)
        }
    }

    private fun performSearchingProduct(rootView: View, fragment: BaseFragment) {
        rootView.apply {
            reloadData = true
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .title("Tìm kiếm")
                        .customView(R.layout.dialog_search_product_related, false)
                        .positiveText("Tìm")
                        .onPositive({ dialog, _ ->
                            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
                            val edt_search_name = dialog.findViewById(R.id.edt_search_name) as TextInputEditText
                            val edt_search_code = dialog.findViewById(R.id.edt_search_code) as TextInputEditText
                            keyWord = edt_search_name.text.toString().trim { it <= ' ' }
                            code = edt_search_code.text.toString().trim { it <= ' ' }
                            firstLoadProductRelated()

                            val layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rv_search.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rv_search.adapter = adapterDialogProduct
                            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager2) {
                                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                                    loadMoreProductRelated(totalItemsCount)
                                }
                            })
                            adapterDialogProduct.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                                override fun click(position: Int, data: Product, code: Int) {
                                    if (listProductRelated.size == 0) {
                                        listProductRelated.add(data)

                                        adapterProductRelatedImage.replaceAll(listProductRelated)
                                    } else {
                                        val isContained = listProductRelated.any {
                                            if (it is IdentityData && data is IdentityData)
                                                return@any it.id == data.id
                                            return@any false
                                        }

                                        if (isContained) {
                                            fragment.toast("Sản phẩm liên quan đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                                            return
                                        } else {
                                            listProductRelated.add(data)
                                            adapterProductRelatedImage.replaceAll(listProductRelated)
                                        }
                                    }
                                    dialog.dismiss()
                                }
                            }
                            reloadData = true
                        })
                        .negativeText("Huỷ")
                        .onNegative { dialog, _ -> dialog.dismiss() }
                        .autoDismiss(false)
                        .canceledOnTouchOutside(false)
                        .build()

                dialog.show()
            }
        }
    }
}