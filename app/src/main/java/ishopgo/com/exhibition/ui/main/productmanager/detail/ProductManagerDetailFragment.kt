package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.ProductManagerRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_ID
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.*
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_product_manager_detail.*
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.util.*

class ProductManagerDetailFragment : BaseFragment() {
    private lateinit var viewModel: ProductManagerViewModel
    private var product_Id: Long = 0L
    private var provider_id: Long = 0L
    private var feautured: Int = STATUS_NOT_FEAUTURED
    private var status: Int = STATUS_DISPLAY_SHOW
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private var image: String = ""
    private var imageOld = ""
    private var requestBrands = ""
    private var requestProvider = ""
    private val adapterBrands = BrandsAdapter()
    private val adapterProvider = ProviderAdapter()
    private var reloadBrands = false
    private var reloadProvider = false
    private var isEditMode = false
    private val adapterCategory = CategoryAdapter()
    private val adapterCategory_1 = CategoryAdapter()
    private val adapterCategory_2 = CategoryAdapter()
    private val adapterCategory_3 = CategoryAdapter()
    private val adapterCategory_4 = CategoryAdapter()
    private var listCategory = mutableListOf<Category>()
    private var adapterProductRelatedImage = ProductManagerRelatedCollapseAdapters()
    private var adapterDialogProduct = ProductManagerRelatedAdapter()
    private var listProductRelated: ArrayList<ProductManagerProvider> = ArrayList()

    private var brand_id: Long = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_manager_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product_Id = arguments?.getLong(EXTRA_ID, -1L) ?: -1L

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (UserDataManager.currentType == "Chủ hội chợ")
            til_product_provider.visibility = View.VISIBLE else {
            provider_id = UserDataManager.currentUserId
            til_product_provider.visibility = View.GONE
        }

        view_add_images.setOnClickListener {
            CASE_PICK_IMAGE = false
            launchPickPhotoIntent()
        }

        img_add_related_product.setOnClickListener {
            performSearchingProduct()
        }

