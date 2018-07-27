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
    private var adapterProductVatTu = SalePointProductAdapter(0.4f)
    private var adapterProductGiaiPhap = SalePointProductAdapter(0.4f)
    private var listProductRelated = ArrayList<Product>()
    private var listProductVatTu = ArrayList<Product>()
    private var listProductGiaiPhap = ArrayList<Product>()
    private var adapterDialogProduct = ProductManagerRelatedAdapter()
    private var adapterDialogVatTu = ProductManagerRelatedAdapter()
    private var adapterDialogGiaiPhap = ProductManagerRelatedAdapter()

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

        viewModel.dataVatTu.observe(fragment, Observer { p ->
            p?.let {
                if (reloadData) {
                    adapterDialogVatTu.replaceAll(it)
                    fragment.hideProgressDialog()
                } else {
                    adapterDialogVatTu.addAll(it)
                }
            }
        })

        viewModel.dataGiaiPhap.observe(fragment, Observer { p ->
            p?.let {
                if (reloadData) {
                    adapterDialogGiaiPhap.replaceAll(it)
                    fragment.hideProgressDialog()
                } else {
                    adapterDialogGiaiPhap.addAll(it)
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
            rv_supplies_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
            rv_solution_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
            img_add_solution_product.visibility = View.GONE
            img_add_supplies_product.visibility = View.GONE
        }
        this.listProductRelated = listProductRelated
        this.listProductVatTu = listVatTu
        this.listProductGiaiPhap = listGiaiPhap
    }

    companion object {
        const val DELETE_PRODUCT = 1
    }

    override fun handleStartEdit(rootView: View) {
        rootView.apply {
            edit_product_agri_scale.isFocusable = true
            edit_product_agri_scale.isFocusableInTouchMode = true
            edit_product_agri_quantity.isFocusable = true
            edit_product_agri_quantity.isFocusableInTouchMode = true
            edit_product_agri_expiryDate.isFocusable = true
            edit_product_agri_expiryDate.isFocusableInTouchMode = true
            edit_product_agri_pack.isFocusable = true
            edit_product_agri_pack.isFocusableInTouchMode = true
            edit_product_agri_season.isFocusable = true
            edit_product_agri_season.isFocusableInTouchMode = true
            edit_product_agri_shipmentCode.isFocusable = true
            edit_product_agri_shipmentCode.isFocusableInTouchMode = true
            edit_product_agri_manufacturingDate.isFocusable = true
            edit_product_agri_manufacturingDate.isFocusableInTouchMode = true
            edit_product_agri_harvestDate.isFocusable = true
            edit_product_agri_harvestDate.isFocusableInTouchMode = true
            edit_product_agri_shippedDate.isFocusable = true
            edit_product_agri_shippedDate.isFocusableInTouchMode = true
            sw_show_accreditation.isClickable = true
            sw_show_nksx.isClickable = true

            img_add_solution_product.visibility = View.VISIBLE
            img_add_supplies_product.visibility = View.VISIBLE

            loadSanPhamLienQuan(this)
            loadVatTu(this)
            loadGiaiPhap(this)
        }
    }

    override fun handleEndEdit(rootView: View) {
        rootView.apply {
            edit_product_agri_scale.isFocusable = false
            edit_product_agri_scale.isFocusableInTouchMode = false
            edit_product_agri_quantity.isFocusable = false
            edit_product_agri_quantity.isFocusableInTouchMode = false
            edit_product_agri_expiryDate.isFocusable = false
            edit_product_agri_expiryDate.isFocusableInTouchMode = false
            edit_product_agri_pack.isFocusable = false
            edit_product_agri_pack.isFocusableInTouchMode = false
            edit_product_agri_season.isFocusable = false
            edit_product_agri_season.isFocusableInTouchMode = false
            edit_product_agri_shipmentCode.isFocusable = false
            edit_product_agri_shipmentCode.isFocusableInTouchMode = false
            edit_product_agri_manufacturingDate.isFocusable = false
            edit_product_agri_manufacturingDate.isFocusableInTouchMode = false
            edit_product_agri_harvestDate.isFocusable = false
            edit_product_agri_harvestDate.isFocusableInTouchMode = false
            edit_product_agri_shippedDate.isFocusable = false
            edit_product_agri_shippedDate.isFocusableInTouchMode = false
            sw_show_accreditation.isClickable = false
            sw_show_nksx.isClickable = false

            img_add_solution_product.visibility = View.GONE
            img_add_supplies_product.visibility = View.GONE
        }
    }

    override fun handleInOtherFlavor(rootView: View, detail: ProductManagerDetail, fragment: BaseFragment) {
        rootView.apply {
            //            val linearLayout = view_linear as LinearLayout
            linear_agri.visibility = View.VISIBLE
            linear_scale.visibility = View.GONE
            linear_agri_product.visibility = View.VISIBLE
            val convert = ProductManagerConverter().convert(detail)
            edit_product_agri_scale.setText(convert.providerScale())
            edit_product_agri_quantity.setText(convert.providerQuantity())
            edit_product_agri_expiryDate.setText(convert.providerHsd())
            edit_product_agri_pack.setText(convert.providerDongGoi())
            edit_product_agri_season.setText(convert.providerMuaVu())
            edit_product_agri_shipmentCode.setText(convert.providerMsLohang())
            edit_product_agri_manufacturingDate.setText(convert.providerNgaySx())
            edit_product_agri_harvestDate.setText(convert.providerDkThuhoach())
            edit_product_agri_shippedDate.setText(convert.providerXuatXuong())

            sw_show_nksx.isChecked = convert.providerIsNhatkySx()
            sw_show_nksx.text = if (convert.providerIsNhatkySx()) "Nhật ký sản xuất: Bật"
            else "Nhật ký sản xuất: Tắt"

            sw_show_accreditation.isChecked = convert.providerIsBaoTieu()
            sw_show_accreditation.text = if (convert.providerIsBaoTieu()) "Đã được bao tiêu: Đã được bao tiêu"
            else "Đã được bao tiêu: Chưa được bao tiêu"

            img_add_related_product.setOnClickListener {
                performSearchingRelated(rootView, fragment)
            }

            img_add_solution_product.setOnClickListener {
                performSearchingGiaiPhap(rootView, fragment)
            }

            img_add_supplies_product.setOnClickListener {
                performSearchingVatTu(rootView, fragment)
            }

            if (convert.providerInfo().isNotEmpty()) {
                val listInfo = convert.providerInfo()
                for (i in listInfo.indices) {
                    if (listInfo[i].name == "Vật tư được sử dụng") {
                        val productsRelatedAdapter = ProductAdapter(0.4f)
                        if (listInfo[i].products?.data?.isNotEmpty() == true) {
                            listInfo[i].products?.data?.let {
                                productsRelatedAdapter.replaceAll(it)
                                listProductVatTu.addAll(it)
                                adapterProductVatTu.replaceAll(listProductVatTu)
                            }

                            rv_supplies_products.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                            rv_supplies_products.adapter = productsRelatedAdapter
                            rv_supplies_products.isNestedScrollingEnabled = false
                            rv_supplies_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
                        }
                    }

                    if (listInfo[i].name == "Giải pháp được sử dụng") {
                        val productsRelatedAdapter = ProductAdapter(0.4f)
                        if (listInfo[i].products?.data?.isNotEmpty() == true) {
                            listInfo[i].products?.data?.let {
                                productsRelatedAdapter.replaceAll(it)
                                listProductGiaiPhap.addAll(it)
                                adapterProductGiaiPhap.replaceAll(listProductGiaiPhap)
                            }

                            rv_solution_products.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                            rv_solution_products.adapter = productsRelatedAdapter
                            rv_solution_products.isNestedScrollingEnabled = false
                            rv_solution_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
                        }
                    }

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
                    }
                }
            }
//            if (convert.providerInfo().isNotEmpty()) {
//                var count = 1
//                val listInfo = convert.providerInfo()
//                for (i in listInfo.indices) {
//                    val productInfo = ProductDetailWidget(rootView.context)
//                    productInfo.apply {
//                        label_products.text = listInfo[i].name
//                        val adapter = ProductAdapter(0.4f)
//                        if (listInfo[i].products?.data?.isNotEmpty() == true) {
//                            listInfo[i].products?.data?.let { adapter.replaceAll(it) }
//
//                            rv_product_related_products.adapter = adapter
//                            val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
//                            layoutManager.isAutoMeasureEnabled = true
//                            rv_product_related_products.layoutManager = layoutManager
//                            rv_product_related_products.isNestedScrollingEnabled = false
//                            rv_product_related_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
//
//                        }
//                        val rvProduct = linearLayout.findViewById<View>(R.id.rv_product_images)
//                        val indexOfChild = linearLayout.indexOfChild(rvProduct)
//                        linearLayout.addView(productInfo, indexOfChild + count)
//                        count += 1
//                    }
//                }
//
//            }
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

    private fun loadVatTu(rootView: View) {
        rootView.apply {
            context?.let {
                rv_supplies_products.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                rv_supplies_products.adapter = adapterProductVatTu
                adapterProductVatTu.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                    override fun click(position: Int, data: Product, code: Int) {
                        when (code) {
                            DELETE_PRODUCT -> {
                                listProductVatTu.remove(data)
                                adapterProductVatTu.replaceAll(listProductVatTu)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadGiaiPhap(rootView: View) {
        rootView.apply {
            context?.let {
                rv_solution_products.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                rv_solution_products.adapter = adapterProductGiaiPhap
                adapterProductGiaiPhap.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                    override fun click(position: Int, data: Product, code: Int) {
                        when (code) {
                            DELETE_PRODUCT -> {
                                listProductGiaiPhap.remove(data)
                                adapterProductGiaiPhap.replaceAll(listProductGiaiPhap)
                            }
                        }
                    }
                }
            }
        }
    }

    private var keyWordGiaiPhap: String = ""
    private var codeGiaiPhap: String = ""
    private var reloadGiaiPhap = true

    private fun firstLoadGiaiPhap() {
        if (viewModel != null) {
            reloadGiaiPhap = true
            val firstLoad = ProductManagerRequest()
            firstLoad.limit = Const.PAGE_LIMIT
            firstLoad.offset = 0
            firstLoad.name = keyWordGiaiPhap
            firstLoad.code = codeGiaiPhap
            firstLoad.productId = product_Id
            viewModel!!.loadDataGiaiPhap(firstLoad)
        }
    }

    private fun loadMoreGiaiPhap(currentCount: Int) {
        if (viewModel != null) {
            reloadGiaiPhap = false
            val loadMore = ProductManagerRequest()
            loadMore.limit = Const.PAGE_LIMIT
            loadMore.offset = currentCount
            loadMore.name = keyWordGiaiPhap
            loadMore.code = codeGiaiPhap
            loadMore.productId = product_Id
            viewModel!!.loadDataGiaiPhap(loadMore)
        }
    }

    private var keyWordVaTu: String = ""
    private var codeVatTu: String = ""
    private var reloadVatTu = true

    private fun firstLoadVatTu() {
        if (viewModel != null) {
            reloadVatTu = true
            val firstLoad = ProductManagerRequest()
            firstLoad.limit = Const.PAGE_LIMIT
            firstLoad.offset = 0
            firstLoad.name = keyWordVaTu
            firstLoad.code = codeVatTu
            firstLoad.productId = product_Id
            viewModel!!.loadDataVatTu(firstLoad)
        }
    }

    private fun loadMoreVatTu(currentCount: Int) {
        if (viewModel != null) {
            reloadVatTu = false
            val loadMore = ProductManagerRequest()
            loadMore.limit = Const.PAGE_LIMIT
            loadMore.offset = currentCount
            loadMore.name = keyWordVaTu
            loadMore.code = codeVatTu
            loadMore.productId = product_Id
            viewModel!!.loadDataVatTu(loadMore)
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

    private fun performSearchingRelated(rootView: View, fragment: BaseFragment) {
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

    private fun performSearchingVatTu(rootView: View, fragment: BaseFragment) {
        rootView.apply {
            reloadVatTu = true
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .title("Tìm kiếm")
                        .customView(R.layout.dialog_search_product_related, false)
                        .positiveText("Tìm")
                        .onPositive({ dialog, _ ->
                            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
                            val edt_search_name = dialog.findViewById(R.id.edt_search_name) as TextInputEditText
                            val edt_search_code = dialog.findViewById(R.id.edt_search_code) as TextInputEditText
                            keyWordVaTu = edt_search_name.text.toString().trim { it <= ' ' }
                            codeVatTu = edt_search_code.text.toString().trim { it <= ' ' }
                            firstLoadVatTu()

                            val layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rv_search.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rv_search.adapter = adapterDialogVatTu
                            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager2) {
                                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                                    loadMoreVatTu(totalItemsCount)
                                }
                            })
                            adapterDialogVatTu.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                                override fun click(position: Int, data: Product, code: Int) {
                                    if (listProductVatTu.size == 0) {
                                        listProductVatTu.add(data)

                                        adapterProductVatTu.replaceAll(listProductVatTu)
                                    } else {
                                        val isContained = listProductVatTu.any {
                                            if (it is IdentityData && data is IdentityData)
                                                return@any it.id == data.id
                                            return@any false
                                        }

                                        if (isContained) {
                                            fragment.toast("Sản phẩm liên quan đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                                            return
                                        } else {
                                            listProductVatTu.add(data)
                                            adapterProductVatTu.replaceAll(listProductVatTu)
                                        }
                                    }
                                    dialog.dismiss()
                                }
                            }
                            reloadVatTu = true
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

    private fun performSearchingGiaiPhap(rootView: View, fragment: BaseFragment) {
        rootView.apply {
            reloadGiaiPhap = true
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .title("Tìm kiếm")
                        .customView(R.layout.dialog_search_product_related, false)
                        .positiveText("Tìm")
                        .onPositive({ dialog, _ ->
                            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
                            val edt_search_name = dialog.findViewById(R.id.edt_search_name) as TextInputEditText
                            val edt_search_code = dialog.findViewById(R.id.edt_search_code) as TextInputEditText
                            keyWordGiaiPhap = edt_search_name.text.toString().trim { it <= ' ' }
                            codeGiaiPhap = edt_search_code.text.toString().trim { it <= ' ' }
                            firstLoadGiaiPhap()

                            val layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rv_search.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rv_search.adapter = adapterDialogGiaiPhap
                            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager2) {
                                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                                    loadMoreGiaiPhap(totalItemsCount)
                                }
                            })
                            adapterDialogGiaiPhap.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                                override fun click(position: Int, data: Product, code: Int) {
                                    if (listProductGiaiPhap.size == 0) {
                                        listProductGiaiPhap.add(data)

                                        adapterProductGiaiPhap.replaceAll(listProductGiaiPhap)
                                    } else {
                                        val isContained = listProductGiaiPhap.any {
                                            if (it is IdentityData && data is IdentityData)
                                                return@any it.id == data.id
                                            return@any false
                                        }

                                        if (isContained) {
                                            fragment.toast("Sản phẩm liên quan đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                                            return
                                        } else {
                                            listProductGiaiPhap.add(data)
                                            adapterProductGiaiPhap.replaceAll(listProductGiaiPhap)
                                        }
                                    }
                                    dialog.dismiss()
                                }
                            }
                            reloadGiaiPhap = true
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


    interface ProductManagerDetailProvider {
        fun providerScale(): String
        fun providerQuantity(): String
        fun providerIsNhatkySx(): Boolean
        fun providerIsBaoTieu(): Boolean
        fun providerNgaySx(): String
        fun providerDkThuhoach(): String
        fun providerMsLohang(): String
        fun providerMuaVu(): String
        fun providerDongGoi(): String
        fun providerHsd(): String
        fun providerXuatXuong(): String
        fun providerInfo(): List<InfoProduct>
    }

    class ProductManagerConverter : Converter<ProductManagerDetail, ProductManagerDetailProvider> {

        override fun convert(from: ProductManagerDetail): ProductManagerDetailProvider {
            return object : ProductManagerDetailProvider {
                override fun providerIsNhatkySx(): Boolean {
                    return from.isNhatkySx == 1
                }

                override fun providerIsBaoTieu(): Boolean {
                    return from.isBaoTieu == 1
                }

                override fun providerNgaySx(): String {
                    return from.ngaySx ?: ""
                }

                override fun providerDkThuhoach(): String {
                    return from.dkThuhoach ?: ""
                }

                override fun providerMsLohang(): String {
                    return from.msLohang ?: ""
                }

                override fun providerMuaVu(): String {
                    return from.muaVu ?: ""
                }

                override fun providerDongGoi(): String {
                    return from.dongGoi ?: ""
                }

                override fun providerHsd(): String {
                    return from.hsd ?: ""
                }

                override fun providerXuatXuong(): String {
                    return from.xuatXuong ?: ""
                }

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
}