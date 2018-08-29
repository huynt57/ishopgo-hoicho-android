package ishopgo.com.exhibition.ui.main.product.icheckproduct.update

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckCategory
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.domain.response.IcheckVendor
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
import ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint.IcheckCategoryAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_icheck_update_product.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class IcheckUpdateProductFragment : BaseActionBarFragment() {
    companion object {
        fun newInstance(params: Bundle): IcheckUpdateProductFragment {
            val fragment = IcheckUpdateProductFragment()
            fragment.arguments = params

            return fragment
        }

        const val PERMISSIONS_REQUEST_CAMERA = 100

        const val CATEGORY_LEVEL_1: Int = 1
        const val CATEGORY_LEVEL_2: Int = 2
        const val CATEGORY_LEVEL_3: Int = 3
        const val CATEGORY_LEVEL_4: Int = 4
    }

    private var data: IcheckProduct? = null
    private var code = ""
    private var image = "image default"
    private var category_1 = 0L
    private var category_2 = 0L
    private var category_3 = 0L
    private var category_4 = 0L
    private val adapterCategory_1 = IcheckCategoryAdapter()
    private val adapterCategory_2 = IcheckCategoryAdapter()
    private val adapterCategory_3 = IcheckCategoryAdapter()
    private val adapterCategory_4 = IcheckCategoryAdapter()
    private var adapterImages = ComposingPostMediaAdapter()
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private lateinit var viewModel: IcheckProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        code = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE) ?: ""

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, IcheckProduct::class.java)
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_icheck_update_product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()
        setupImageRecycleview()
        view_add_images.setOnClickListener { launchPickPhotoIntent() }
        btn_product_add.setOnClickListener {
            val listImage = mutableListOf<String>()
            for (i in postMedias.indices){
                listImage.add(postMedias[i].uri.toString())
            }
            if (isRequiredFieldsValid(listImage, edit_product_tenSp.text.toString(), edit_product_giaBan.text.toString(), edit_product_maSp.text.toString(),
                            category_1, category_2, category_3, category_4, edit_product_moTa.text.toString()))
                viewModel.updateIcheckProduct(edit_product_maSp.text.toString(), edit_product_tenSp.text.toString(), edit_product_giaBan.money
                        ?: 0L, image, listImage, category_1, category_2, category_3, category_4,
                        edit_product_moTa.text.toString())
        }
        view_camera.setOnClickListener {
            takePhoto()
        }
        if (data != null) {
            val convert = ProductDetailConverter().convert(data!!)

            edit_product_tenSp.setText(convert.provideProductName())
            edit_product_maSp.setText(convert.provideProductBarCode())
            edit_product_giaBan.setText(convert.provideProductPrice())

            if (data!!.attributes?.isNotEmpty() == true) {
                val attribute = data!!.attributes!![0]
                edit_product_moTa.setText(attribute.shortContent)
            }

            val vendor = convert.provideProductVendor()
            if (vendor != null) {
                edit_product_tenDoanhNghiep.setText(vendor.name)
                edit_product_soDienThoai.setText(vendor.phone)
                edit_product_email.setText(vendor.email)
                edit_product_website.setText(vendor.website)
                edit_product_diaChi.setText(vendor.address)

                if (vendor.country != null) {
                    edit_product_quocGia.setText(vendor.country?.name ?: "")
                }
            }

            if (edit_product_tenSp.text.toString().trim().isNotEmpty()) {
                edit_product_tenSp.isFocusable = false
                edit_product_tenSp.isFocusableInTouchMode = false
            }

            if (edit_product_giaBan.text.toString().trim().isNotEmpty()) {
                edit_product_giaBan.isFocusable = false
                edit_product_giaBan.isFocusableInTouchMode = false
            }

            if (edit_product_moTa.text.toString().trim().isNotEmpty()) {
                edit_product_moTa.isFocusable = false
                edit_product_moTa.isFocusableInTouchMode = false
            }

            if (edit_product_tenDoanhNghiep.text.toString().trim().isNotEmpty()) {
                edit_product_tenDoanhNghiep.isFocusable = false
                edit_product_tenDoanhNghiep.isFocusableInTouchMode = false
            }

            if (edit_product_soDienThoai.text.toString().trim().isNotEmpty()) {
                edit_product_soDienThoai.isFocusable = false
                edit_product_soDienThoai.isFocusableInTouchMode = false
            }

            if (edit_product_email.text.toString().trim().isNotEmpty()) {
                edit_product_email.isFocusable = false
                edit_product_email.isFocusableInTouchMode = false
            }

            if (edit_product_website.text.toString().trim().isNotEmpty()) {
                edit_product_website.isFocusable = false
                edit_product_website.isFocusableInTouchMode = false
            }

            if (edit_product_diaChi.text.toString().trim().isNotEmpty()) {
                edit_product_diaChi.isFocusable = false
                edit_product_diaChi.isFocusableInTouchMode = false
            }
        }

        if (code.isNotBlank()) {
            edit_product_maSp.setText(code)
        }

        edt_product_danhMuc.setOnClickListener { getCategory(edt_product_danhMuc, CATEGORY_LEVEL_1) }
        edt_product_danhMuc_cap1.setOnClickListener { getCategory(edt_product_danhMuc_cap1, CATEGORY_LEVEL_2) }
        edt_product_danhMuc_cap2.setOnClickListener { getCategory(edt_product_danhMuc_cap2, CATEGORY_LEVEL_3) }
        edt_product_danhMuc_cap3.setOnClickListener { getCategory(edt_product_danhMuc_cap3, CATEGORY_LEVEL_4) }
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

            if (level == CATEGORY_LEVEL_1) {
                rv_search.adapter = adapterCategory_1
                adapterCategory_1.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
                    override fun click(position: Int, data: IcheckCategory, code: Int) {
                        context?.let {
                            view.text = data.name ?: ""
                            view.error = null

                            category_1 = data.id
                            category_2 = 0L
                            category_3 = 0L
                            category_4 = 0L

                            if (data.childrens ?: 0 > 0) {
                                val request = String.format("https://api-affiliate.icheck.com.vn:6086/categories?parent_id=%s", data.id)
                                viewModel.loadIcheckCategory_2(request)

                                til_category_1.visibility = View.VISIBLE
                                edt_product_danhMuc_cap1.setText("")
                                til_category_2.visibility = View.GONE
                                edt_product_danhMuc_cap2.setText("")
                                til_category_3.visibility = View.GONE
                                edt_product_danhMuc_cap3.setText("")
                            } else {
                                til_category_1.visibility = View.GONE
                                edt_product_danhMuc_cap1.setText("")
                                til_category_2.visibility = View.GONE
                                edt_product_danhMuc_cap2.setText("")
                                til_category_3.visibility = View.GONE
                                edt_product_danhMuc_cap3.setText("")
                            }
                            dialog.dismiss()
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_2) {
                rv_search.adapter = adapterCategory_2
                adapterCategory_2.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
                    override fun click(position: Int, data: IcheckCategory, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.name ?: ""
                            view.error = null

                            category_2 = data.id
                            category_3 = 0L
                            category_4 = 0L

                            if (data.childrens ?: 0 > 0) {
                                val request = String.format("https://api-affiliate.icheck.com.vn:6086/categories?parent_id=%s", data.id)
                                viewModel.loadIcheckCategory_3(request)

                                til_category_2.visibility = View.VISIBLE
                                edt_product_danhMuc_cap2.setText("")
                                til_category_3.visibility = View.GONE
                                edt_product_danhMuc_cap3.setText("")
                            } else {
                                til_category_2.visibility = View.GONE
                                edt_product_danhMuc_cap2.setText("")
                                til_category_3.visibility = View.GONE
                                edt_product_danhMuc_cap3.setText("")
                            }
                        }
                    }
                }
            }

            if (level == CATEGORY_LEVEL_3) {
                rv_search.adapter = adapterCategory_3
                adapterCategory_3.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
                    override fun click(position: Int, data: IcheckCategory, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.name ?: ""
                            view.error = null
                            category_3 = data.id
                            category_4 = 0L
                            if (data.childrens ?: 0 > 0) {
                                val request = String.format("https://api-affiliate.icheck.com.vn:6086/categories?parent_id=%s", data.id)
                                viewModel.loadIcheckCategory_4(request)

                                til_category_3.visibility = View.VISIBLE
                                edt_product_danhMuc_cap3.setText("")
                            } else {
                                til_category_3.visibility = View.GONE
                                edt_product_danhMuc_cap3.setText("")
                            }
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_4) {
                rv_search.adapter = adapterCategory_4
                adapterCategory_4.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
                    override fun click(position: Int, data: IcheckCategory, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.name ?: ""
                            view.error = null
                            category_4 = data.id
                        }
                    }
                }
            }

            dialog.show()
        }
    }

    private fun isRequiredFieldsValid(image: List<String>, name: String, price: String, code: String, category_1: Long, category_2: Long, category_3: Long, category_4: Long, moTa: String): Boolean {
        if (image.isEmpty()) {
            toast("Ảnh sản phẩm không được để trống")
            return false
        }

        if (name.trim().isEmpty()) {
            toast("Tên không được để trống")
            edit_product_tenSp.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_tenSp)
            return false
        }

        if (code.trim().isEmpty()) {
            toast("Mã sản phẩm không được để trống")
            edit_product_maSp.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_maSp)
            return false
        }

        if (price.trim().isEmpty()) {
            toast("Giá bản lẻ không được để trống")
            edit_product_giaBan.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_giaBan)
            return false
        }

        if (moTa.trim().isEmpty()) {
            toast("Mô tả sản phẩm không được để trống")
            edit_product_moTa.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_moTa)
            return false
        }

        if (category_1 == 0L) {
            toast("Danh mục không được để trống")
            edt_product_danhMuc.error = getString(R.string.error_field_required)
            edt_product_danhMuc.requestFocus()
            return false
        }

        if (til_category_1.visibility == View.VISIBLE)
            if (category_2 == 0L) {
                toast("Danh mục không được để trống")
                edt_product_danhMuc_cap1.error = getString(R.string.error_field_required)
                edt_product_danhMuc_cap1.requestFocus()
                return false
            }

        if (til_category_2.visibility == View.VISIBLE)
            if (category_3 == 0L) {
                toast("Danh mục không được để trống")
                edt_product_danhMuc_cap2.error = getString(R.string.error_field_required)
                edt_product_danhMuc_cap2.requestFocus()
                return false
            }

        if (til_category_3.visibility == View.VISIBLE)
            if (category_4 == 0L) {
                toast("Danh mục không được để trống")
                edt_product_danhMuc_cap3.error = getString(R.string.error_field_required)
                edt_product_danhMuc_cap3.requestFocus()
                return false
            }

        return true
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun setupImageRecycleview() {
        context?.let {
            rv_product_images.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            rv_product_images.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_product_images.adapter = adapterImages
            adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    postMedias.remove(data)
                    if (postMedias.isEmpty()) rv_product_images.visibility = View.GONE
                    adapterImages.replaceAll(postMedias)
                }
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private var sendingPhotoUri: Uri? = null
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )
    }

    private fun takePhoto() {
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {

            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA) }

        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(context?.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.e("Hong", "khong the tao file", ex)
                }
                photoFile?.let {
                    val photoURI = FileProvider.getUriForFile(context!!, getString(R.string.file_provider_authority), it)
                    sendingPhotoUri = photoURI

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, Const.RequestCode.TAKE_PICTURE)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(IcheckProductViewModel::class.java, false)
        viewModel.errorSignal.observe(this, android.arch.lifecycle.Observer { baseErrorSignal ->
            baseErrorSignal?.let {
                resolveError(it)
            }
        })
        viewModel.updateIcheckSucccess.observe(this, android.arch.lifecycle.Observer {
            toast("Mọi đóng góp sẽ được gửi tới nhà phát triển, cảm ơn bạn đã bổ sung thông tin sản phẩm")
            activity?.finish()
        })

        viewModel.dataCategory.observe(this, android.arch.lifecycle.Observer {
            edt_product_danhMuc.visibility = View.VISIBLE
            it?.let { it1 -> adapterCategory_1.replaceAll(it1) }
        })

        viewModel.dataCategory_2.observe(this, android.arch.lifecycle.Observer {
            edt_product_danhMuc_cap1.visibility = View.VISIBLE
            it?.let { it1 -> adapterCategory_2.replaceAll(it1) }
        })

        viewModel.dataCategory_3.observe(this, android.arch.lifecycle.Observer {
            edt_product_danhMuc_cap2.visibility = View.VISIBLE
            it?.let { it1 -> adapterCategory_3.replaceAll(it1) }
        })

        viewModel.dataCategory_4.observe(this, android.arch.lifecycle.Observer {
            edt_product_danhMuc_cap3.visibility = View.VISIBLE
            it?.let { it1 -> adapterCategory_4.replaceAll(it1) }
        })

        viewModel.loadIcheckCategory("https://api-affiliate.icheck.com.vn:6086/categories")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
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
        if (requestCode == Const.RequestCode.TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            sendingPhotoUri?.let {
                val postMedia = PostMedia()

                postMedia.uri = it
                postMedias.add(postMedia)
                adapterImages.replaceAll(postMedias)
                rv_product_images.visibility = View.VISIBLE
            }
        }
    }

    interface ProductDetailProvider {

        fun provideProductImage(): String
        fun provideProductName(): CharSequence
        fun provideProductPrice(): CharSequence
        fun provideProductBarCode(): CharSequence
        fun provideProductVendor(): IcheckVendor?
    }

    class ProductDetailConverter : Converter<IcheckProduct, ProductDetailProvider> {

        override fun convert(from: IcheckProduct): ProductDetailProvider {
            return object : ProductDetailProvider {

                override fun provideProductImage(): String {
                    return "http://ucontent.icheck.vn/" + from.imageDefault + "_medium.jpg"
                }

                override fun provideProductName(): CharSequence {
                    return from.productName ?: ""
                }

                override fun provideProductPrice(): CharSequence {
                    return from.priceDefault.toString()

                }

                override fun provideProductBarCode(): CharSequence {
                    return from.code ?: ""
                }

                override fun provideProductVendor(): IcheckVendor? {
                    return from.vendor
                }

            }
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Bổ sung thông tin sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }
}