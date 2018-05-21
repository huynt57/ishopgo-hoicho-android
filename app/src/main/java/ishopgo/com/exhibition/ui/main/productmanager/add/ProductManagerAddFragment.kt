package ishopgo.com.exhibition.ui.main.productmanager.add

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_product_manager_add.*
import java.util.*

class ProductManagerAddFragment : BaseFragment() {
    private lateinit var viewModel: ProductManagerViewModel
    private val adapterBrands = BrandsAdapter()
    private val adapterProvider = ProviderAdapter()
    private var adapterProductRelatedImage = ProductManagerRelatedCollapseAdapters()
    private var adapterDialogProduct = ProductManagerRelatedAdapter()

    private var reloadBrands = false
    private var reloadProvider = false
    private var requestBrands = ""
    private var requestProvider = ""
    private val handler = Handler(Looper.getMainLooper())
    private var status: Int = STATUS_DISPLAY_SHOW
    private var feautured: Int = STATUS_NOT_FEAUTURED
    private var image: String = ""
    private var brand_id: String = ""
    private var provider_id: String = ""
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private var listProductRelated: ArrayList<ProductManagerProvider> = ArrayList()

    companion object {
        const val TAG = "ProductManagerFragment"
        const val STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        const val STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dang landing page
        const val STATUS_DISPLAY_HIDDEN: Int = 0 //Không hiển thị

        const val STATUS_FEAUTURED: Int = 1 //Sp nổi bật
        const val STATUS_NOT_FEAUTURED: Int = 0 //Sp bình thường
        var CASE_PICK_IMAGE: Boolean = true // true = Ảnh sản phẩm, false = Nhiều ảnh

        fun newInstance(params: Bundle): ProductManagerAddFragment {
            val fragment = ProductManagerAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_manager_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_product_brand.setOnClickListener { getBrands(edit_product_brand) }
        edit_product_provider.setOnClickListener { getProvider(edit_product_provider) }
        view_image_add_product.setOnClickListener {
            CASE_PICK_IMAGE = true
            launchPickPhotoIntent()
        }

        img_add_related_product.setOnClickListener { performSearchingProduct() }

        view_add_images.setOnClickListener {
            CASE_PICK_IMAGE = false
            launchPickPhotoIntent()
        }
        edit_product_status.setText("Hiển thị dạng chuẩn")
        edit_product_status.setOnClickListener {
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .customView(R.layout.dialog_product_status, false)
                        .autoDismiss(false)
                        .canceledOnTouchOutside(true)
                        .build()

                val tv_status_standard = dialog.findViewById(R.id.tv_status_standard) as TextView
                val tv_status_landing_padding = dialog.findViewById(R.id.tv_status_landing_padding) as TextView
                val tv_status_hidden = dialog.findViewById(R.id.tv_status_hidden) as TextView

                tv_status_standard.setOnClickListener {
                    status = STATUS_DISPLAY_SHOW
                    edit_product_status.setText(tv_status_standard.text)
                    dialog.dismiss()
                }

                tv_status_landing_padding.setOnClickListener {
                    status = STATUS_DISPLAY_LANDING_PAGE
                    edit_product_status.setText(tv_status_landing_padding.text)
                    dialog.dismiss()
                }

                tv_status_hidden.setOnClickListener {
                    status = STATUS_DISPLAY_HIDDEN
                    edit_product_status.setText(tv_status_hidden.text)
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
        sw_featured.setOnCheckedChangeListener { _, _ -> feautured = if (sw_featured.isChecked) STATUS_FEAUTURED else STATUS_NOT_FEAUTURED }

        btn_product_add.setOnClickListener {
            if (checkRequireFields(image, edit_product_name.text.toString(), edit_product_title.text.toString(), edit_product_price.text.toString(), edit_product_code.text.toString())) {
                showProgressDialog()
                viewModel.createProductManager(edit_product_name.text.toString(), edit_product_code.text.toString(), edit_product_title.text.toString(), edit_produt_ttprice.text.toString(),
                        edit_product_price.text.toString(), edit_product_provider_price.text.toString(), edit_product_dvt.text.toString(), provider_id, brand_id, edt_product_madeIn.text.toString(),
                        image, postMedias, edit_product_description.text.toString(), status, edit_product_meta_description.text.toString(), edit_product_meta_keyword.text.toString(),
                        edit_product_tag.text.toString(), null, listProductRelated, feautured)
            }
        }

        setupImageRecycleview()
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
        viewModel.createProductSusscess.observe(this, Observer { p ->
            hideProgressDialog()
            toast("Thêm sản phẩm thành công")
            activity?.setResult(RESULT_OK)
            activity?.finish()
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

        reloadBrands = true
        reloadProvider = true

        firstLoadBrand()
        firstLoadProvider()
        firstLoadProductRelated()
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

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (!CASE_PICK_IMAGE) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
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
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
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
                                loadSanPhamLienQuan()
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
            rv_related_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_related_products.adapter = adapterProductRelatedImage
            adapterProductRelatedImage.listener = object : ClickableAdapter.BaseAdapterAction<ProductManagerProvider> {
                override fun click(position: Int, data: ProductManagerProvider, code: Int) {
                    listProductRelated.remove(data)
                    adapterProductRelatedImage.replaceAll(listProductRelated)
                }
            }
        }
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
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder))
                    .into(view_image_add_product)
        }
    }

    private fun checkRequireFields(image: String, name: String, title: String, price: String, code: String): Boolean {
        if (image.trim().isEmpty()) {
            toast("Ảnh sản phẩm không được để trống")
            return false
        }

        if (name.trim().isEmpty()) {
            toast("Tên không được để trống")
            edit_product_name.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_name)
            return false
        }

        if (title.trim().isEmpty()) {
            toast("Tiêu đề không được để trống")
            edit_product_title.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_title)
            return false
        }

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
        return true
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }
}