        btn_product_update.setOnClickListener {
            if (!isEditMode) {
                startEditing()
                isEditMode = true
            } else {
                if (UserDataManager.currentType == "Chủ hội chợ") {
                    if (checkRequireFields(edit_product_name.text.toString(), edit_product_price.text.toString(), edit_product_code.text.toString(),
                                    edt_product_categories.text.toString(), edit_product_provider.text.toString(), edit_product_brand.text.toString())) {
                        showProgressDialog()
                        viewModel.editProductManager(product_Id, edit_product_name.text.toString(), edit_product_code.text.toString(), edit_product_title.text.toString(),
                                edit_product_price?.money
                                        ?: 0, edit_product_dvt.text.toString(), provider_id, brand_id, edit_product_madeIn.text.toString(),
                                image, postMedias, edit_product_description.text.toString(), status, edit_product_meta_description.text.toString(), edit_product_meta_keyword.text.toString(),
                                edit_product_tags.text.toString(), listCategory, listProductRelated, feautured, edit_produt_wholesale_from.money
                                ?: 0, edit_produt_wholesale_to.money
                                ?: 0, edit_produt_wholesale_count.text.toString())
                    }
                } else
                    if (checkRequireFields(edit_product_name.text.toString(), edit_product_price.text.toString(), edit_product_code.text.toString(),
                                    edt_product_categories.text.toString(), provider_id.toString(), edit_product_brand.text.toString())) {
                        showProgressDialog()
                        viewModel.editProductManager(product_Id, edit_product_name.text.toString(), edit_product_code.text.toString(), edit_product_title.text.toString(),
                                edit_product_price?.money
                                        ?: 0, edit_product_dvt.text.toString(), provider_id, brand_id, edit_product_madeIn.text.toString(),
                                image, postMedias, edit_product_description.text.toString(), status, edit_product_meta_description.text.toString(), edit_product_meta_keyword.text.toString(),
                                edit_product_tags.text.toString(), listCategory, listProductRelated, feautured, edit_produt_wholesale_from.money
                                ?: 0, edit_produt_wholesale_to.money
                                ?: 0, edit_produt_wholesale_count.text.toString())
                    }
            }
        }
    }

    private fun firstLoadCategory() {
        viewModel.loadCategories()
    }

    private fun firstLoadCategoryChild(category: Category, level: Int) {
        viewModel.loadChildCategory(category, level)
    }

    private fun setupImageRecycleview() {
        rv_product_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_product_images.adapter = adapterImages
        adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
            override fun click(position: Int, data: PostMedia, code: Int) {
                postMedias.remove(data)
                if (postMedias.isEmpty()) rv_product_images.visibility = View.GONE
                adapterImages.replaceAll(postMedias)
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProductManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.dataProductDetail.observe(this, Observer { p ->
            p.let {
                it?.let { it1 -> showDetail(it1) }
            }
        })

        viewModel.dataBrands.observe(this, Observer { p ->
            p.let {
                if (reloadBrands) {
                    it?.let { it1 -> adapterBrands.replaceAll(it1) }
                } else {
                    it?.let { it1 -> adapterBrands.addAll(it1) }
                }
            }
        })

        viewModel.dataProvider.observe(this, Observer { p ->
            p.let {
                if (reloadProvider) {
                    it?.let { it1 -> adapterProvider.replaceAll(it1) }
                } else {
                    it?.let { it1 -> adapterProvider.addAll(it1) }
                }
            }
        })

        viewModel.dataReturned.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    adapterDialogProduct.replaceAll(it)
                    hideProgressDialog()
                } else {
                    adapterDialogProduct.addAll(it)
                }
            }
        })

        viewModel.editProductSusscess.observe(this, Observer { p ->
            hideProgressDialog()
            stopEditing()
            isEditMode = false
            toast("Cập nhật thành công")
            viewModel.getProductDetail(product_Id)
            activity?.setResult(RESULT_OK)
        })

        viewModel.categories.observe(this, Observer { p ->
            p?.let {
                adapterCategory.replaceAll(it)
            }
        })

        viewModel.childCategories.observe(this, Observer { p ->
            p?.let {
                adapterCategory_1.replaceAll(it)
            }
        })

        viewModel.childCategories_1.observe(this, Observer { p ->
            p?.let {
                adapterCategory_2.replaceAll(it)
            }
        })

        viewModel.childCategories_2.observe(this, Observer { p ->
            p?.let {
                adapterCategory_3.replaceAll(it)
            }
        })

        viewModel.childCategories_3.observe(this, Observer { p ->
            p?.let {
                adapterCategory_4.replaceAll(it)
            }
        })

        viewModel.childCategoriesDetail.observe(this, Observer { p ->
            p?.let {
                adapterCategory_1.replaceAll(it)
            }
        })

        viewModel.childCategoriesDetail_2.observe(this, Observer { p ->
            p?.let {
                adapterCategory_2.replaceAll(it)
            }
        })

        viewModel.childCategoriesDetail_3.observe(this, Observer { p ->
            p?.let {
                adapterCategory_3.replaceAll(it)
            }
        })

        viewModel.childCategoriesDetail_4.observe(this, Observer { p ->
            p?.let {
                adapterCategory_4.replaceAll(it)
            }
        })

        reloadBrands = true
        reloadProvider = true
        firstLoadBrand()
        firstLoadProvider()
        firstLoadCategory()
        firstLoadProductRelated()
        viewModel.getProductDetail(product_Id)
    }

    private fun firstLoadBrand() {
        reloadBrands = false
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBrand(firstLoad)
    }

    private fun loadMoreBrands(currentCount: Int) {
        reloadBrands = true
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBrand(loadMore)
    }

    private fun firstLoadProvider() {
        reloadProvider = false
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getProvider(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadProvider = true
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getProvider(loadMore)
    }

    private fun firstLoadProductRelated() {
        reloadData = true
        val firstLoad = ProductManagerRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = keyWord
        firstLoad.code = code
        viewModel.loadData(firstLoad)
    }

    private fun loadMoreProductRelated(currentCount: Int) {
        reloadData = false
        val loadMore = ProductManagerRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.name = keyWord
        loadMore.code = code
        viewModel.loadData(loadMore)
    }

    private var keyWord: String = ""
    private var code: String = ""

    private fun performSearchingProduct() {
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

                        val layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        rv_search.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        rv_search.adapter = adapterDialogProduct
                        rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager2) {
                            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                                loadMoreProductRelated(totalItemsCount)
                            }
                        })
                        adapterDialogProduct.listener = object : ClickableAdapter.BaseAdapterAction<ProductManagerProvider> {
                            override fun click(position: Int, data: ProductManagerProvider, code: Int) {
                                if (listProductRelated.size == 0) {
                                    listProductRelated.add(data)
                                } else {
                                    val isContained = listProductRelated.any {
                                        if (it is IdentityData && data is IdentityData)
                                            return@any it.id == data.id
                                        return@any false
                                    }

                                    if (isContained) {
                                        toast("Sản phẩm liên quan đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                                        return
                                    } else {
                                        listProductRelated.add(data)
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

            val edt_search_name = dialog.findViewById(R.id.edt_search_name) as TextInputEditText
            val edt_search_code = dialog.findViewById(R.id.edt_search_code) as TextInputEditText
            edt_search_name.hint = "Nhập tên sản phẩm"
            edt_search_code.hint = "Nhập mã sản phẩm"

            dialog.show()
        }
    }

    private fun loadSanPhamLienQuan() {
        context?.let {
            adapterProductRelatedImage.replaceAll(listProductRelated)
            rv_product_related_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_related_products.adapter = adapterProductRelatedImage
            adapterProductRelatedImage.listener = object : ClickableAdapter.BaseAdapterAction<ProductManagerProvider> {
                override fun click(position: Int, data: ProductManagerProvider, code: Int) {
                    listProductRelated.remove(data)
                    adapterProductRelatedImage.replaceAll(listProductRelated)
                }
            }
        }
    }

    private fun showDetail(info: ProductManagerDetail) {

        sw_show_wholesale.isChecked = info.provideViewWholesale()

        if (info.provideViewWholesale()) {
            linear_wholesale.visibility = View.VISIBLE
            edit_produt_wholesale_from.setText(info.provideWholesaleFrom())
            edit_produt_wholesale_to.setText(info.provideWholesaleTo())
            edit_produt_wholesale_count.setText(info.provideWholesaleCountProduct())
        } else linear_wholesale.visibility = View.GONE

        if (info.provideImages().isNotEmpty()) {
            imageOld = info.provideImages()[0]

            Glide.with(context)
                    .load(info.provideImages()[0])
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_image_product_detail)

            view_image_product_detail.setOnClickListener {
                val listImage = mutableListOf<String>()
                listImage.add(info.provideImages()[0])
                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                startActivity(intent)
            }

            img_add_related_product.visibility = View.GONE
            view_add_images.visibility = View.GONE

            val imagesAdapter = ProductManagerDetailImagesAdapter()
            info.images?.let { imagesAdapter.replaceAll(it) }
            rv_product_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_images.adapter = imagesAdapter
            imagesAdapter.listener = object : ClickableAdapter.BaseAdapterAction<String> {
                override fun click(position: Int, data: String, code: Int) {
                    val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, info.provideImages().toTypedArray())
                    startActivity(intent)
                }
            }
        }

        if (info.categories != null && info.categories!!.isNotEmpty()) {
            listCategory = info.categories!!
            for (i in info.categories!!.indices) {

                if (i == 0) edt_product_categories.setText(info.categories!![i].name)
                if (i == 1) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_1)
                    til_category_1.visibility = View.VISIBLE
                    edt_product_categories_1.setText(info.categories!![i].name)
                }
                if (i == 2) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_2)
                    til_category_2.visibility = View.VISIBLE
                    edt_product_categories_2.setText(info.categories!![i].name)
                }
                if (i == 3) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_3)
                    til_category_3.visibility = View.VISIBLE
                    edt_product_categories_3.setText(info.categories!![i].name)
                }
                if (i == 4) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_4)
                    til_category_4.visibility = View.VISIBLE
                    edt_product_categories_4.setText(info.categories!![i].name)
                }
            }
        }

        sw_featured.isChecked = info.wasIsFeatured()

        edit_product_name.setText(info.provideName())
        edit_product_code.setText(info.provideCode())
        edit_product_dvt.setText(info.provideDVT())
        edit_product_madeIn.setText(info.provideMadeIn())
        edit_product_price.setText(info.providePrice())
        edit_product_title.setText(info.provideTitle())
        edit_product_meta_description.setText(info.provideMetaDescription())
        edit_product_tags.setText(info.provideTags())
        edit_product_provider.setText(info.providerAccount?.name ?: "")

        sw_status.isChecked = info.provideStatus()
        if (info.provideDepartments() != null && info.provideDepartments()!!.isNotEmpty())
            edit_product_brand.setText(info.provideDepartments()?.get(0)?.name ?: "")

        container_product_detail.visibility = if (info.provideDescription().isEmpty()) View.GONE else View.VISIBLE
        if (container_product_detail.visibility == View.VISIBLE) {
            try {
                val css = IOUtils.toString(context?.assets?.open("WebViewStyle.css"), "UTF-8")
                val fullHtml = String.format(
                        "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                        css,
                        info.provideDescription()
                )
                webView_spk_detail_description.loadData(fullHtml, "text/html; charset=UTF-8", null)
            } catch (e: IOException) {
                e.printStackTrace()
                webView_spk_detail_description.loadData(info.provideDescription(), "text/html; charset=UTF-8", null)
            }
        }

        tv_view_details.setOnClickListener({
            val i = Intent(context, FullDetailActivity::class.java)
            i.putExtra(Const.TransferKey.EXTRA_JSON, info.provideDescription())
            startActivity(i)
        })


        if (info.collectionProducts != null) {
            val productsRelatedAdapter = ProductManagerDetailRelatedAdapter()
            info.collectionProducts!!.products?.let { productsRelatedAdapter.replaceAll(it) }
            rv_product_related_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_related_products.adapter = productsRelatedAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun stopEditing() {

        if (image.isNotEmpty()) {
            val listImage = mutableListOf<String>()
            listImage.add(image)
            view_image_product_detail.setOnClickListener {
                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                startActivity(intent)
            }
        } else {
            val listImage = mutableListOf<String>()
            listImage.add(imageOld)
            view_image_product_detail.setOnClickListener {
                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                startActivity(intent)
            }
        }

        container_product_detail.visibility = View.VISIBLE
        img_add_related_product.visibility = View.GONE
        view_add_images.visibility = View.GONE
        til_product_description.visibility = View.GONE
        edit_product_name.isFocusable = false
        edit_product_name.isFocusableInTouchMode = false
        edit_product_code.isFocusable = false
        edit_product_code.isFocusableInTouchMode = false
        edit_product_dvt.isFocusable = false
        edit_product_dvt.isFocusableInTouchMode = false
        edit_product_madeIn.isFocusable = false
        edit_product_madeIn.isFocusableInTouchMode = false
        edit_product_price.isFocusable = false
        edit_product_price.isFocusableInTouchMode = false
        edit_product_title.isFocusable = false
        edit_product_title.isFocusableInTouchMode = false
        edit_product_meta_description.isFocusable = false
        edit_product_meta_description.isFocusableInTouchMode = false
        edit_product_tags.isFocusable = false
        edit_product_tags.isFocusableInTouchMode = false
        edit_produt_wholesale_from.isFocusable = false
        edit_produt_wholesale_from.isFocusableInTouchMode = false
        edit_produt_wholesale_to.isFocusable = false
        edit_produt_wholesale_to.isFocusableInTouchMode = false
        edit_produt_wholesale_count.isFocusable = false
        edit_produt_wholesale_count.isFocusableInTouchMode = false
        edit_product_price.setOnClickListener(null)
        edit_product_provider.setOnClickListener(null)
        sw_featured.isClickable = false
        sw_status.isClickable = false
        sw_show_wholesale.isClickable = false

        edt_product_categories.setOnClickListener(null)
        edt_product_categories_1.setOnClickListener(null)
        edt_product_categories_2.setOnClickListener(null)
        edt_product_categories_3.setOnClickListener(null)
        edt_product_categories_4.setOnClickListener(null)

        view_scrollview.smoothScrollTo(0, 0)
        btn_product_update.text = "Cập nhật"
    }

    @SuppressLint("SetTextI18n")
    private fun startEditing() {
        view_image_product_detail.setOnClickListener {
            CASE_PICK_IMAGE
            launchPickPhotoIntent()
        }
        container_product_detail.visibility = View.GONE

        //Tạm thời đóng thêm ảnh vào danh sách và thêm sản phẩm vì chưa xử lý được
        img_add_related_product.visibility = View.GONE
        view_add_images.visibility = View.GONE

        til_product_description.visibility = View.VISIBLE
        edit_product_name.isFocusable = true
        edit_product_name.isFocusableInTouchMode = true
        edit_product_code.isFocusable = true
        edit_product_code.isFocusableInTouchMode = true
        edit_product_dvt.isFocusable = true
        edit_product_dvt.isFocusableInTouchMode = true
        edit_product_madeIn.isFocusable = true
        edit_product_madeIn.isFocusableInTouchMode = true
        edit_product_price.isFocusable = true
        edit_product_price.isFocusableInTouchMode = true
        edit_product_title.isFocusable = true
        edit_product_title.isFocusableInTouchMode = true
        edit_product_meta_description.isFocusable = true
        edit_product_meta_description.isFocusableInTouchMode = true
        edit_product_tags.isFocusable = true
        edit_product_tags.isFocusableInTouchMode = true
        edit_produt_wholesale_from.isFocusable = true
        edit_produt_wholesale_from.isFocusableInTouchMode = true
        edit_produt_wholesale_to.isFocusable = true
        edit_produt_wholesale_to.isFocusableInTouchMode = true
        edit_produt_wholesale_count.isFocusable = true
        edit_produt_wholesale_count.isFocusableInTouchMode = true
        edit_product_brand.setOnClickListener { getBrands(edit_product_brand) }
        edit_product_provider.setOnClickListener { getProvider(edit_product_provider) }
        sw_featured.isClickable = true
        sw_status.isClickable = true
        sw_show_wholesale.isClickable = true
        sw_featured.setOnCheckedChangeListener { _, _ -> feautured = if (sw_featured.isChecked) STATUS_FEAUTURED else STATUS_NOT_FEAUTURED }
        sw_status.setOnCheckedChangeListener { _, _ -> status = if (sw_status.isChecked) STATUS_DISPLAY_SHOW else STATUS_DISPLAY_HIDDEN }

        sw_show_wholesale.setOnCheckedChangeListener { _, _ ->
            if (sw_show_wholesale.isChecked) linear_wholesale.visibility = View.VISIBLE else {
                linear_wholesale.visibility = View.GONE
                edit_produt_wholesale_from.setText("")
                edit_produt_wholesale_to.setText("")
                edit_produt_wholesale_count.setText("")
            }
        }

        edt_product_categories.setOnClickListener { getCategory(edt_product_categories, CATEGORY_LEVEL_PARENT) }
        edt_product_categories_1.setOnClickListener { getCategory(edt_product_categories_1, CATEGORY_LEVEL_1) }
        edt_product_categories_2.setOnClickListener { getCategory(edt_product_categories_2, CATEGORY_LEVEL_2) }
        edt_product_categories_3.setOnClickListener { getCategory(edt_product_categories_3, CATEGORY_LEVEL_3) }
        edt_product_categories_4.setOnClickListener { getCategory(edt_product_categories_4, CATEGORY_LEVEL_4) }


        edit_product_name.requestFocus()
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(edit_product_name, 0)

        view_scrollview.smoothScrollTo(0, 0)

        btn_product_update.text = "Xong"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data && !CASE_PICK_IMAGE) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }
            adapterImages.replaceAll(postMedias)
            rv_product_images.visibility = View.VISIBLE
        }

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data && CASE_PICK_IMAGE) {
            if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_image_product_detail)
        }
    }


    private fun getCategory(view: TextView, level: Int) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn danh mục")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            if (level == CATEGORY_LEVEL_PARENT) {
                rv_search.adapter = adapterCategory
                adapterCategory.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
                    override fun click(position: Int, data: CategoryProvider, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.provideName()
                            view.error = null
                            if (data is Category) {
                                listCategory.clear()
                                firstLoadCategoryChild(data, CATEGORY_LEVEL_1)
                                listCategory.add(data)
                                til_category_1.visibility = View.VISIBLE
                                edt_product_categories_1.setText("")
                                til_category_2.visibility = View.GONE
                                edt_product_categories_2.setText("")
                                til_category_3.visibility = View.GONE
                                edt_product_categories_3.setText("")
                                til_category_4.visibility = View.GONE
                                edt_product_categories_4.setText("")
                            }
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_1) {
                rv_search.adapter = adapterCategory_1
                adapterCategory_1.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
                    override fun click(position: Int, data: CategoryProvider, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            if (listCategory.size >= 2)
                                for (i in 1 until listCategory.size) {
                                    if (i == 1)
                                        listCategory.removeAt(i)
                                    if (i == 2)
                                        listCategory.removeAt(i - 1)
                                    if (i == 3)
                                        listCategory.removeAt(i - 2)
                                    if (i == 4)
                                        listCategory.removeAt(i - 3)
                                }
                            view.text = data.provideName()
                            view.error = null
                            if (data is Category) {
                                firstLoadCategoryChild(data, CATEGORY_LEVEL_2)
                                listCategory.add(data)
                                til_category_2.visibility = View.VISIBLE
                                edt_product_categories_2.setText("")
                                til_category_3.visibility = View.GONE
                                edt_product_categories_3.setText("")
                                til_category_4.visibility = View.GONE
                                edt_product_categories_4.setText("")
                            }
                        }
                    }
                }
            }

            if (level == CATEGORY_LEVEL_2) {
                rv_search.adapter = adapterCategory_2
                adapterCategory_2.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
                    override fun click(position: Int, data: CategoryProvider, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            if (listCategory.size >= 3)
                                for (i in 2 until listCategory.size) {
                                    if (i == 2)
                                        listCategory.removeAt(i)
                                    if (i == 3)
                                        listCategory.removeAt(i - 1)
                                    if (i == 4)
                                        listCategory.removeAt(i - 2)
                                }
                            view.text = data.provideName()
                            view.error = null
                            if (data is Category) {
                                firstLoadCategoryChild(data, CATEGORY_LEVEL_3)
                                listCategory.add(data)
                                til_category_3.visibility = View.VISIBLE
                                edt_product_categories_3.setText("")
                                til_category_4.visibility = View.GONE
                                edt_product_categories_4.setText("")
                            }
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_3) {
                rv_search.adapter = adapterCategory_3
                adapterCategory_3.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
                    override fun click(position: Int, data: CategoryProvider, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            if (listCategory.size >= 4)
                                for (i in 3 until listCategory.size) {
                                    if (i == 3)
                                        listCategory.removeAt(i)
                                    if (i == 4)
                                        listCategory.removeAt(i - 1)
                                }
                            view.text = data.provideName()
                            view.error = null
                            if (data is Category) {
                                firstLoadCategoryChild(data, CATEGORY_LEVEL_4)
                                listCategory.add(data)
                                til_category_4.visibility = View.VISIBLE
                                edt_product_categories_4.setText("")
                            }
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_4) {
                rv_search.adapter = adapterCategory_4
                adapterCategory_4.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
                    override fun click(position: Int, data: CategoryProvider, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.provideName()
                            view.error = null
                            if (data is Category) {
                                listCategory.add(data)
                            }
                        }
                    }
                }
            }

            dialog.show()
        }
    }

    private fun getBrands(view: TextView) {
        requestBrands = ""
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thương hiệu")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBrands
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreBrands(totalItemsCount)
                }
            })

            adapterBrands.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
                override fun click(position: Int, data: Brand, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        brand_id = data.id
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getProvider(view: TextView) {
        requestProvider = ""
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn nhà cung cấp")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterProvider
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterProvider.listener = object : ClickableAdapter.BaseAdapterAction<Provider> {
                override fun click(position: Int, data: Provider, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        provider_id = data.id
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (!CASE_PICK_IMAGE) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private fun checkRequireFields(name: String, price: String, code: String, category: String, provider: String, brand: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tên không được để trống")
            edit_product_name.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_name)
            return false
        }

//        if (title.trim().isEmpty()) {
//            toast("Tiêu đề không được để trống")
//            edit_product_title.error = getString(R.string.error_field_required)
//            requestFocusEditText(edit_product_title)
//            return false
//        }

        if (price.trim().isEmpty()) {
            toast("Giá bản lẻ không được để trống")
            edit_product_price.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_price)
            return false
        }

        if (code.trim().isEmpty()) {
            toast("Mã sản phẩm không được để trống")
            edit_product_code.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_code)
            return false
        }

        if (category.trim().isEmpty()) {
            toast("Danh mục không được để trống")
            edt_product_categories.error = getString(R.string.error_field_required)
            edt_product_categories.requestFocus()
            return false
        }

        if (provider.trim().isEmpty()) {
            toast("Nhà cung cấp không được để trống")
            edit_product_provider.error = getString(R.string.error_field_required)
            edit_product_provider.requestFocus()
            return false
        }

        if (brand.trim().isEmpty()) {
            toast("Thương hiệu không được để trống")
            edit_product_brand.error = getString(R.string.error_field_required)
            edit_product_brand.requestFocus()
            return false
        }
        return true
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    companion object {
        const val STATUS_FEAUTURED: Int = 1 //Sp nổi bật
        const val STATUS_NOT_FEAUTURED: Int = 0 //Sp bình thường
        var CASE_PICK_IMAGE: Boolean = true // true = Ảnh sản phẩm, false = Nhiều ảnh

        const val STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        const val STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dang landing page
        const val STATUS_DISPLAY_HIDDEN: Int = 0 //Không hiển thị

        fun newInstance(params: Bundle): ProductManagerDetailFragment {
            val fragment = ProductManagerDetailFragment()
            fragment.arguments = params

            return fragment
        }

        const val CATEGORY_LEVEL_PARENT: Int = 0
        const val CATEGORY_LEVEL_1: Int = 1
        const val CATEGORY_LEVEL_2: Int = 2
        const val CATEGORY_LEVEL_3: Int = 3
        const val CATEGORY_LEVEL_4: Int = 4
    }
}