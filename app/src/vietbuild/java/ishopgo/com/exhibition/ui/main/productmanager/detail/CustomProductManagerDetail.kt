package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.view.View
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail

class CustomProductManagerDetail : ProductManagerDetailOverwrite() {
    private var viewModel: ProductManagerViewModel? = null
    private var product_Id: Long = 0L

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

    override fun handleViewCreated(rootView: View, context: Context) {
        rootView.apply {
            rv_product_related_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        }
    }

    override fun handleStartEdit(rootView: View) {
        rootView.apply {
            edit_product_scale.isFocusable = true
            edit_product_scale.isFocusableInTouchMode = true
            edit_product_quantity.isFocusable = true
            edit_product_quantity.isFocusableInTouchMode = true

            loadSanPhamLienQuan(this)
        }
    }

    override fun handleEndEdit(rootView: View) {
        rootView.apply {
            edit_product_scale.isFocusable = false
            edit_product_scale.isFocusableInTouchMode = false
            edit_product_quantity.isFocusable = false
            edit_product_quantity.isFocusableInTouchMode = false
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
        }
    }

    interface ProductManagerDetailProvider {
        fun providerScale(): String
        fun providerQuantity(): String
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
            }
        }
    }

    private var adapterProductRelatedImage = ProductManagerRelatedCollapseAdapters()
    private var listProductRelated = ArrayList<ProductManager>()
    private var adapterDialogProduct = ProductManagerRelatedAdapter()

    private fun loadSanPhamLienQuan(rootView: View) {
        rootView.apply {
            context?.let {
                rv_product_related_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rv_product_related_products.adapter = adapterProductRelatedImage
                adapterProductRelatedImage.listener = object : ClickableAdapter.BaseAdapterAction<ProductManager> {
                    override fun click(position: Int, data: ProductManager, code: Int) {
                        listProductRelated.remove(data)
                        adapterProductRelatedImage.replaceAll(listProductRelated)
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
                            adapterDialogProduct.listener = object : ClickableAdapter.BaseAdapterAction<ProductManager> {
                                override fun click(position: Int, data: ProductManager, code: Int) {
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