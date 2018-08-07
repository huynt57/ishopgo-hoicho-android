package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
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
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_ID
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.model.product_manager.ProductRelated
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.*
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointProductAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_product_manager_add.*
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProductManagerDetailFragment : BaseFragment() {
    private lateinit var viewModel: ProductManagerViewModel
    private var product_Id: Long = 0L
    private var booth_id: Long = 0L
    private var feautured: Int = STATUS_NOT_FEAUTURED
    private var status: Int = STATUS_DISPLAY_SHOW
    private var postMedias = ArrayList<PostMedia>()
    private var postMediasCert = ArrayList<PostMedia>()
    private var listImageDelete = ArrayList<PostMedia>()
    private var adapterImages = ComposingPostMediaAdapter()
    private var adapterImagesCert = ComposingPostMediaAdapter()
    private var image: String = ""
    private var imageOld = ""
    private var requestBrands = ""
    private val adapterBrands = BrandsAdapter()
    private val adapterBooth = BoothAdapter()
    private var reloadBrands = false
    private var reloadProvider = false
    private var isEditMode = false
    private val adapterCategory = CategoryAdapter()
    private val adapterCategory_1 = CategoryAdapter()
    private val adapterCategory_2 = CategoryAdapter()
    private val adapterCategory_3 = CategoryAdapter()
    private val adapterCategory_4 = CategoryAdapter()
    private var listCategory = mutableListOf<Category>()
    private var listProductRelated = mutableListOf<Product>()
    private var listProductVatTu = mutableListOf<Product>()
    private var listProductGiaiPhap = mutableListOf<Product>()
    private var nkxs: Int = NKSX_DISPLAY_HIDDEN
    private var baoTieu: Int = ACCREDITATINON_DISPLAY_HIDDEN
    private var moTa: String = ""
    private val adapterDonViSanXuat = BoothAdapter()
    private val adapterDonViNhapKhau = BoothAdapter()
    private val adapterCoSoCheBien = BoothAdapter()
    private var listDescriptionCSCB: MutableList<Description> = ArrayList()
    private var listDescriptionVatTu: MutableList<Description> = ArrayList()
    private var listDescriptionGiaiPhap: MutableList<Description> = ArrayList()
    private val handleOverwrite: ProductManagerDetailOverwrite = CustomProductManagerDetail()
    private var adapterDescriptionCSCB = DescriptionAdapter()
    private var adapterDescriptionVatTu = DescriptionAdapter()
    private var adapterDescriptionGiaiPhap = DescriptionAdapter()
    private var thuongHieuId: Long = 0L
    private var gianHangId: Long = 0L
    private var donViSXId: Long = 0L
    private var donViNKId: Long = 0L
    private var coSoCBId: Long = 0L
    private var typeCamera = 0
    private var typeImages = 0
    private var adapterProductRelatedImage = SalePointProductAdapter(0.4f)
    private var adapterVatTu = SalePointProductAdapter(0.4f)
    private var adapterGiaiPhap = SalePointProductAdapter(0.4f)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_manager_add, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product_Id = arguments?.getLong(EXTRA_ID, -1L) ?: -1L
        handleOverwrite.handleOnCreate(product_Id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()

        btn_product_add.text = "Cập nhật"

        view_camera.setOnClickListener {
            CASE_TAKE_PHOTO = false
            typeCamera = TYPE_CAMERA_IMAGES
            takePhoto()
        }

        view_add_images.setOnClickListener {
            CASE_PICK_IMAGE = false
            typeImages = TYPE_SELECTED_IMAGES
            launchPickPhotoIntent()
        }

        view_camera_cert.setOnClickListener {
            CASE_TAKE_PHOTO = false
            typeCamera = TYPE_CAMERA_CERT
            takePhoto()
        }

        view_add_images_cert.setOnClickListener {
            CASE_PICK_IMAGE = false
            typeImages = TYPE_SELECTED_CERT
            launchPickPhotoIntent()
        }

        if (UserDataManager.currentType == "Chủ hội chợ" || UserDataManager.currentType == "Chủ gian hàng")
            btn_product_add.visibility = View.VISIBLE
        else if (UserDataManager.currentType == "Quản trị viên") {
            val listPermission = Const.listPermission

            if (listPermission.isNotEmpty())
                for (i in listPermission.indices)
                    if (Const.Permission.EDIT_PRODUCT == listPermission[i]) {
                        btn_product_add.visibility = View.VISIBLE
                        break
                    }

        } else btn_product_add.visibility = View.GONE

        edit_product_thuongHieu.setOnClickListener { getBrands(edit_product_thuongHieu) }
        edit_product_gianHang.setOnClickListener { getBooth(edit_product_gianHang, TYPE_DONVI_PHANPHOI) }
        edit_product_donViSX.setOnClickListener { getBooth(edit_product_donViSX, TYPE_DONVI_SANXUAT) }
        edit_product_donViNK.setOnClickListener { getBooth(edit_product_donViNK, TYPE_DONVI_NHAPKHAU) }
        edit_product_CosoCB.setOnClickListener { getBooth(edit_product_CosoCB, TYPE_COSO_CHEBIEN) }

        edt_product_danhMuc.setOnClickListener { getCategory(edt_product_danhMuc, CATEGORY_LEVEL_PARENT) }
        edt_product_danhMuc_cap1.setOnClickListener { getCategory(edt_product_danhMuc_cap1, CATEGORY_LEVEL_1) }
        edt_product_danhMuc_cap2.setOnClickListener { getCategory(edt_product_danhMuc_cap2, CATEGORY_LEVEL_2) }
        edt_product_danhMuc_cap3.setOnClickListener { getCategory(edt_product_danhMuc_cap3, CATEGORY_LEVEL_3) }
        edt_product_danhMuc_cap4.setOnClickListener { getCategory(edt_product_danhMuc_cap4, CATEGORY_LEVEL_4) }

        btn_add_general_coSo_cB.setOnClickListener { toast("Đang phát triển") }
        btn_vatTu.setOnClickListener { toast("Đang phát triển") }
        btn_giaiPhap.setOnClickListener { toast("Đang phát triển") }

        btn_add_description_coSo_cB.setOnClickListener { dialogAddDescription(ADD_DESCRIPTION_COSOCB, null) }
        btn_add_description_vatTu.setOnClickListener { dialogAddDescription(ADD_DESCRIPTION_VATTU, tv_nguyenLieu_vatTu) }
        btn_add_description_giaiPhap.setOnClickListener { dialogAddDescription(ADD_DESCRIPTION_GIAIPHAP, tv_giaiPhap) }

        tv_nguyenLieu_vatTu.setOnClickListener { dialogChangeName(tv_nguyenLieu_vatTu) }
        tv_giaiPhap.setOnClickListener { dialogChangeName(tv_giaiPhap) }
        tv_lienQuan.setOnClickListener { dialogChangeName(tv_lienQuan) }

        btn_product_add.setOnClickListener {
            val tenSp = edit_product_tenSp.text.toString()
            val maSp = edit_product_maSp.text.toString()
            val dvt = edit_product_dvt.text.toString()
            val xuatSu = edt_product_xuatSu.text.toString()
            val ngayDongGoi = edt_product_ngayDongGoi.text.toString()
            val quyCachDongGoi = edt_product_quyCach_dongGoi.text.toString()
            val hsd = edt_product_hsd.text.toString()
            val giaBan = edit_product_giaBan?.money ?: 0
            val giaBanKm = edit_product_giaKm?.money ?: 0
            val giaBanSiTu = edit_product_giaBanSi_tu?.money ?: 0
            val giaBanSiDen = edit_product_giaBanSi_den?.money ?: 0
            val soLuongBanSi = edit_product_soLuongSi.text.toString()

            val maSoLoSX = edit_product_maSoLoSanXuat.text.toString()
            val ngaySX = edit_product_ngaySX.text.toString()
            val ngayThuHoachDK = edit_product_ngayThuHoach.text.toString()
            val quyMo = edit_product_quyMoSX.text.toString()
            val khaNangCungUng = edit_product_khaNang_cungUng.text.toString()
            val muaVu = edit_product_muaVu_sanXuat.text.toString()

            val msLoHang = edit_product_maSoLoHang.text.toString()
            val cangXuat = edit_product_cangXuat.text.toString()
            val cangNhap = edit_product_cangNhap.text.toString()
            val ngayXuatHang = edit_product_ngayXuatHang.text.toString()
            val ngayNhapHang = edit_product_ngayNhapHang.text.toString()
            val soLuongNhap = edit_product_soLuongNhap.text.toString()

            val hinhThucVC = edit_product_hinhThuc_vanChuyen.text.toString()
            val ngayVC = edit_product_ngayVanChuyen.text.toString()
            val donViVC = edit_product_tenDonVi_vanChuyen.text.toString()

            val moTa = edit_product_moTa.text.toString()

            val tenVatTu = tv_nguyenLieu_vatTu.text
            val tenGiaiPhap = tv_giaiPhap.text
            val tenLienQuan = tv_lienQuan.text


            toast("Đang phát triển")
//                if (isRequiredFieldsValid(tenSp, edit_product_price.text.toString(), maSp, edt_product_categories.text.toString(),
//                                edit_product_booth.text.toString(), edit_product_brand.text.toString())) {
//                    showProgressDialog()
//                    viewModel.editProductManager(product_Id, tenSp, maSp, tieuDe,
//                            giaBan, giaBanKm, dvt, booth_id, brand_id, xuatSu,
//                            image, postMedias, moTa, status, metaMota, metaKeyword,
//                            tag, listCategory, listProductRelated, feautured, giaBanSiTu, giaBanSiDen, soLuongBanSi, listImageDelete, quyMo, sanLuong,
//                            dongGoi, muaVu, hsd, msLoHang, ngaySX, ngayThuHoachDK, ngayXuatXuong, nkxs, baoTieu, listProductVatTu, listProductGiaiPhap)
//                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogChoosePicture(image: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_select_image, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()

            val tv_choose_takePhoto = dialog.findViewById(R.id.tv_choose_takePhoto) as VectorSupportTextView
            val tv_choose_album = dialog.findViewById(R.id.tv_choose_album) as VectorSupportTextView
            val tv_choose_viewImages = dialog.findViewById(R.id.tv_choose_viewImages) as VectorSupportTextView

            tv_choose_viewImages.setOnClickListener {
                val listImage = mutableListOf<String>()
                listImage.add(image)
                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                startActivity(intent)
                dialog.dismiss()
            }
            tv_choose_takePhoto.setOnClickListener {
                CASE_TAKE_PHOTO = true
                takePhoto()
                dialog.dismiss()
            }

            tv_choose_album.setOnClickListener {
                CASE_PICK_IMAGE = true
                launchPickPhotoIntent()
                dialog.dismiss()
            }

            dialog.show()
        }
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

    private fun dialogAddDescription(type: Int, view: VectorSupportTextView?) {
        context?.let {
            val title: String = if (type == ADD_DESCRIPTION_COSOCB) "Thêm mô cơ sở chế biến" else "Thêm mô ${view?.text}"

            val dialog = MaterialDialog.Builder(it)
                    .title(title)
                    .customView(R.layout.layout_product_single_description, false)
                    .positiveText("Xong")
                    .onPositive { dialog, _ ->
                        val edit_product_tieuDe = dialog.findViewById(R.id.edit_product_tieuDe) as TextInputEditText
                        val edit_product_moTa = dialog.findViewById(R.id.edit_product_moTa) as TextInputEditText
                        val description = Description()
                        description.title = edit_product_tieuDe.text.toString()
                        description.description = edit_product_moTa.text.toString()
                        if (type == ADD_DESCRIPTION_COSOCB) {
                            listDescriptionCSCB.add(description)
                            adapterDescriptionCSCB.replaceAll(listDescriptionCSCB)
                        }

                        if (type == ADD_DESCRIPTION_VATTU) {
                            listDescriptionVatTu.add(description)
                            adapterDescriptionVatTu.replaceAll(listDescriptionVatTu)
                        }

                        if (type == ADD_DESCRIPTION_GIAIPHAP) {
                            listDescriptionGiaiPhap.add(description)
                            adapterDescriptionGiaiPhap.replaceAll(listDescriptionGiaiPhap)
                        }

                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            dialog.show()
        }
    }

    private fun dialogChangeName(view: VectorSupportTextView?) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thay đổi tiêu đề")
                    .customView(R.layout.dialog_edit_name_product, false)
                    .positiveText("Thay đổi")
                    .onPositive { dialog, _ ->
                        val edit_product_tieuDe = dialog.findViewById(R.id.edit_product_name) as TextInputEditText
                        view?.text = edit_product_tieuDe.text.toString()
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ bỏ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val edit_product_tieuDe = dialog.findViewById(R.id.edit_product_name) as TextInputEditText
            edit_product_tieuDe.setText(view?.text ?: "")
            dialog.show()
        }
    }

    private fun firstLoadCategory() {
        viewModel.loadCategories()
    }

    private fun firstLoadCategoryChild(category: Category, level: Int) {
        viewModel.loadChildCategory(category, level)
    }

    private fun setupSanPhamLienQuan() {
        context?.let {
            adapterProductRelatedImage.replaceAll(listProductRelated)
            rv_related_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_related_products.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_related_products.adapter = adapterProductRelatedImage
            adapterProductRelatedImage.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    listProductRelated.remove(data)
                    adapterProductRelatedImage.replaceAll(listProductRelated)
                }
            }
        }
    }

    private fun setupVatTu() {
        context?.let {
            adapterVatTu.replaceAll(listProductVatTu)
            rv_supplies_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_supplies_products.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_supplies_products.adapter = adapterVatTu
            adapterVatTu.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    listProductVatTu.remove(data)
                    adapterVatTu.replaceAll(listProductVatTu)
                }
            }
        }
    }

    private fun setupGiaiPhap() {
        context?.let {
            adapterGiaiPhap.replaceAll(listProductGiaiPhap)
            rv_solution_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_solution_products.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_solution_products.adapter = adapterGiaiPhap
            adapterGiaiPhap.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    listProductGiaiPhap.remove(data)
                    adapterGiaiPhap.replaceAll(listProductGiaiPhap)
                }
            }
        }
    }

    private fun setupImageRecycleview() {
        rv_product_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_product_images.adapter = adapterImages
        adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
            override fun click(position: Int, data: PostMedia, code: Int) {
                postMedias.remove(data)
                if (data.uri.toString().subSequence(0, 4).contains("http"))
                    listImageDelete.add(data)

                if (postMedias.isEmpty()) rv_product_images.visibility = View.GONE
                adapterImages.replaceAll(postMedias)
            }
        }
    }

    private fun setupImageCertRecycleview() {
        context?.let {
            rv_product_cert.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            rv_product_cert.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_product_cert.adapter = adapterImagesCert
            adapterImagesCert.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    postMediasCert.remove(data)
                    if (postMediasCert.isEmpty()) rv_product_cert.visibility = View.GONE
                    adapterImagesCert.replaceAll(postMediasCert)
                }
            }
        }
    }

    private fun setupRecyclerviewDescriptionCSCB() {
        context?.let {
            rv_description_cscb.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_description_cscb.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_description_cscb.adapter = adapterDescriptionCSCB
            adapterDescriptionCSCB.listener = object : ClickableAdapter.BaseAdapterAction<Description> {
                override fun click(position: Int, data: Description, code: Int) {
                    listDescriptionCSCB.remove(data)
                    adapterDescriptionCSCB.replaceAll(listDescriptionCSCB)
                }
            }
        }
    }

    private fun setupRecyclerviewDescriptionVatTu() {
        context?.let {
            rv_description_vatTu.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_description_vatTu.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_description_vatTu.adapter = adapterDescriptionVatTu
            adapterDescriptionVatTu.listener = object : ClickableAdapter.BaseAdapterAction<Description> {
                override fun click(position: Int, data: Description, code: Int) {
                    listDescriptionVatTu.remove(data)
                    adapterDescriptionVatTu.replaceAll(listDescriptionVatTu)
                }
            }
        }
    }

    private fun setupRecyclerviewDescriptionGiaiPhap() {
        context?.let {
            rv_description_giaiPhap.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_description_giaiPhap.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_description_giaiPhap.adapter = adapterDescriptionGiaiPhap
            adapterDescriptionGiaiPhap.listener = object : ClickableAdapter.BaseAdapterAction<Description> {
                override fun click(position: Int, data: Description, code: Int) {
                    listDescriptionGiaiPhap.remove(data)
                    adapterDescriptionGiaiPhap.replaceAll(listDescriptionGiaiPhap)
                }
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

        handleOverwrite.handleActivityCreated(viewModel, this)

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

        viewModel.dataBooth.observe(this, Observer { p ->
            p.let {
                if (reloadProvider) {
                    it?.let { it1 -> adapterBooth.replaceAll(it1) }
                } else {
                    it?.let { it1 -> adapterBooth.addAll(it1) }
                }
            }
        })

        viewModel.editProductSusscess.observe(this, Observer {
            hideProgressDialog()
            isEditMode = false
            toast("Cập nhật thành công")
            viewModel.getProductDetail(product_Id)
            postMedias.clear()
            listProductRelated.clear()
            listProductVatTu.clear()
            listProductGiaiPhap.clear()
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
        viewModel.getBooth(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadProvider = true
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBooth(loadMore)
    }

    @SuppressLint("SetTextI18n")
    private fun showDetail(info: ProductManagerDetail) {
        val convert = ProductManagerConverter().convert(info)
        hideProgressDialog()

        if (UserDataManager.currentType == "Chủ hội chợ") {
            til_product_booth.visibility = View.VISIBLE
            booth_id = info.providerId ?: -1L

        } else {
            booth_id = UserDataManager.currentUserId
            til_product_booth.visibility = View.GONE
        }

        if (convert.provideViewWholesale()) {
            edit_product_giaBanSi_tu.setText(convert.provideWholesaleFrom())
            edit_product_giaBanSi_den.setText(convert.provideWholesaleTo())
            edit_product_soLuongSi.setText(convert.provideWholesaleCountProduct())
        } else {
            edit_product_giaBanSi_tu.setText("")
            edit_product_giaBanSi_den.setText("")
            edit_product_soLuongSi.setText("")
        }

        if (convert.provideImages().isNotEmpty()) {
            for (i in convert.provideImages()) {
                val postMedia = PostMedia()
                postMedia.uri = Uri.parse(i)
                postMedias.add(postMedia)
            }
            adapterImages.replaceAll(postMedias)

            imageOld = convert.provideImages()[0]

            Glide.with(context)
                    .load(convert.provideImages()[0])
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_image_add_product)

            view_image_add_product.setOnClickListener {
                if (image.isNotEmpty()) {
                    openDialogChoosePicture(image)
                } else
                    openDialogChoosePicture(convert.provideImages()[0])
            }


            val imagesAdapter = ProductManagerDetailImagesAdapter()
            info.images?.let { imagesAdapter.replaceAll(it) }
            rv_product_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_images.adapter = imagesAdapter
            imagesAdapter.listener = object : ClickableAdapter.BaseAdapterAction<String> {
                override fun click(position: Int, data: String, code: Int) {
                    val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, convert.provideImages().toTypedArray())
                    startActivity(intent)
                }
            }
        }

        if (info.categories != null && info.categories!!.isNotEmpty()) {
            listCategory = info.categories!!
            for (i in info.categories!!.indices) {

                if (i == 0) edt_product_danhMuc.setText(info.categories!![i].name)
                if (i == 1) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_1)
                    til_category_1.visibility = View.VISIBLE
                    edt_product_danhMuc_cap1.setText(info.categories!![i].name)
                }
                if (i == 2) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_2)
                    til_category_2.visibility = View.VISIBLE
                    edt_product_danhMuc_cap2.setText(info.categories!![i].name)
                }
                if (i == 3) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_3)
                    til_category_3.visibility = View.VISIBLE
                    edt_product_danhMuc_cap3.setText(info.categories!![i].name)
                }
                if (i == 4) {
                    viewModel.loadChildCategoryDetail(info.categories!![i - 1].id, CATEGORY_LEVEL_4)
                    til_category_4.visibility = View.VISIBLE
                    edt_product_danhMuc_cap4.setText(info.categories!![i].name)
                }
            }
        }

        sw_spNoiBat.isChecked = convert.provideIsFeatured()
        sw_spNoiBat.visibility = if (UserDataManager.currentType == "Chủ hội chợ") View.VISIBLE else View.GONE
        sw_spNoiBat.text = if (convert.provideIsFeatured()) "Sản phẩm nổi bật: Nổi bật"
        else "Sản phẩm nổi bật: Không nổi bật"

        sw_hienThi.isChecked = convert.provideStatus()
        sw_hienThi.text = if (convert.provideStatus()) "Tuỳ chọn hiển thị: Hiển thị dạng chuẩn"
        else "Tuỳ chọn hiển thị: Không hiển thị"

        sw_show_nksx.setOnCheckedChangeListener { _, _ ->
            if (sw_show_nksx.isChecked) {
                nkxs = NKSX_DISPLAY_SHOW
                sw_show_nksx.text = "Nhật ký sản xuất: Bật"
            } else {
                nkxs = NKSX_DISPLAY_HIDDEN
                sw_show_nksx.text = "Nhật ký sản xuất: Tắt"
            }
        }

        sw_show_baoTieu.setOnCheckedChangeListener { _, _ ->
            if (sw_show_baoTieu.isChecked) {
                baoTieu = ACCREDITATINON_DISPLAY_SHOW
                sw_show_baoTieu.text = "Đã được bao tiêu: Đã được bao tiêu"
            } else {
                baoTieu = ACCREDITATINON_DISPLAY_HIDDEN
                sw_show_baoTieu.text = "Đã được bao tiêu: Chưa được bao tiêu"
            }
        }

        sw_hienThi.setOnCheckedChangeListener { _, _ ->
            if (sw_hienThi.isChecked) {
                status = STATUS_DISPLAY_SHOW
                sw_hienThi.text = "Tuỳ chọn hiển thị: Hiển thị dạng chuẩn"
            } else {
                status = STATUS_DISPLAY_HIDDEN
                sw_hienThi.text = "Tuỳ chọn hiển thị: Không hiển thị"
            }
        }

        sw_spNoiBat.setOnCheckedChangeListener { _, _ ->
            if (sw_spNoiBat.isChecked) {
                feautured = STATUS_FEAUTURED
                sw_spNoiBat.text = "Sản phẩm nổi bật: Nổi bật"
            } else {
                feautured = STATUS_NOT_FEAUTURED
                sw_spNoiBat.text = "Sản phẩm nổi bật: Không nổi bật"
            }
        }

        edit_product_tenSp.setText(convert.provideName())
        edit_product_maSp.setText(convert.provideCode())
        edit_product_dvt.setText(convert.provideDVT())
        edt_product_xuatSu.setText(convert.provideMadeIn())
        edit_product_giaBan.setText(convert.providePrice())
        edit_product_giaKm.setText(convert.providerPricePromotion())
        edit_product_giaBanSi_tu.setText(convert.provideWholesaleFrom())
        edit_product_giaBanSi_den.setText(convert.provideWholesaleTo())
        edit_product_soLuongSi.setText(convert.provideWholesaleCountProduct())
        edt_product_ngayDongGoi.setText(convert.providerNgayDongGoi())
        edt_product_quyCach_dongGoi.setText(convert.providerQuyCachDongGoi())
        edt_product_hsd.setText(convert.providerHSD())

        edit_product_maSoLoSanXuat.setText(convert.providerMaSoLoSX())
        edit_product_ngaySX.setText(convert.providerNgaySX())
        edit_product_ngayThuHoach.setText(convert.providerNgayThuHoachDK())
        edit_product_quyMoSX.setText(convert.providerQuyMo())
        edit_product_khaNang_cungUng.setText(convert.providerKhaNangCungUng())
        edit_product_muaVu_sanXuat.setText(convert.providerMuaVu())

        edit_product_maSoLoHang.setText(convert.providerMsLoHang())
        edit_product_cangXuat.setText(convert.providerCangXuat())
        edit_product_cangNhap.setText(convert.providerCangNhap())
        edit_product_ngayXuatHang.setText(convert.providerNgayXuatHang())
        edit_product_ngayNhapHang.setText(convert.providerNgayNhapHang())
        edit_product_soLuongNhap.setText(convert.providerSoLuongNhap())

        edit_product_hinhThuc_vanChuyen.setText(convert.providerHinhThucVC())
        edit_product_ngayVanChuyen.setText(convert.providerNgayVC())
        edit_product_tenDonVi_vanChuyen.setText(convert.providerDonViVC())

        for (i in convert.providerInfo().indices) {
            val data = convert.providerInfo()[i]
            if (i == 0) {
                tv_nguyenLieu_vatTu.text = data.name
                if (data.descriptions?.isNotEmpty() == true)
                    listDescriptionVatTu = data.descriptions ?: mutableListOf()

                if (convert.providerInfo()[i].products?.data?.isNotEmpty() == true)
                    listProductVatTu = data.products?.data ?: mutableListOf()
            }
            if (i == 1) {
                tv_giaiPhap.text = convert.providerInfo()[i].name
                if (convert.providerInfo()[i].descriptions?.isNotEmpty() == true)
                    listDescriptionGiaiPhap = convert.providerInfo()[i].descriptions ?: mutableListOf()
                if (convert.providerInfo()[i].products?.data?.isNotEmpty() == true)
                    listProductGiaiPhap = data.products?.data ?: mutableListOf()
            }
            if (i == 2) {
                tv_lienQuan.text = convert.providerInfo()[i].name
                if (convert.providerInfo()[i].products?.data?.isNotEmpty() == true)
                    listProductRelated = data.products?.data ?: mutableListOf()
            }
        }

        moTa = convert.provideDescription()
        if (moTa.isNotEmpty()) {
            container_product_detail.visibility = View.VISIBLE
            til_product_moTa.visibility = View.GONE

            try {
                val css = IOUtils.toString(context?.assets?.open("WebViewStyle.css"), "UTF-8")
                val fullHtml = String.format(
                        "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                        css,
                        convert.provideDescription()
                )
                webView_spk_detail_description.loadData(fullHtml, "text/html; charset=UTF-8", null)
            } catch (e: IOException) {
                e.printStackTrace()
                webView_spk_detail_description.loadData(convert.provideDescription(), "text/html; charset=UTF-8", null)
            }

            tv_view_details.setOnClickListener(
                    {
                        val i = Intent(context, FullDetailActivity::class.java)
                        i.putExtra(Const.TransferKey.EXTRA_JSON, convert.provideDescription())
                        startActivity(i)
                    })

        } else {
            container_product_detail.visibility = View.GONE
            til_product_moTa.visibility = View.VISIBLE

        }
        if (convert.provideDepartments() != null) {
            thuongHieuId = convert.provideDepartments()!!.id
            edit_product_thuongHieu.setText(convert.provideDepartments()!!.name ?: "")
        }

        if (convert.providerPP() != null) {
            gianHangId = convert.providerPP()!!.id
            edit_product_gianHang.setText(convert.providerPP()!!.name ?: "")
        }

        if (convert.providerNNK() != null) {
            donViNKId = convert.providerNNK()!!.id
            edit_product_thuongHieu.setText(convert.providerNNK()!!.name ?: "")
        }

        if (convert.providerCSCB() != null) {
            coSoCBId = convert.providerCSCB()!!.id
            edit_product_CosoCB.setText(convert.providerCSCB()!!.name ?: "")
            val data = convert.providerCSCB()?.descriptions
            if (data?.isNotEmpty() == true)
                listDescriptionCSCB = data
        }

        if (convert.providerBooth() != null) {
            donViSXId = convert.providerBooth()!!.id
            edit_product_donViSX.setText(convert.providerBooth()!!.name ?: "")
        }

        setupImageRecycleview()
        setupImageCertRecycleview()
        setupRecyclerviewDescriptionCSCB()
        setupRecyclerviewDescriptionVatTu()
        setupRecyclerviewDescriptionGiaiPhap()
        setupSanPhamLienQuan()
        setupVatTu()
        setupGiaiPhap()
    }

    interface ProductManagerDetailProvider {
        fun provideName(): String
        fun provideTitle(): String
        fun provideDVT(): String
        fun provideCode(): String
        fun provideProviderPrice(): String
        fun providePrice(): String
        fun provideStatus(): Boolean
        fun provideMadeIn(): String
        fun provideDescription(): String
        fun provideCollectionProducts(): ProductRelated?
        fun provideImages(): List<String>
        fun provideDepartments(): Brand?
        fun provideCategory(): List<Category>?
        fun provideLink(): String
        fun provideIsFeatured(): Boolean
        fun provideViewWholesale(): Boolean
        fun provideWholesaleFrom(): String
        fun provideWholesaleTo(): String
        fun provideWholesaleCountProduct(): String
        fun providerBooth(): Booth?
        fun providerPricePromotion(): String
        fun providerHSD(): String
        fun providerNgayDongGoi(): String
        fun providerQuyCachDongGoi(): String
        fun providerMaSoLoSX(): String
        fun providerNgaySX(): String
        fun providerNgayThuHoachDK(): String
        fun providerQuyMo(): String
        fun providerKhaNangCungUng(): String
        fun providerMuaVu(): String
        fun providerMsLoHang(): String
        fun providerCangXuat(): String
        fun providerCangNhap(): String
        fun providerNgayXuatHang(): String
        fun providerNgayNhapHang(): String
        fun providerSoLuongNhap(): String
        fun providerHinhThucVC(): String
        fun providerNgayVC(): String
        fun providerDonViVC(): String
        fun providerInfo(): List<InfoProduct>
        fun providerPP(): Booth?
        fun providerNNK(): Booth?
        fun providerCSCB(): Booth?

    }

    class ProductManagerConverter : Converter<ProductManagerDetail, ProductManagerDetailProvider> {

        override fun convert(from: ProductManagerDetail): ProductManagerDetailProvider {
            return object : ProductManagerDetailProvider {
                override fun providerPP(): Booth? {
                    return from.pp
                }

                override fun providerNNK(): Booth? {
                    return from.nnk
                }

                override fun providerCSCB(): Booth? {
                    return from.cscb
                }

                override fun providerInfo(): List<InfoProduct> {
                    return from.info ?: mutableListOf()
                }

                override fun providerHSD(): String {
                    return from.hsd ?: ""
                }

                override fun providerNgayDongGoi(): String {
                    return from.ngayDonggoi ?: ""
                }

                override fun providerQuyCachDongGoi(): String {
                    return from.ngayDonggoi ?: ""
                }

                override fun providerMaSoLoSX(): String {
                    return from.msSanxuat ?: ""
                }

                override fun providerNgaySX(): String {
                    return from.ngaySx ?: ""
                }

                override fun providerNgayThuHoachDK(): String {
                    return from.dkThuhoach ?: ""
                }

                override fun providerQuyMo(): String {
                    return from.quyMo ?: ""
                }

                override fun providerKhaNangCungUng(): String {
                    return from.providerName ?: ""
                }

                override fun providerMuaVu(): String {
                    return from.muaVu ?: ""
                }

                override fun providerMsLoHang(): String {
                    return from.msLohang ?: ""
                }

                override fun providerCangXuat(): String {
                    return from.cangXuat ?: ""
                }

                override fun providerCangNhap(): String {
                    return from.cangNhap ?: ""
                }

                override fun providerNgayXuatHang(): String {
                    return from.xuatXuongdate ?: ""
                }

                override fun providerNgayNhapHang(): String {
                    return from.nhapHangdate ?: ""
                }

                override fun providerSoLuongNhap(): String {
                    return from.slNhap ?: ""
                }

                override fun providerHinhThucVC(): String {
                    return from.hinhThucVc ?: ""
                }

                override fun providerNgayVC(): String {
                    return from.ngayVc ?: ""
                }

                override fun providerDonViVC(): String {
                    return from.donviVc ?: ""
                }

                override fun providerPricePromotion(): String {
                    return from.promotionPrice.toString()
                }

                override fun providerBooth(): Booth? {
                    return from.booth
                }

                override fun provideWholesaleFrom(): String {
                    return from.wholesalePriceFrom.toString()
                }

                override fun provideWholesaleTo(): String {
                    return from.wholesalePriceTo.toString()
                }

                override fun provideWholesaleCountProduct(): String {
                    return from.wholesaleCountProduct.toString()
                }

                override fun provideCategory(): List<Category>? {
                    return from.categories ?: mutableListOf()
                }

                override fun provideViewWholesale(): Boolean {
                    return from.viewWholesale == VIEW_WHOLESALE
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideTitle(): String {
                    return from.title ?: ""
                }

                override fun provideDVT(): String {
                    return from.dvt ?: ""
                }

                override fun provideCode(): String {
                    return from.code ?: ""
                }

                override fun provideProviderPrice(): String {
                    return from.providerPrice.toString()
                }

                override fun providePrice(): String {
                    return from.price.toString()
                }

                override fun provideStatus(): Boolean {
                    return from.status == STATUS_DISPLAY_SHOW
                }

                override fun provideMadeIn(): String {
                    return from.madeIn ?: ""
                }

                override fun provideDescription(): String {
                    return from.description ?: ""
                }

                override fun provideCollectionProducts(): ProductRelated? {
                    return from.collectionProducts
                }

                override fun provideImages(): List<String> {
                    return from.images ?: mutableListOf()
                }

                override fun provideDepartments(): Brand? {
                    return from.department
                }

                override fun provideLink(): String {
                    return from.link ?: ""
                }


                override fun provideIsFeatured(): Boolean {
                    return wasIsFeatured()
                }

                fun wasIsFeatured() = from.isFeatured == IS_FEATURED

            }
        }
    }


//    @SuppressLint("SetTextI18n")
//    private fun stopEditing() {
//
//        if (image.isNotEmpty()) {
//            val listImage = mutableListOf<String>()
//            listImage.add(image)
//            view_image_product_detail.setOnClickListener {
//                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
//                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
//                startActivity(intent)
//            }
//        } else {
//            val listImage = mutableListOf<String>()
//            listImage.add(imageOld)
//            view_image_product_detail.setOnClickListener {
//                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
//                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
//                startActivity(intent)
//            }
//        }
//
//    }

//    @SuppressLint("SetTextI18n")
//    private fun startEditing() {
//
////        setupImageRecycleview()
//        btn_product_update.text = "Xong"
//    }

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
                if (typeImages == TYPE_SELECTED_IMAGES)
                    postMedias.add(postMedia)
                if (typeImages == TYPE_SELECTED_CERT)
                    postMediasCert.add(postMedia)
            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    if (typeImages == TYPE_SELECTED_IMAGES)
                        postMedias.add(postMedia)
                    if (typeImages == TYPE_SELECTED_CERT)
                        postMediasCert.add(postMedia)
                }
            }
            if (typeImages == TYPE_SELECTED_IMAGES) {
                adapterImages.replaceAll(postMedias)
                rv_product_images.visibility = View.VISIBLE
            }
            if (typeImages == TYPE_SELECTED_CERT) {
                adapterImagesCert.replaceAll(postMediasCert)
                rv_product_cert.visibility = View.VISIBLE
            }
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
                    .into(view_image_add_product)
        }

        if (requestCode == Const.RequestCode.TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            if (CASE_TAKE_PHOTO) {
                sendingPhotoUri?.let {
                    Glide.with(context)
                            .load(it)
                            .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder))
                            .into(view_image_add_product)
                }
            } else
                sendingPhotoUri?.let {
                    val postMedia = PostMedia()

                    postMedia.uri = it
                    if (typeCamera == TYPE_CAMERA_IMAGES) {
                        postMedias.add(postMedia)
                        adapterImages.replaceAll(postMedias)
                        rv_product_images.visibility = View.VISIBLE
                    }
                    if (typeCamera == TYPE_CAMERA_CERT) {
                        postMediasCert.add(postMedia)
                        adapterImagesCert.replaceAll(postMediasCert)
                        rv_product_cert.visibility = View.VISIBLE
                    }
                }
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
                adapterCategory.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                    override fun click(position: Int, data: Category, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.name ?: ""
                            view.error = null
                            listCategory.clear()
                            firstLoadCategoryChild(data, CATEGORY_LEVEL_1)
                            listCategory.add(data)
                            til_category_1.visibility = View.VISIBLE
                            edt_product_danhMuc_cap1.setText("")
                            til_category_2.visibility = View.GONE
                            edt_product_danhMuc_cap2.setText("")
                            til_category_3.visibility = View.GONE
                            edt_product_danhMuc_cap3.setText("")
                            til_category_4.visibility = View.GONE
                            edt_product_danhMuc_cap4.setText("")
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_1) {
                rv_search.adapter = adapterCategory_1
                adapterCategory_1.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                    override fun click(position: Int, data: Category, code: Int) {
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
                            view.text = data.name ?: ""
                            view.error = null
                            firstLoadCategoryChild(data, CATEGORY_LEVEL_2)
                            listCategory.add(data)
                            til_category_2.visibility = View.VISIBLE
                            edt_product_danhMuc_cap2.setText("")
                            til_category_3.visibility = View.GONE
                            edt_product_danhMuc_cap3.setText("")
                            til_category_4.visibility = View.GONE
                            edt_product_danhMuc_cap4.setText("")
                        }
                    }
                }
            }

            if (level == CATEGORY_LEVEL_2) {
                rv_search.adapter = adapterCategory_2
                adapterCategory_2.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                    override fun click(position: Int, data: Category, code: Int) {
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
                            view.text = data.name ?: ""
                            view.error = null
                            firstLoadCategoryChild(data, CATEGORY_LEVEL_3)
                            listCategory.add(data)
                            til_category_3.visibility = View.VISIBLE
                            edt_product_danhMuc_cap3.setText("")
                            til_category_4.visibility = View.GONE
                            edt_product_danhMuc_cap4.setText("")
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_3) {
                rv_search.adapter = adapterCategory_3
                adapterCategory_3.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                    override fun click(position: Int, data: Category, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            if (listCategory.size >= 4)
                                for (i in 3 until listCategory.size) {
                                    if (i == 3)
                                        listCategory.removeAt(i)
                                    if (i == 4)
                                        listCategory.removeAt(i - 1)
                                }
                            view.text = data.name ?: ""
                            view.error = null
                            firstLoadCategoryChild(data, CATEGORY_LEVEL_4)
                            listCategory.add(data)
                            til_category_4.visibility = View.VISIBLE
                            edt_product_danhMuc_cap4.setText("")
                        }
                    }
                }
            }
            if (level == CATEGORY_LEVEL_4) {
                rv_search.adapter = adapterCategory_4
                adapterCategory_4.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
                    override fun click(position: Int, data: Category, code: Int) {
                        context?.let {
                            dialog.dismiss()
                            view.text = data.name ?: ""
                            view.error = null
                            listCategory.add(data)
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
                        thuongHieuId = data.id
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getBooth(view: TextView, type: Int) {
        context?.let {
            var title = ""
            if (type == TYPE_DONVI_PHANPHOI)
                title = "Chọn đơn vị phân phối"
            if (type == TYPE_DONVI_SANXUAT)
                title = "Chọn đơn vị sản xuất"
            if (type == TYPE_DONVI_NHAPKHAU)
                title = "Chọn đơn vị nhập khẩu"
            if (type == TYPE_COSO_CHEBIEN)
                title = "Chọn cơ sở chế biến"

            val dialog = MaterialDialog.Builder(it)
                    .title(title)
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

            if (type == TYPE_DONVI_PHANPHOI)
                rv_search.adapter = adapterBooth
            if (type == TYPE_DONVI_SANXUAT)
                rv_search.adapter = adapterDonViSanXuat
            if (type == TYPE_DONVI_NHAPKHAU)
                rv_search.adapter = adapterDonViNhapKhau
            if (type == TYPE_COSO_CHEBIEN)
                rv_search.adapter = adapterCoSoCheBien

            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        gianHangId = data.id
                        view.text = data.boothName ?: ""
                        view.error = null
                    }
                }
            }

            adapterDonViSanXuat.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        donViSXId = data.id
                        view.text = data.boothName ?: ""
                        view.error = null
                    }
                }
            }

            adapterDonViNhapKhau.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        donViNKId = data.id
                        view.text = data.boothName ?: ""
                        view.error = null
                    }
                }
            }

            adapterCoSoCheBien.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        coSoCBId = data.id
                        view.text = data.boothName ?: ""
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

    private fun isRequiredFieldsValid(name: String, price: String, code: String, category: String, provider: String, brand: String): Boolean {
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



        if (category.trim().isEmpty()) {
            toast("Danh mục không được để trống")
            edt_product_danhMuc.error = getString(R.string.error_field_required)
            edt_product_danhMuc.requestFocus()
            return false
        }

        if (provider.trim().isEmpty()) {
            toast("Nhà cung cấp không được để trống")
            edit_product_gianHang.error = getString(R.string.error_field_required)
            edit_product_gianHang.requestFocus()
            return false
        }

        if (brand.trim().isEmpty()) {
            toast("Thương hiệu không được để trống")
            edit_product_thuongHieu.error = getString(R.string.error_field_required)
            edit_product_thuongHieu.requestFocus()
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
        var CASE_TAKE_PHOTO: Boolean = true // true = Chụp ảnh từ camera, false = Chọn ảnh từ bộ sưu tập

        const val STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        const val STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dang landing page
        const val STATUS_DISPLAY_HIDDEN: Int = 0 //Không hiển thị

        const val IS_FEATURED: Int = 1  //Sản phẩm nổi bật

        const val VIEW_WHOLESALE = 1

        const val NKSX_DISPLAY_SHOW: Int = 1 //Hiển thị NKSX
        const val NKSX_DISPLAY_HIDDEN: Int = 0 //Không hiển thị NKSX

        const val ACCREDITATINON_DISPLAY_SHOW: Int = 1 //Hiển thị bao tiêu
        const val ACCREDITATINON_DISPLAY_HIDDEN: Int = 0 //Không hiển thị bao

        const val TYPE_DONVI_PHANPHOI: Int = 0
        const val TYPE_DONVI_SANXUAT: Int = 1
        const val TYPE_DONVI_NHAPKHAU: Int = 2
        const val TYPE_COSO_CHEBIEN: Int = 3

        const val ADD_DESCRIPTION_COSOCB: Int = 0
        const val ADD_DESCRIPTION_VATTU: Int = 1
        const val ADD_DESCRIPTION_GIAIPHAP: Int = 2

        const val PERMISSIONS_REQUEST_CAMERA = 100

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

        const val TYPE_CAMERA_IMAGES = 0
        const val TYPE_CAMERA_CERT = 1

        const val TYPE_SELECTED_IMAGES = 0
        const val TYPE_SELECTED_CERT = 1
    }
}