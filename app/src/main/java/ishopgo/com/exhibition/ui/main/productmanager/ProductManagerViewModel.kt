package ishopgo.com.exhibition.ui.main.productmanager

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.ProductManagerRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Description
import ishopgo.com.exhibition.model.ManagerBooth
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProductManagerViewModel : BaseListViewModel<List<Product>>(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    var totalProduct = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is ProductManagerRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["name"] = params.name
            fields["code"] = params.code
            fields["product_id"] = params.productId

            addDisposable(isgService.getProductManager(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManageProduct>() {
                        override fun success(data: ManageProduct?) {
                            dataReturned.postValue(data?.product ?: mutableListOf())
                            totalProduct.postValue(data?.total ?: 0)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var createProductSusscess = MutableLiveData<Boolean>()

    fun createProductManager(image: String, tenSp: String, maSp: String, dvt: String, xuatSu: String, ngayDongGoi: String,
                             quyCachDongGoi: String, hsd: String, giaBan: Long, giaBanKm: Long, giaBanSiTu: Long, giaBanSiDen: Long, soLuongBanSi: String,
                             maSoLoSX: String, ngaySX: String, ngayThuHoachDK: String, quyMo: String, khaNangCungUng: String, muaVu: String, msLoHang: String,
                             cangXuat: String, cangNhap: String, ngayXuatHang: String, ngayNhapHang: String, soLuongNhap: String, hinhThucVC: String, ngayVC: String,
                             donViVC: String, moTa: String, thuongHieuID: Long, gianHangId: Long, isNKSX: Int, isBaoTieu: Int, trangThaiHT: Int, spNoiBat: Int,
                             listAnh: ArrayList<PostMedia>, listDanhMuc: ArrayList<Category>, listVatTu: ArrayList<Product>, listGiaiPhap: ArrayList<Product>,
                             listSpLienQuan: ArrayList<Product>, tenVatTu: String, tenGiaiPhap: String, tenLienQuan: String, listCert: ArrayList<PostMedia>,
                             donViSXId: Long, donViNKId: Long, coSoCBId: Long, listDescriptionCSCB: ArrayList<Description>,
                             listDescriptionVatTu: ArrayList<Description>, listDescriptionGiaiPhap: ArrayList<Description>, ghiChu_vc: String,
                             chungNhan: ArrayList<String>, chuoiCungUng: ArrayList<BoothManager>, tags: String) {


        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        if (tenSp.isNotEmpty())
            builder.addFormDataPart("name", tenSp)
        if (maSp.isNotEmpty())
            builder.addFormDataPart("code", maSp)
        if (dvt.isNotEmpty())
            builder.addFormDataPart("dvt", dvt)
        if (xuatSu.isNotEmpty())
            builder.addFormDataPart("madeIn", xuatSu)
        if (ngayDongGoi.isNotEmpty())
            builder.addFormDataPart("ngay_donggoi", ngayDongGoi)

        if (gianHangId != -1L)
            builder.addFormDataPart("provider_id", gianHangId.toString())
        if (thuongHieuID != -1L)
            builder.addFormDataPart("department_id", thuongHieuID.toString())

        if (quyCachDongGoi.isNotEmpty())
            builder.addFormDataPart("dong_goi", quyCachDongGoi)
        if (hsd.isNotEmpty())
            builder.addFormDataPart("hsd", hsd)
        if (giaBan != 0L)
            builder.addFormDataPart("price", giaBan.toString())
        if (giaBanKm != 0L)
            builder.addFormDataPart("promotion_price", giaBanKm.toString())
        if (giaBanSiTu != 0L)
            builder.addFormDataPart("wholesale_price_from", giaBanSiTu.toString())
        if (giaBanSiDen != 0L)
            builder.addFormDataPart("wholesale_price_to", giaBanSiDen.toString())
        if (soLuongBanSi.isNotEmpty())
            builder.addFormDataPart("wholesale_count_product", soLuongBanSi)
        if (maSoLoSX.isNotEmpty())
            builder.addFormDataPart("ms_sanxuat", maSoLoSX)
        if (ngaySX.isNotEmpty())
            builder.addFormDataPart("ngay_sx", ngaySX)
        if (ngayThuHoachDK.isNotEmpty())
            builder.addFormDataPart("dk_thuhoach", ngayThuHoachDK)
        if (quyMo.isNotEmpty())
            builder.addFormDataPart("quy_mo", quyMo)
        if (khaNangCungUng.isNotEmpty())
            builder.addFormDataPart("san_luong", khaNangCungUng)
        if (muaVu.isNotEmpty())
            builder.addFormDataPart("mua_vu", muaVu)
        if (msLoHang.isNotEmpty())
            builder.addFormDataPart("ms_lohang", msLoHang)
        if (cangXuat.isNotEmpty())
            builder.addFormDataPart("cang_xuat", cangXuat)
        if (cangNhap.isNotEmpty())
            builder.addFormDataPart("cang_nhap", cangNhap)
        if (ngayXuatHang.isNotEmpty())
            builder.addFormDataPart("xuat_xuong_date", ngayXuatHang)
        if (ngayNhapHang.isNotEmpty())
            builder.addFormDataPart("nhap_hang_date", ngayNhapHang)
        if (soLuongNhap.isNotEmpty())
            builder.addFormDataPart("sl_nhap", soLuongNhap)
        if (hinhThucVC.isNotEmpty())
            builder.addFormDataPart("hinhthuc_vc", hinhThucVC)
        if (ngayVC.isNotEmpty())
            builder.addFormDataPart("ngay_vc", ngayVC)
        if (donViVC.isNotEmpty())
            builder.addFormDataPart("donvi_vc", donViVC)
        if (moTa.isNotEmpty())
            builder.addFormDataPart("description", moTa)
        if (donViSXId != 0L)
            builder.addFormDataPart("nsx_id", donViSXId.toString())
        if (donViNKId != 0L)
            builder.addFormDataPart("nk_id", donViNKId.toString())
        if (coSoCBId != 0L)
            builder.addFormDataPart("cs_chebien_id", coSoCBId.toString())
        if (ghiChu_vc.isNotEmpty())
            builder.addFormDataPart("vc_note", ghiChu_vc)

        builder.addFormDataPart("name_vattu", tenVatTu)
        builder.addFormDataPart("name_giaiphap", tenGiaiPhap)
        builder.addFormDataPart("name_lienquan", tenLienQuan)

        builder.addFormDataPart("is_nhatky_sx", isNKSX.toString())
        builder.addFormDataPart("is_baotieu", isBaoTieu.toString())
        builder.addFormDataPart("status", trangThaiHT.toString())
        builder.addFormDataPart("is_featured", spNoiBat.toString())

        if (tags.isNotEmpty())
            builder.addFormDataPart("tags", tags)

        if (!chungNhan.isEmpty()) {
            var chungNhanString = ""
            for (i in chungNhan.indices) {
                chungNhanString = "$chungNhan, ${chungNhan[i]}"
            }
            builder.addFormDataPart("chung_nhan", chungNhanString)
        }

        if (!chuoiCungUng.isEmpty()) {
            for (i in chuoiCungUng.indices) {
                builder.addFormDataPart("relate_shops[]", chuoiCungUng[i].id.toString())
            }
        }

        if (!listDescriptionCSCB.isEmpty()) {
            for (i in listDescriptionCSCB.indices) {
                builder.addFormDataPart("list_content_title_array_cscb[]", listDescriptionCSCB[i].title.toString())
                builder.addFormDataPart("list_content_desc_array_cscb[]", listDescriptionCSCB[i].description.toString())
            }
        }

        if (!listDescriptionVatTu.isEmpty()) {
            for (i in listDescriptionVatTu.indices) {
                builder.addFormDataPart("list_content_title_array_vtdsd[]", listDescriptionVatTu[i].title.toString())
                builder.addFormDataPart("list_content_desc_array_vtdsd[]", listDescriptionVatTu[i].description.toString())
            }
        }

        if (!listDescriptionGiaiPhap.isEmpty()) {
            for (i in listDescriptionGiaiPhap.indices) {
                builder.addFormDataPart("list_content_title_array_gpdsd[]", listDescriptionGiaiPhap[i].title.toString())
                builder.addFormDataPart("list_content_desc_array_gpdsd[]", listDescriptionGiaiPhap[i].description.toString())
            }
        }

        if (!listSpLienQuan.isEmpty()) {
            for (i in listSpLienQuan.indices) {
                builder.addFormDataPart("products_bsp_array[]", listSpLienQuan[i].id.toString())
            }
        }

        if (!listVatTu.isEmpty()) {
            for (i in listVatTu.indices) {
                builder.addFormDataPart("vat_tu_products_bsp[]", listVatTu[i].id.toString())
            }
        }

        if (!listGiaiPhap.isEmpty()) {
            for (i in listGiaiPhap.indices) {
                builder.addFormDataPart("giai_phap_products_bsp[]", listGiaiPhap[i].id.toString())
            }
        }

        if (listDanhMuc.isNotEmpty()) {
            for (i in listDanhMuc.indices) {
                builder.addFormDataPart("categories[]", listDanhMuc[i].id.toString())
            }
        }

        if (listAnh.isNotEmpty()) {
            for (i in listAnh.indices) {
                val uri = listAnh[i].uri
                uri?.let {
                    val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                    imageFile.deleteOnExit()
                    Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                    val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                    builder.addFormDataPart("images[]", imageFile.name, imageBody)
                }

            }
        }

        if (listCert.isNotEmpty()) {
            for (i in listCert.indices) {
                val uri = listCert[i].uri
                uri?.let {
                    val imageFile = File(appContext.cacheDir, "postCert$i.jpg")
                    imageFile.deleteOnExit()
                    Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                    val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                    builder.addFormDataPart("cert_files[]", imageFile.name, imageBody)
                }

            }
        }

        var imagePart: MultipartBody.Part? = null

        if (image.trim().isNotEmpty()) {
            val imageFile = File(appContext.cacheDir, "product_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)
        }

        if (imagePart != null) {
            builder.addPart(imagePart)
        }

        addDisposable(isgService.createProductManager(builder.build())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createProductSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var updateVisibilityOk = MutableLiveData<Boolean>()

    fun updateVisibility(productId: Long, status: Int) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("status", status.toString())

        addDisposable(isgService.editProduct(productId, builder.build())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        updateVisibilityOk.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var editProductSusscess = MutableLiveData<Boolean>()

    fun editProductManager(productId: Long, image: String, tenSp: String, maSp: String, dvt: String, xuatSu: String, ngayDongGoi: String,
                           quyCachDongGoi: String, hsd: String, giaBan: Long, giaBanKm: Long, giaBanSiTu: Long, giaBanSiDen: Long, soLuongBanSi: String,
                           maSoLoSX: String, ngaySX: String, ngayThuHoachDK: String, quyMo: String, khaNangCungUng: String, muaVu: String, msLoHang: String,
                           cangXuat: String, cangNhap: String, ngayXuatHang: String, ngayNhapHang: String, soLuongNhap: String, hinhThucVC: String, ngayVC: String,
                           donViVC: String, moTa: String, thuongHieuID: Long, gianHangId: Long, isNKSX: Int, isBaoTieu: Int, trangThaiHT: Int, spNoiBat: Int,
                           listAnh: ArrayList<PostMedia>, listDanhMuc: MutableList<Category>, listVatTu: MutableList<Product>, listGiaiPhap: MutableList<Product>,
                           listSpLienQuan: MutableList<Product>, tenVatTu: String, tenGiaiPhap: String, tenLienQuan: String, listCert: ArrayList<PostMedia>,
                           donViSXId: Long, donViNKId: Long, coSoCBId: Long, listDescriptionCSCB: MutableList<Description>,
                           listDescriptionVatTu: MutableList<Description>, listDescriptionGiaiPhap: MutableList<Description>, ghiChu_vc: String, listImageDelete: ArrayList<PostMedia>) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (tenSp.isNotEmpty())
            builder.addFormDataPart("name", tenSp)
        if (maSp.isNotEmpty())
            builder.addFormDataPart("code", maSp)
        if (dvt.isNotEmpty())
            builder.addFormDataPart("dvt", dvt)
        if (xuatSu.isNotEmpty())
            builder.addFormDataPart("madeIn", xuatSu)
        if (ngayDongGoi.isNotEmpty())
            builder.addFormDataPart("ngay_donggoi", ngayDongGoi)

        if (gianHangId != -1L)
            builder.addFormDataPart("provider_id", gianHangId.toString())
        if (thuongHieuID != -1L)
            builder.addFormDataPart("department_id", thuongHieuID.toString())

        if (quyCachDongGoi.isNotEmpty())
            builder.addFormDataPart("dong_goi", quyCachDongGoi)
        if (hsd.isNotEmpty())
            builder.addFormDataPart("hsd", hsd)
        if (giaBan != 0L)
            builder.addFormDataPart("price", giaBan.toString())
        if (giaBanKm != 0L)
            builder.addFormDataPart("promotion_price", giaBanKm.toString())
        if (giaBanSiTu != 0L)
            builder.addFormDataPart("wholesale_price_from", giaBanSiTu.toString())
        if (giaBanSiDen != 0L)
            builder.addFormDataPart("wholesale_price_to", giaBanSiDen.toString())
        if (soLuongBanSi.isNotEmpty())
            builder.addFormDataPart("wholesale_count_product", soLuongBanSi)
        if (maSoLoSX.isNotEmpty())
            builder.addFormDataPart("ms_sanxuat", maSoLoSX)
        if (ngaySX.isNotEmpty())
            builder.addFormDataPart("ngay_sx", ngaySX)
        if (ngayThuHoachDK.isNotEmpty())
            builder.addFormDataPart("dk_thuhoach", ngayThuHoachDK)
        if (quyMo.isNotEmpty())
            builder.addFormDataPart("quy_mo", quyMo)
        if (khaNangCungUng.isNotEmpty())
            builder.addFormDataPart("san_luong", khaNangCungUng)
        if (muaVu.isNotEmpty())
            builder.addFormDataPart("mua_vu", muaVu)
        if (msLoHang.isNotEmpty())
            builder.addFormDataPart("ms_lohang", msLoHang)
        if (cangXuat.isNotEmpty())
            builder.addFormDataPart("cang_xuat", cangXuat)
        if (cangNhap.isNotEmpty())
            builder.addFormDataPart("cang_nhap", cangNhap)
        if (ngayXuatHang.isNotEmpty())
            builder.addFormDataPart("xuat_xuong_date", ngayXuatHang)
        if (ngayNhapHang.isNotEmpty())
            builder.addFormDataPart("nhap_hang_date", ngayNhapHang)
        if (soLuongNhap.isNotEmpty())
            builder.addFormDataPart("sl_nhap", soLuongNhap)
        if (hinhThucVC.isNotEmpty())
            builder.addFormDataPart("hinhthuc_vc", hinhThucVC)
        if (ngayVC.isNotEmpty())
            builder.addFormDataPart("ngay_vc", ngayVC)
        if (donViVC.isNotEmpty())
            builder.addFormDataPart("donvi_vc", donViVC)
        if (moTa.isNotEmpty())
            builder.addFormDataPart("description", moTa)
        if (donViSXId != 0L)
            builder.addFormDataPart("nsx_id", donViSXId.toString())
        if (donViNKId != 0L)
            builder.addFormDataPart("nk_id", donViNKId.toString())
        if (coSoCBId != 0L)
            builder.addFormDataPart("cs_chebien_id", coSoCBId.toString())
        if (ghiChu_vc.isNotEmpty())
            builder.addFormDataPart("vc_note", ghiChu_vc)

        builder.addFormDataPart("name_vattu", tenVatTu)
        builder.addFormDataPart("name_giaiphap", tenGiaiPhap)
        builder.addFormDataPart("name_lienquan", tenLienQuan)

        builder.addFormDataPart("is_nhatky_sx", isNKSX.toString())
        builder.addFormDataPart("is_baotieu", isBaoTieu.toString())
        builder.addFormDataPart("status", trangThaiHT.toString())
        builder.addFormDataPart("is_featured", spNoiBat.toString())

        if (!listDescriptionCSCB.isEmpty()) {
            for (i in listDescriptionCSCB.indices) {
                builder.addFormDataPart("list_content_title_array_cscb[]", listDescriptionCSCB[i].title.toString())
                builder.addFormDataPart("list_content_desc_array_cscb[]", listDescriptionCSCB[i].description.toString())
            }
        }

        if (!listDescriptionVatTu.isEmpty()) {
            for (i in listDescriptionVatTu.indices) {
                builder.addFormDataPart("list_content_title_array_vtdsd[]", listDescriptionVatTu[i].title.toString())
                builder.addFormDataPart("list_content_desc_array_vtdsd[]", listDescriptionVatTu[i].description.toString())
            }
        }

        if (!listDescriptionGiaiPhap.isEmpty()) {
            for (i in listDescriptionGiaiPhap.indices) {
                builder.addFormDataPart("list_content_title_array_gpdsd[]", listDescriptionGiaiPhap[i].title.toString())
                builder.addFormDataPart("list_content_desc_array_gpdsd[]", listDescriptionGiaiPhap[i].description.toString())
            }
        }

        if (!listSpLienQuan.isEmpty()) {
            for (i in listSpLienQuan.indices) {
                builder.addFormDataPart("products_bsp_array[]", listSpLienQuan[i].id.toString())
            }
        }

        if (!listVatTu.isEmpty()) {
            for (i in listVatTu.indices) {
                builder.addFormDataPart("vat_tu_products_bsp[]", listVatTu[i].id.toString())
            }
        }

        if (!listGiaiPhap.isEmpty()) {
            for (i in listGiaiPhap.indices) {
                builder.addFormDataPart("giai_phap_products_bsp[]", listGiaiPhap[i].id.toString())
            }
        }

        if (listDanhMuc.isNotEmpty()) {
            for (i in listDanhMuc.indices) {
                builder.addFormDataPart("categories[]", listDanhMuc[i].id.toString())
            }
        }

        if (listAnh.isNotEmpty()) {
            for (i in listAnh.indices) {
                val uri = listAnh[i].uri
                if (!uri.toString().subSequence(0, 4).contains("http"))
                    uri?.let {
                        val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                        imageFile.deleteOnExit()
                        Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                        val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                        builder.addFormDataPart("images[]", imageFile.name, imageBody)
                    }
            }
        }

        if (listCert.isNotEmpty()) {
            for (i in listCert.indices) {
                val uri = listCert[i].uri
                if (!uri.toString().subSequence(0, 4).contains("http"))
                    uri?.let {
                        val imageFile = File(appContext.cacheDir, "postCert$i.jpg")
                        imageFile.deleteOnExit()
                        Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                        val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                        builder.addFormDataPart("cert_files[]", imageFile.name, imageBody)
                    }
            }
        }

        var imagePart: MultipartBody.Part? = null

        if (image.trim().isNotEmpty()) {
            val imageFile = File(appContext.cacheDir, "product_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)
        }

        if (imagePart != null) {
            builder.addPart(imagePart)
        }

        if (listImageDelete.isNotEmpty()) {
            for (i in listImageDelete.indices) {
                builder.addFormDataPart("deleted_images[]", listImageDelete[i].uri.toString())
            }
        }

        addDisposable(isgService.editProduct(productId, builder.build())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        editProductSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var dataBrands = MutableLiveData<List<Brand>>()

    fun getBrand(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            addDisposable(isgService.getBrands(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerBrand>() {
                        override fun success(data: ManagerBrand?) {
                            data?.let {
                                dataBrands.postValue(it.brand ?: mutableListOf())
                            }
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))
        }
    }

    var dataBooth = MutableLiveData<List<BoothManager>>()

    fun getBooth(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            addDisposable(authService.getBooth(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerBooth>() {
                        override fun success(data: ManagerBooth?) {
                            dataBooth.postValue(data?.booths ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))
        }
    }

    var dataBoothRelated = MutableLiveData<List<BoothManager>>()

    fun getBoothRelated(boothId: Long) {
        addDisposable(authService.getBoothRelated(boothId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<BoothManager>>() {
                    override fun success(data: List<BoothManager>?) {
                        dataBoothRelated.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var dataCertImages = MutableLiveData<List<CertImages>>()

    fun getCertImagesConfig(providerId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["provider_id"] = providerId
        addDisposable(authService.getImagesForConfig(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<CertImages>>() {
                    override fun success(data: List<CertImages>?) {
                        dataCertImages.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var dataProductDetail = MutableLiveData<ProductDetail>()

    fun getProductDetail(product_Id: Long) {

        addDisposable(authService.getProductManagerDetail(product_Id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ProductDetail>() {
                    override fun success(data: ProductDetail?) {
                        data?.let {
                            dataProductDetail.postValue(it)
                        }
                    }


                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var pushTopSuccess = MutableLiveData<Boolean>()

    fun pushToTop(productId: Long) {
        val params = mutableMapOf<String, Any>()
        params["product_id"] = productId

        addDisposable(authService.pushTop(params)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        pushTopSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var categories = MutableLiveData<List<Category>>()

    fun loadCategories() {
        addDisposable(noAuthService.getCategories()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                    override fun success(data: List<Category>?) {
                        val filtered = data?.filter { it.id != 0L }
                        categories.postValue(filtered ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var childCategories = MutableLiveData<List<Category>>()
    var childCategories_1 = MutableLiveData<List<Category>>()
    var childCategories_2 = MutableLiveData<List<Category>>()
    var childCategories_3 = MutableLiveData<List<Category>>()

    fun loadChildCategory(category: Category, level: Int) {
        val fields = mutableMapOf<String, Any>()
        fields["category_id"] = category.id
        addDisposable(noAuthService.getSubCategories(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                    override fun success(data: List<Category>?) {
                        if (level == CATEGORY_LEVEL_1)
                            childCategories.postValue(data ?: mutableListOf())
                        if (level == CATEGORY_LEVEL_2)
                            childCategories_1.postValue(data ?: mutableListOf())
                        if (level == CATEGORY_LEVEL_3)
                            childCategories_2.postValue(data ?: mutableListOf())
                        if (level == CATEGORY_LEVEL_4)
                            childCategories_3.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var childCategoriesDetail = MutableLiveData<List<Category>>()
    var childCategoriesDetail_2 = MutableLiveData<List<Category>>()
    var childCategoriesDetail_3 = MutableLiveData<List<Category>>()
    var childCategoriesDetail_4 = MutableLiveData<List<Category>>()

    fun loadChildCategoryDetail(categoryId: Long, level: Int) {
        val fields = mutableMapOf<String, Any>()
        fields["category_id"] = categoryId
        Log.d("1231231", categoryId.toString())

        addDisposable(noAuthService.getSubCategories(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                    override fun success(data: List<Category>?) {
                        if (level == CATEGORY_LEVEL_1)
                            childCategoriesDetail.postValue(data ?: mutableListOf())
                        if (level == CATEGORY_LEVEL_2)
                            childCategoriesDetail_2.postValue(data ?: mutableListOf())
                        if (level == CATEGORY_LEVEL_3)
                            childCategoriesDetail_3.postValue(data ?: mutableListOf())
                        if (level == CATEGORY_LEVEL_4)
                            childCategoriesDetail_4.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var total = MutableLiveData<Int>()
    var filterProduct = MutableLiveData<List<Product>>()

    fun loadSearchProduct(params: Request) {
        if (params is SearchProductRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword
            if (params.categoryId != 0L) fields["category_id"] = params.categoryId
            if (params.brandId != 0L) fields["brand_id"] = params.brandId
            if (params.boothId != 0L) fields["booth_id"] = params.boothId

            addDisposable(noAuthService.searchProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerProduct>() {
                        override fun success(data: ManagerProduct?) {
                            total.postValue(data?.total ?: 0)
                            filterProduct.postValue(data?.product ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    companion object {
        const val CATEGORY_LEVEL_1: Int = 1
        const val CATEGORY_LEVEL_2: Int = 2
        const val CATEGORY_LEVEL_3: Int = 3
        const val CATEGORY_LEVEL_4: Int = 4
    }
}