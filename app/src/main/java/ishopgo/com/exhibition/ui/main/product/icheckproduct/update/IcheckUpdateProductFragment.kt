package ishopgo.com.exhibition.ui.main.product.icheckproduct.update

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import ishopgo.com.exhibition.R
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
    }

    private var data: IcheckProduct? = null
    private var image = ""
    private var code = ""
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
            viewModel.updateIcheckProduct("", -1L, "", mutableListOf(), -1L, -1L, -1L, -1L,
                    "", "", "", "", -1L)
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
        }

        if (code.isNotBlank()) {
            edit_product_maSp.setText(code)
        }
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
            toast("Thành công")
        })
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