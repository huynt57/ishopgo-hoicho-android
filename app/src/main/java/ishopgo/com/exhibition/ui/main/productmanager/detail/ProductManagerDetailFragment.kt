package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.crashlytics.android.core.CrashlyticsCore
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.showStackTrace
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.BrandsAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.ProductManagerAddFragment
import ishopgo.com.exhibition.ui.main.productmanager.add.ProviderAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_product_manager_detail.*
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.util.ArrayList

class ProductManagerDetailFragment : BaseFragment() {
    private lateinit var viewModel: ProductManagerViewModel
    private var product_Id: Long = -1L
    private var feautured: Int = STATUS_NOT_FEAUTURED
    private var status: Int = STATUS_DISPLAY_SHOW
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private lateinit var adapterImages: ComposingPostMediaAdapter
    private var image: String = ""
    private var imageOld = ""
    private var requestBrands = ""
    private var requestProvider = ""
    private val adapterBrands = BrandsAdapter()
    private val adapterProvider = ProviderAdapter()
    private var reloadBrands = false
    private var reloadProvider = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_manager_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product_Id = arguments?.getLong("product_Id", -1L) ?: -1L

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
        reloadBrands = true
        reloadProvider = true
        firstLoadBrand()
        firstLoadProvider()
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

    private fun showDetail(info: ProductManagerDetail) {

        if (info.provideImages().isNotEmpty()) {
            imageOld = info.provideImages()[0]

            Glide.with(context)
                    .load(info.provideImages()[0])
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder))
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

        sw_featured.isChecked = info.wasIsFeatured()

        edit_product_name.setText(info.provideName())
        edit_product_code.setText(info.provideCode())
        edit_product_dvt.setText(info.provideDVT())
        edit_product_madeIn.setText(info.provideMadeIn())
        edit_product_provider_price.setText(info.provideProviderPrice())
        edit_product_price.setText(info.providePrice())
        edit_product_ttprice.setText(info.provideTTPrice())
        edit_product_title.setText(info.provideTitle())
        edit_product_meta_description.setText(info.provideMetaDescription())
        edit_product_tags.setText(info.provideTags())
        edit_product_provider.setText(info.providerAccount?.name ?: "")
        edit_product_status.setText(info.provideStatus())

        if (info.provideDepartments()?.isNotEmpty()!!) edit_product_brand.setText(info.provideDepartments()?.get(0)?.name
                ?: "") else edit_product_brand.setText("")


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
                CrashlyticsCore.getInstance().log(e.showStackTrace())
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

        btn_product_update.setOnClickListener { toast("Đang phát triển") }

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
        edit_product_provider_price.isFocusable = false
        edit_product_provider_price.isFocusableInTouchMode = false
        edit_product_price.isFocusable = false
        edit_product_price.isFocusableInTouchMode = false
        edit_product_ttprice.isFocusable = false
        edit_product_ttprice.isFocusableInTouchMode = false
        edit_product_title.isFocusable = false
        edit_product_title.isFocusableInTouchMode = false
        edit_product_meta_description.isFocusable = false
        edit_product_meta_description.isFocusableInTouchMode = false
        edit_product_tags.isFocusable = false
        edit_product_tags.isFocusableInTouchMode = false
        edit_product_price.setOnClickListener(null)
        edit_product_provider.setOnClickListener(null)
        edit_product_status.setOnClickListener(null)
        sw_featured.isEnabled = false

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
        img_add_related_product.visibility = View.VISIBLE
        view_add_images.visibility = View.VISIBLE
        til_product_description.visibility = View.VISIBLE
        edit_product_name.isFocusable = true
        edit_product_name.isFocusableInTouchMode = true
        edit_product_code.isFocusable = true
        edit_product_code.isFocusableInTouchMode = true
        edit_product_dvt.isFocusable = true
        edit_product_dvt.isFocusableInTouchMode = true
        edit_product_madeIn.isFocusable = true
        edit_product_madeIn.isFocusableInTouchMode = true
        edit_product_provider_price.isFocusable = true
        edit_product_provider_price.isFocusableInTouchMode = true
        edit_product_price.isFocusable = true
        edit_product_price.isFocusableInTouchMode = true
        edit_product_ttprice.isFocusable = true
        edit_product_ttprice.isFocusableInTouchMode = true
        edit_product_title.isFocusable = true
        edit_product_title.isFocusableInTouchMode = true
        edit_product_meta_description.isFocusable = true
        edit_product_meta_description.isFocusableInTouchMode = true
        edit_product_tags.isFocusable = true
        edit_product_tags.isFocusableInTouchMode = true
        edit_product_brand.setOnClickListener { getBrands(edit_product_brand) }
        edit_product_provider.setOnClickListener { getProvider(edit_product_provider) }
        edit_product_status.setOnClickListener { getStatus(edit_product_status) }
        sw_featured.isEnabled = true
        sw_featured.setOnCheckedChangeListener { _, _ -> feautured = if (sw_featured.isChecked) STATUS_FEAUTURED else STATUS_NOT_FEAUTURED }

        edit_product_name.requestFocus()
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(edit_product_name, 0)

        view_scrollview.smoothScrollTo(0, 0)

        btn_product_update.text = "Xong"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data && !ProductManagerAddFragment.CASE_PICK_IMAGE) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context, data.data, (2 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context, data.clipData.getItemAt(i).uri, (2 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }
            adapterImages = ComposingPostMediaAdapter(postMedias)
            adapterImages.notifyItemInserted(postMedias.size - 1)
            rv_product_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_images.adapter = adapterImages
        }

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data && ProductManagerAddFragment.CASE_PICK_IMAGE) {
            if (Toolbox.exceedSize(context, data.data, (2 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder))
                    .into(view_image_product_detail)
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
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getStatus(view: TextView) {
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
                view.text = tv_status_standard.text
                dialog.dismiss()
            }

            tv_status_landing_padding.setOnClickListener {
                status = STATUS_DISPLAY_LANDING_PAGE
                view.text = tv_status_landing_padding.text
                dialog.dismiss()
            }

            tv_status_hidden.setOnClickListener {
                status = STATUS_DISPLAY_HIDDEN
                view.text = tv_status_hidden.text
                dialog.dismiss()
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

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (!CASE_PICK_IMAGE) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
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
    }
}