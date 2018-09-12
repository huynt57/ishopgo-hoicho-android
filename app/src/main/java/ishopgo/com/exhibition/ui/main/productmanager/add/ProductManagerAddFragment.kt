package ishopgo.com.exhibition.ui.main.productmanager.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.UserManager
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.content.res.AppCompatResources
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
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.brandmanager.search.BrandSearchViewModel
import ishopgo.com.exhibition.ui.main.home.search.brands.SearchBrandsViewModel
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.search_product.SearchProductManagerViewModel
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointProductAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_product_manager_add.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductManagerAddFragment : BaseFragment() {
    private lateinit var viewModel: ProductManagerViewModel
    private lateinit var searchProductViewModel: SearchProductManagerViewModel
    private lateinit var searchBrandsViewModel: BrandSearchViewModel
    private val adapterBrands = BrandsAdapter()
    private val adapterBooth = BoothAdapter()
    private val adapterDonViCungUng = BoothAdapter()
    private val adapterDonViCungUng_2 = BoothAdapter()
    private val adapterDonViSanXuat = BoothAdapter()
    private val adapterDonViNhapKhau = BoothAdapter()
    private val adapterCoSoCheBien = BoothAdapter()
    private val adapterCategory = CategoryAdapter()
    private val adapterCategory_1 = CategoryAdapter()
    private val adapterCategory_2 = CategoryAdapter()
    private val adapterCategory_3 = CategoryAdapter()
    private val adapterCategory_4 = CategoryAdapter()
    private var adapterProductRelatedImage = SalePointProductAdapter(0.4f)
    private var adapterVatTu = SalePointProductAdapter(0.4f)
    private var adapterGiaiPhap = SalePointProductAdapter(0.4f)
    private var listCategory = ArrayList<Category>()
    private var listRelatedShop = ArrayList<BoothManager>()

    private var reloadBrands = false
    private var reloadProvider = false
    private var trangThaiHT: Int = STATUS_DISPLAY_SHOW
    private var spNoiBat: Int = STATUS_NOT_FEAUTURED
    private var nkxs: Int = NKSX_DISPLAY_HIDDEN
    private var baoTieu: Int = ACCREDITATINON_DISPLAY_HIDDEN
    private var image: String = ""
    private var thuongHieuId: Long = 0L
    private var gianHangId: Long = 0L
    private var donViSXId: Long = 0L
    private var donViNKId: Long = 0L
    private var coSoCBId: Long = 0L
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var postMediasCert: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private var adapterImagesCert = ComposingPostMediaAdapter()
    private var adapterDescriptionCSCB = DescriptionAdapter()
    private var adapterDescriptionVatTu = DescriptionAdapter()
    private var adapterDescriptionGiaiPhap = DescriptionAdapter()
    private var listProductRelated: ArrayList<Product> = ArrayList()
    private var listVatTu: ArrayList<Product> = ArrayList()
    private var listGiaiPhap: ArrayList<Product> = ArrayList()
    private var listDescriptionCSCB: ArrayList<Description> = ArrayList()
    private var listDescriptionVatTu: ArrayList<Description> = ArrayList()
    private var listDescriptionGiaiPhap: ArrayList<Description> = ArrayList()
    private var typeCamera = 0
    private var typeImages = 0
    private val listChungNhan = ArrayList<String>()

    companion object {
        const val TAG = "ProductManagerFragment"
        const val STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        const val STATUS_DISPLAY_HIDDEN: Int = 1 //Không hiển thị

        const val STATUS_FEAUTURED: Int = 1 //Sp nổi bật
        const val STATUS_NOT_FEAUTURED: Int = 0 //Sp bình thường
        var CASE_PICK_IMAGE: Boolean = true // true = Ảnh sản phẩm, false = Nhiều ảnh
        var CASE_TAKE_PHOTO: Boolean = true // true = Chụp ảnh từ camera, false = Chọn ảnh từ bộ sưu tập

        const val NKSX_DISPLAY_SHOW: Int = 1 //Hiển thị NKSX
        const val NKSX_DISPLAY_HIDDEN: Int = 0 //Không hiển thị NKSX

        const val ACCREDITATINON_DISPLAY_SHOW: Int = 1 //Hiển thị bao tiêu
        const val ACCREDITATINON_DISPLAY_HIDDEN: Int = 0 //Không hiển thị bao tiêu

        const val CATEGORY_LEVEL_PARENT: Int = 0
        const val CATEGORY_LEVEL_1: Int = 1
        const val CATEGORY_LEVEL_2: Int = 2
        const val CATEGORY_LEVEL_3: Int = 3
        const val CATEGORY_LEVEL_4: Int = 4

        const val TYPE_SP_LIENQUAN: Int = 0
        const val TYPE_SP_VATTU: Int = 1
        const val TYPE_SP_GIAIPHAP: Int = 2

        const val TYPE_DONVI_PHANPHOI: Int = 0
        const val TYPE_DONVI_SANXUAT: Int = 1
        const val TYPE_DONVI_NHAPKHAU: Int = 2
        const val TYPE_COSO_CHEBIEN: Int = 3

        const val TYPE_DONVI_CAP_1: Int = 0
        const val TYPE_DONVI_CAP_2: Int = 1
        const val TYPE_DONVI_CAP_3: Int = 2
        const val TYPE_DONVI_CAP_4: Int = 3
        const val TYPE_DONVI_CAP_5: Int = 4

        const val ADD_DESCRIPTION_COSOCB: Int = 0
        const val ADD_DESCRIPTION_VATTU: Int = 1
        const val ADD_DESCRIPTION_GIAIPHAP: Int = 2

        const val TYPE_CAMERA_IMAGES = 0
        const val TYPE_CAMERA_CERT = 1

        const val TYPE_SELECTED_IMAGES = 0
        const val TYPE_SELECTED_CERT = 1

        const val PERMISSIONS_REQUEST_CAMERA = 100

        fun newInstance(params: Bundle): ProductManagerAddFragment {
            val fragment = ProductManagerAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_manager_add, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_product_thuongHieu.setOnClickListener {
            searchProductViewModel.openSearchBrands()
//            getBrands(edit_product_thuongHieu)
        }
        edit_product_gianHang.setOnClickListener { getBooth(edit_product_gianHang, TYPE_DONVI_PHANPHOI) }
        edit_product_donViSX.setOnClickListener { getBooth(edit_product_donViSX, TYPE_DONVI_SANXUAT) }
        edit_product_donViNK.setOnClickListener { getBooth(edit_product_donViNK, TYPE_DONVI_NHAPKHAU) }
        edit_product_CosoCB.setOnClickListener { getBooth(edit_product_CosoCB, TYPE_COSO_CHEBIEN) }
        edit_product_chungNhan.setOnClickListener { getChungNhan(edit_product_chungNhan) }
        edit_product_donVi.setOnClickListener { getBooth(edit_product_donVi, TYPE_DONVI_CAP_1) }
        edit_product_donVi2.setOnClickListener { getBooth(edit_product_donVi2, TYPE_DONVI_CAP_2) }
        edit_product_donVi3.setOnClickListener { getBooth(edit_product_donVi3, TYPE_DONVI_CAP_3) }
        edit_product_donVi4.setOnClickListener { getBooth(edit_product_donVi4, TYPE_DONVI_CAP_4) }
        edit_product_donVi5.setOnClickListener { getBooth(edit_product_donVi5, TYPE_DONVI_CAP_5) }
        img_add_solution_product.setOnClickListener { searchProductViewModel.openSearchSp(TYPE_SP_GIAIPHAP) }
        img_add_supplies_product.setOnClickListener { searchProductViewModel.openSearchSp(TYPE_SP_VATTU) }

        btn_add_donVi2.setOnClickListener {
            btn_add_donVi2.visibility = View.GONE
            btn_add_donVi3.visibility = View.VISIBLE
            linear_dovi2.visibility = View.VISIBLE
        }

        btn_add_donVi3.setOnClickListener {
            btn_add_donVi3.visibility = View.GONE
            btn_add_donVi4.visibility = View.VISIBLE
            linear_dovi3.visibility = View.VISIBLE
        }

        btn_add_donVi4.setOnClickListener {
            btn_add_donVi4.visibility = View.GONE
            btn_add_donVi5.visibility = View.VISIBLE
            linear_dovi4.visibility = View.VISIBLE
        }

        btn_add_donVi5.setOnClickListener {
            btn_add_donVi5.visibility = View.GONE
            linear_dovi5.visibility = View.VISIBLE
        }

        if (UserDataManager.currentType == "Chủ hội chợ")
            linear_donVi.visibility = View.VISIBLE else {
            gianHangId = UserDataManager.currentUserId
        }

        view_image_add_product.setOnClickListener {
            openDialogChoosePicture()
        }

        view_camera.setOnClickListener {
            CASE_TAKE_PHOTO = false
            typeCamera = TYPE_CAMERA_IMAGES
            takePhoto()
        }

        img_add_related_product.setOnClickListener { searchProductViewModel.openSearchSp(TYPE_SP_LIENQUAN) }

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
            showDialogSelectCert()
        }

        sw_hienThi.setOnCheckedChangeListener { _, _ ->
            if (sw_hienThi.isChecked) {
                trangThaiHT = STATUS_DISPLAY_SHOW
                sw_hienThi.text = "Tuỳ chọn hiển thị: Hiển thị dạng chuẩn"
            } else {
                trangThaiHT = STATUS_DISPLAY_HIDDEN
                sw_hienThi.text = "Tuỳ chọn hiển thị: Không hiển thị"
            }
        }

        sw_spNoiBat.visibility = if (UserDataManager.currentType == "Chủ hội chợ") View.VISIBLE else View.GONE

        sw_spNoiBat.setOnCheckedChangeListener { _, _ ->
            if (sw_spNoiBat.isChecked) {
                spNoiBat = STATUS_FEAUTURED
                sw_spNoiBat.text = "Sản phẩm nổi bật: Nổi bật"
            } else {
                spNoiBat = STATUS_NOT_FEAUTURED
                sw_spNoiBat.text = "Sản phẩm nổi bật: Không nổi bật"
            }
        }

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
            val ghiChuVC = edit_product_ghiChu_vanChuyen.text.toString()

            val moTa = edit_product_moTa.text.toString()
            val tags = edit_product_tuKhoa.text.toString()

            val tenVatTu = tv_nguyenLieu_vatTu.text.toString()
            val tenGiaiPhap = tv_giaiPhap.text.toString()
            val tenLienQuan = tv_lienQuan.text.toString()

            if (UserDataManager.currentType == "Chủ hội chợ") {
                if (isRequiredFieldsValid(image, tenSp, edit_product_giaBan.text.toString(), maSp,
                                edt_product_danhMuc.text.toString(), edit_product_donVi.text.toString(), edit_product_thuongHieu.text.toString())) {

                    showProgressDialog()

                    viewModel.createProductManager(image, tenSp, maSp, dvt, xuatSu, ngayDongGoi, quyCachDongGoi, hsd, giaBan, giaBanKm, giaBanSiTu, giaBanSiDen, soLuongBanSi, maSoLoSX,
                            ngaySX, ngayThuHoachDK, quyMo, khaNangCungUng, muaVu, msLoHang, cangXuat, cangNhap, ngayXuatHang, ngayNhapHang, soLuongNhap,
                            hinhThucVC, ngayVC, donViVC, moTa, thuongHieuId, gianHangId, nkxs, baoTieu, trangThaiHT, spNoiBat, postMedias,
                            listCategory, listVatTu, listGiaiPhap, listProductRelated, tenVatTu, tenGiaiPhap, tenLienQuan, postMediasCert, donViSXId, donViNKId, coSoCBId, listDescriptionCSCB,
                            listDescriptionVatTu, listDescriptionGiaiPhap, ghiChuVC, listChungNhan, listRelatedShop, tags)
                }
            } else
                if (isRequiredFieldsValid(image, tenSp, edit_product_giaBan.text.toString(), maSp,
                                edt_product_danhMuc.text.toString(), gianHangId.toString(), edit_product_thuongHieu.text.toString())) {

                    showProgressDialog()

                    viewModel.createProductManager(image, tenSp, maSp, dvt, xuatSu, ngayDongGoi, quyCachDongGoi, hsd, giaBan, giaBanKm, giaBanSiTu, giaBanSiDen, soLuongBanSi, maSoLoSX,
                            ngaySX, ngayThuHoachDK, quyMo, khaNangCungUng, muaVu, msLoHang, cangXuat, cangNhap, ngayXuatHang, ngayNhapHang, soLuongNhap,
                            hinhThucVC, ngayVC, donViVC, moTa, thuongHieuId, gianHangId, nkxs, baoTieu, trangThaiHT, spNoiBat, postMedias,
                            listCategory, listVatTu, listGiaiPhap, listProductRelated, tenVatTu, tenGiaiPhap, tenLienQuan, postMediasCert, donViSXId, donViNKId, coSoCBId, listDescriptionCSCB,
                            listDescriptionVatTu, listDescriptionGiaiPhap, ghiChuVC, listChungNhan, listRelatedShop, tags)
                }
        }

        btn_add_general_coSo_cB.setOnClickListener { toast("Đang phát triển") }
        btn_vatTu.setOnClickListener { toast("Đang phát triển") }
        btn_giaiPhap.setOnClickListener { toast("Đang phát triển") }

        btn_add_description_coSo_cB.setOnClickListener { dialogAddDescription(ADD_DESCRIPTION_COSOCB) }
        btn_add_description_vatTu.setOnClickListener { dialogAddDescription(ADD_DESCRIPTION_VATTU) }
        btn_add_description_giaiPhap.setOnClickListener { dialogAddDescription(ADD_DESCRIPTION_GIAIPHAP) }

        edt_product_danhMuc.setOnClickListener { getCategory(edt_product_danhMuc, CATEGORY_LEVEL_PARENT) }
        edt_product_danhMuc_cap1.setOnClickListener { getCategory(edt_product_danhMuc_cap1, CATEGORY_LEVEL_1) }
        edt_product_danhMuc_cap2.setOnClickListener { getCategory(edt_product_danhMuc_cap2, CATEGORY_LEVEL_2) }
        edt_product_danhMuc_cap3.setOnClickListener { getCategory(edt_product_danhMuc_cap3, CATEGORY_LEVEL_3) }
        edt_product_danhMuc_cap4.setOnClickListener { getCategory(edt_product_danhMuc_cap4, CATEGORY_LEVEL_4) }

        setupImageRecycleview()
        setupImageCertRecycleview()
        setupRecyclerviewDescriptionCSCB()
        setupRecyclerviewDescriptionVatTu()
        setupRecyclerviewDescriptionGiaiPhap()
        loadSanPhamLienQuan()
        loadVatTu()
        loadGiaiPhap()
    }

    private fun showDialogSelectCert() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_select_image_cert, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tv_choose_album = dialog.findViewById(R.id.tv_choose_album) as VectorSupportTextView
            tv_choose_album.setOnClickListener {
                CASE_PICK_IMAGE = false
                typeImages = TYPE_SELECTED_CERT
                launchPickPhotoIntent()
                dialog.dismiss()
            }
            val tv_choose_config = dialog.findViewById(R.id.tv_choose_config) as VectorSupportTextView
            tv_choose_config.setOnClickListener {
                searchProductViewModel.openSearchCertImages(gianHangId)
                dialog.dismiss()
            }
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

    @SuppressLint("SetTextI18n")
    private fun openDialogChoosePicture() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_select_image, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()

            val tv_choose_takePhoto = dialog.findViewById(R.id.tv_choose_takePhoto) as VectorSupportTextView
            val tv_choose_album = dialog.findViewById(R.id.tv_choose_album) as VectorSupportTextView
            val tv_choose_viewImages = dialog.findViewById(R.id.tv_choose_viewImages) as VectorSupportTextView
            tv_choose_viewImages.visibility = View.GONE

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

    private fun dialogAddDescription(type: Int) {
        context?.let {
            var title = ""
            if (type == ADD_DESCRIPTION_COSOCB)
                title = "Thêm mô tả cơ sở chế biến"
            if (type == ADD_DESCRIPTION_VATTU)
                title = "Thêm mô tả nguyên liệu, vật tư được sử dụng"
            if (type == ADD_DESCRIPTION_GIAIPHAP)
                title = "Thêm mô tả giải pháp được sử dụng"

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

    private fun setupFlowChungChi() {
        container_receivers.adapter = object : TagAdapter<String>(listChungNhan) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val view = layoutInflater.inflate(R.layout.add_tag_flow, container_receivers, false) as TextView
                view.text = t ?: ""

                val icCheckbox = AppCompatResources.getDrawable(view.context, R.drawable.ic_close_white_16dp)
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, icCheckbox, null)
                return view
            }
        }

        container_receivers.setOnTagClickListener { view, position, parent ->
            run {
                if (position < listChungNhan.size)
                    listChungNhan.removeAt(position)
                else
                    listChungNhan.removeAt(listChungNhan.size - 1)

                parent.removeView(view)
                if (listChungNhan.size == 0) {
                    container_receivers.visibility = View.GONE
                }
            }
            true
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
        viewModel = obtainViewModel(ProductManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.createProductSusscess.observe(this, Observer {
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

        viewModel.dataBooth.observe(this, Observer { p ->
            p?.let {
                if (reloadProvider) {
                    adapterBooth.replaceAll(it)
                    adapterCoSoCheBien.replaceAll(it)
                    adapterDonViSanXuat.replaceAll(it)
                    adapterDonViNhapKhau.replaceAll(it)
                    adapterDonViCungUng.replaceAll(it)
                } else {
                    adapterBooth.addAll(it)
                    adapterCoSoCheBien.addAll(it)
                    adapterDonViSanXuat.addAll(it)
                    adapterDonViNhapKhau.addAll(it)
                    adapterDonViCungUng.addAll(it)
                }
            }
        })

        viewModel.dataBoothRelated.observe(this, Observer { p ->
            p?.let {
                adapterDonViCungUng_2.replaceAll(it)
            }
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

        viewModel.dataCertImages.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()

            }
        })

        viewModel.dataShopInfo.observe(this, Observer { p ->
            p?.let {
                linear_donVi.visibility = View.VISIBLE
                edit_product_donVi.isFocusable = false
                edit_product_donVi.isFocusableInTouchMode = false
                edit_product_donVi.setOnClickListener(null)
                edit_product_donVi.setText(it.name ?: "")
            }
        })



        searchBrandsViewModel = obtainViewModel(BrandSearchViewModel::class.java, true)
        searchBrandsViewModel.getDataSearchBrands.observe(this, Observer { p ->
            p?.let {
                thuongHieuId = it.id
                edit_product_thuongHieu.setText(it.name ?: "")
            }
        })

        searchProductViewModel = obtainViewModel(SearchProductManagerViewModel::class.java, true)
        searchProductViewModel.getSpLienQuan.observe(this, Observer { p ->
            p?.let {
                val data = it
                if (listProductRelated.size == 0) {
                    listProductRelated.add(it)
                    adapterProductRelatedImage.replaceAll(listProductRelated)
                } else {
                    val isContained = listProductRelated.any {
                        return@any it.id == data.id
                    }

                    if (isContained) {
                        toast("Sản phẩm này đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                        return@let
                    } else {
                        listProductRelated.add(data)
                        adapterProductRelatedImage.replaceAll(listProductRelated)
                    }
                }
            }
        })

        searchProductViewModel.getSpVatTu.observe(this, Observer { p ->
            p?.let {
                val data = it
                if (listVatTu.size == 0) {
                    listVatTu.add(data)
                    adapterVatTu.replaceAll(listVatTu)
                } else {
                    val isContained = listVatTu.any {
                        return@any it.id == data.id
                    }

                    if (isContained) {
                        toast("Sản phẩm này đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                        return@let
                    } else {
                        listVatTu.add(data)
                        adapterVatTu.replaceAll(listVatTu)
                    }
                }
            }
        })

        searchProductViewModel.getSpGiaiPhap.observe(this, Observer { p ->
            p?.let {
                val data = it
                if (listGiaiPhap.size == 0) {
                    listGiaiPhap.add(data)
                    adapterGiaiPhap.replaceAll(listGiaiPhap)
                } else {
                    val isContained = listGiaiPhap.any {
                        return@any it.id == data.id
                    }

                    if (isContained) {
                        toast("Sản phẩm này đã tồn tại, vui lòng chọn sản phẩm khác khác.")
                        return@let
                    } else {
                        listGiaiPhap.add(data)
                        adapterGiaiPhap.replaceAll(listGiaiPhap)
                    }
                }
            }
        })

        searchProductViewModel.getCertImages.observe(this, Observer { p ->
            p?.let {
                val postMedia = PostMedia()
                postMedia.uri = Uri.parse(it.image ?: "")
                postMedia.id = it.id
                postMediasCert.add(postMedia)
                adapterImagesCert.replaceAll(postMediasCert)
                rv_product_cert.visibility = View.VISIBLE
            }
        })

        reloadBrands = true
        reloadProvider = true

        if (UserDataManager.currentType == "Chủ gian hàng")
            viewModel.getShopInfo(UserDataManager.currentUserId)

//        firstLoadBrand()
        firstLoadCategory()
        firstLoadProvider()
    }

    private fun firstLoadBrand() {
        reloadBrands = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBrand(firstLoad)
    }

    private fun loadMoreBrands(currentCount: Int) {
        reloadBrands = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBrand(loadMore)
    }

    private fun firstLoadProvider() {
        reloadProvider = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBooth(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadProvider = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBooth(loadMore)
    }

    private fun firstLoadCategory() {
        viewModel.loadCategories()
    }

    private fun firstLoadCategoryChild(category: Category, level: Int) {
        viewModel.loadChildCategory(category, level)
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (!CASE_PICK_IMAGE) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private fun getChungNhan(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn chứng nhận")
                    .customView(R.layout.dialog_product_cert, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val tv_Oganic = dialog.findViewById(R.id.tv_Oganic) as TextView
            val tv_Vietgap = dialog.findViewById(R.id.tv_Vietgap) as TextView
            val tv_Globalgap = dialog.findViewById(R.id.tv_Globalgap) as TextView
            val tv_Eurepgap = dialog.findViewById(R.id.tv_Eurepgap) as TextView

            tv_Oganic.setOnClickListener {
                if (listChungNhan.isEmpty()) {
                    listChungNhan.add(tv_Oganic.text.toString())
                    setupFlowChungChi()
                    dialog.dismiss()
                } else {
                    var forEnd = false
                    for (i in listChungNhan.indices)
                        if (listChungNhan[i] == tv_Oganic.text.toString()) {
                            toast("Chứng nhận này đã được chọn")
                            break
                        } else if (i == listChungNhan.size - 1) forEnd = true

                    if (forEnd) {
                        listChungNhan.add(tv_Oganic.text.toString())
                        setupFlowChungChi()
                        dialog.dismiss()
                    }
                }
            }
            tv_Vietgap.setOnClickListener {
                if (listChungNhan.isEmpty()) {
                    listChungNhan.add(tv_Vietgap.text.toString())
                    setupFlowChungChi()
                    dialog.dismiss()
                } else {
                    var forEnd = false
                    for (i in listChungNhan.indices)
                        if (listChungNhan[i] == tv_Vietgap.text.toString()) {
                            toast("Chứng nhận này đã được chọn")
                            break
                        } else if (i == listChungNhan.size - 1) forEnd = true

                    if (forEnd) {
                        listChungNhan.add(tv_Vietgap.text.toString())
                        setupFlowChungChi()
                        dialog.dismiss()
                    }
                }
            }
            tv_Globalgap.setOnClickListener {
                if (listChungNhan.isEmpty()) {
                    listChungNhan.add(tv_Globalgap.text.toString())
                    setupFlowChungChi()
                    dialog.dismiss()
                } else {
                    var forEnd = false
                    for (i in listChungNhan.indices)
                        if (listChungNhan[i] == tv_Globalgap.text.toString()) {
                            toast("Chứng nhận này đã được chọn")
                            break
                        } else if (i == listChungNhan.size - 1) forEnd = true

                    if (forEnd) {
                        listChungNhan.add(tv_Globalgap.text.toString())
                        setupFlowChungChi()
                        dialog.dismiss()
                    }
                }
            }
            tv_Eurepgap.setOnClickListener {
                if (listChungNhan.isEmpty()) {
                    listChungNhan.add(tv_Eurepgap.text.toString())
                    setupFlowChungChi()
                    dialog.dismiss()
                } else {
                    var forEnd = false
                    for (i in listChungNhan.indices)
                        if (listChungNhan[i] == tv_Eurepgap.text.toString()) {
                            toast("Chứng nhận này đã được chọn")
                            break
                        } else if (i == listChungNhan.size - 1) forEnd = true

                    if (forEnd) {
                        listChungNhan.add(tv_Eurepgap.text.toString())
                        setupFlowChungChi()
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getBrands(view: TextView) {
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

    private fun getBooth(view: TextView, type: Int) {
        context?.let {
            var title = ""
//            if (type == TYPE_DONVI_PHANPHOI)
//                title = "Chọn đơn vị phân phối"
//            if (type == TYPE_DONVI_SANXUAT)
//                title = "Chọn đơn vị sản xuất"
//            if (type == TYPE_DONVI_NHAPKHAU)
//                title = "Chọn đơn vị nhập khẩu"
//            if (type == TYPE_COSO_CHEBIEN)
//                title = "Chọn cơ sở chế biến"
            if (type == TYPE_DONVI_CAP_1 || type == TYPE_DONVI_CAP_2 || type == TYPE_DONVI_CAP_3 || type == TYPE_DONVI_CAP_4 || type == TYPE_DONVI_CAP_5)
                title = "Chọn đơn vị trong chuỗi cung ứng"

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

//            if (type == TYPE_DONVI_PHANPHOI)
//                rv_search.adapter = adapterBooth
//            if (type == TYPE_DONVI_SANXUAT)
//                rv_search.adapter = adapterDonViSanXuat
//            if (type == TYPE_DONVI_NHAPKHAU)
//                rv_search.adapter = adapterDonViNhapKhau
//            if (type == TYPE_COSO_CHEBIEN)
//                rv_search.adapter = adapterCoSoCheBien
            if (type == TYPE_DONVI_CAP_1)
                rv_search.adapter = adapterDonViCungUng
            if (type == TYPE_DONVI_CAP_2 || type == TYPE_DONVI_CAP_3 || type == TYPE_DONVI_CAP_4 || type == TYPE_DONVI_CAP_5)
                rv_search.adapter = adapterDonViCungUng_2

            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
//            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
//                override fun click(position: Int, data: BoothManager, code: Int) {
//                    context?.let {
//                        dialog.dismiss()
//                        gianHangId = data.id
//                        view.text = data.boothName ?: ""
//                        view.error = null
//                    }
//                }
//            }
//
//            adapterDonViSanXuat.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
//                override fun click(position: Int, data: BoothManager, code: Int) {
//                    context?.let {
//                        dialog.dismiss()
//                        donViSXId = data.id
//                        view.text = data.boothName ?: ""
//                        view.error = null
//                    }
//                }
//            }
//
//            adapterDonViNhapKhau.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
//                override fun click(position: Int, data: BoothManager, code: Int) {
//                    context?.let {
//                        dialog.dismiss()
//                        donViNKId = data.id
//                        view.text = data.boothName ?: ""
//                        view.error = null
//                    }
//                }
//            }
//
//            adapterCoSoCheBien.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
//                override fun click(position: Int, data: BoothManager, code: Int) {
//                    context?.let {
//                        dialog.dismiss()
//                        coSoCBId = data.id
//                        view.text = data.boothName ?: ""
//                        view.error = null
//                    }
//                }
//            }

            adapterDonViCungUng.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        viewModel.getBoothRelated(data.id)
                        gianHangId = data.id
                        if (data.title?.isNotEmpty() == true)
                            edit_product_chucNangDV.setText(data.title)
                        else edit_product_chucNangDV.setText("")

                        listRelatedShop.clear()
                        edit_product_donVi2.setText("")
                        edit_product_donVi3.setText("")
                        edit_product_donVi4.setText("")
                        edit_product_donVi5.setText("")
                        edit_product_chucNangDV2.setText("")
                        edit_product_chucNangDV3.setText("")
                        edit_product_chucNangDV4.setText("")
                        edit_product_chucNangDV5.setText("")

                        view.text = data.boothName ?: ""
                        view.error = null
                        dialog.dismiss()
                    }
                }
            }

            adapterDonViCungUng_2.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        val title = ""
                        if (type == TYPE_DONVI_CAP_2) {
                            if (listRelatedShop.size >= 1) {
                                listRelatedShop.removeAt(0)
                                listRelatedShop.add(data)
                            } else listRelatedShop.add(data)

                            if (data.title?.isNotEmpty() == true)
                                edit_product_chucNangDV2.setText(data.title)
                            else edit_product_chucNangDV2.setText(title)
                        }
                        if (type == TYPE_DONVI_CAP_3) {
                            if (listRelatedShop.size >= 2) {
                                listRelatedShop.removeAt(1)
                                listRelatedShop.add(1, data)
                            } else listRelatedShop.add(data)

                            if (data.title?.isNotEmpty() == true)
                                edit_product_chucNangDV3.setText(data.title)
                            else edit_product_chucNangDV3.setText(title)
                        }
                        if (type == TYPE_DONVI_CAP_4) {
                            if (listRelatedShop.size >= 3) {
                                listRelatedShop.removeAt(2)
                                listRelatedShop.add(2, data)
                            } else listRelatedShop.add(data)

                            if (data.title?.isNotEmpty() == true)
                                edit_product_chucNangDV4.setText(data.title)
                            else edit_product_chucNangDV4.setText(title)
                        }
                        if (type == TYPE_DONVI_CAP_5) {
                            if (listRelatedShop.size >= 4) {
                                listRelatedShop.removeAt(3)
                                listRelatedShop.add(3, data)
                            } else listRelatedShop.add(data)

                            if (data.title?.isNotEmpty() == true)
                                edit_product_chucNangDV5.setText(data.title)
                            else edit_product_chucNangDV5.setText(title)
                        }

                        view.text = data.boothName ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun loadSanPhamLienQuan() {
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

    private fun loadVatTu() {
        context?.let {
            adapterVatTu.replaceAll(listVatTu)
            rv_supplies_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_supplies_products.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_supplies_products.adapter = adapterVatTu
            adapterVatTu.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    listVatTu.remove(data)
                    adapterVatTu.replaceAll(listVatTu)
                }
            }
        }
    }

    private fun loadGiaiPhap() {
        context?.let {
            adapterGiaiPhap.replaceAll(listGiaiPhap)
            rv_solution_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_solution_products.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_solution_products.adapter = adapterGiaiPhap
            adapterGiaiPhap.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    listGiaiPhap.remove(data)
                    adapterGiaiPhap.replaceAll(listGiaiPhap)
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

    private fun isRequiredFieldsValid(image: String, name: String, price: String, code: String, category: String, provider: String, brand: String): Boolean {
        if (image.trim().isEmpty()) {
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

        if (category.trim().isEmpty()) {
            toast("Danh mục không được để trống")
            edt_product_danhMuc.error = getString(R.string.error_field_required)
            edt_product_danhMuc.requestFocus()
            return false
        }

        if (provider.trim().isEmpty()) {
            toast("Nhà cung cấp không được để trống")
            edit_product_donVi.error = getString(R.string.error_field_required)
            edit_product_donVi.requestFocus()
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
}