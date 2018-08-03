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
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.ManagerBrand
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.ManagerBooth
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.model.product_manager.ProductManager
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
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
                             listAnh: ArrayList<PostMedia>, listDanhMuc: ArrayList<Category>, listVatTu: ArrayList<Product>,
                             listGiaiPhap: ArrayList<Product>, listSpLienQuan: ArrayList<Product>) {


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

        builder.addFormDataPart("is_nhatky_sx", isNKSX.toString())
        builder.addFormDataPart("is_baotieu", isBaoTieu.toString())
        builder.addFormDataPart("status", trangThaiHT.toString())
        builder.addFormDataPart("is_featured", spNoiBat.toString())

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

    fun editProductManager(productId: Long, name: String, code: String, title: String, price: Long, pricePromotion: Long, dvt: String,
                           boothId: Long, brand_id: Long, madeIn: String, image: String, postMedias: ArrayList<PostMedia>,
                           description: String, status: Int, meta_description: String, meta_keyword: String, tag: String,
                           listCategory: List<Category>, listProducts_bsp: ArrayList<Product>, is_featured: Int, wholesale_price_from: Long, wholesale_price_to: Long,
                           wholesale_count_product: String, listImageDelete: ArrayList<PostMedia>, scale: String, quantity: String,
                           pack: String, season: String, expiryDate: String, shipmentCode: String, manufacturingDate: String, harvestDate: String, shippedDate: String,
                           isNksx: Int, isAccreditation: Int, listSuppliesProduct: ArrayList<Product>, listSolutionProduct: ArrayList<Product>) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("name", name)
        builder.addFormDataPart("code", code)
        builder.addFormDataPart("title", title)
        builder.addFormDataPart("price", price.toString())
        builder.addFormDataPart("dvt", dvt)
        builder.addFormDataPart("provider_id", boothId.toString())
        builder.addFormDataPart("department_id", brand_id.toString())
        builder.addFormDataPart("madeIn", madeIn)
        builder.addFormDataPart("description", description)
        builder.addFormDataPart("status", status.toString())
        builder.addFormDataPart("meta_description", meta_description)
        builder.addFormDataPart("meta_keyword", meta_keyword)
        builder.addFormDataPart("is_featured", is_featured.toString())
        builder.addFormDataPart("wholesale_price_from", wholesale_price_from.toString())
        builder.addFormDataPart("wholesale_price_to", wholesale_price_to.toString())
        builder.addFormDataPart("wholesale_count_product", wholesale_count_product)
        builder.addFormDataPart("promotion_price", pricePromotion.toString())

        if (scale.isNotEmpty()) {
            builder.addFormDataPart("quy_mo", scale)
        }
        if (quantity.isNotEmpty()) {
            builder.addFormDataPart("san_luong", quantity)
        }

        //Agi

        if (pack.isNotEmpty()) {
            builder.addFormDataPart("dong_goi", pack)
        }

        if (expiryDate.isNotEmpty()) {
            builder.addFormDataPart("hsd", expiryDate)
        }

        if (season.isNotEmpty()) {
            builder.addFormDataPart("mua_vu", season)
        }

        if (shipmentCode.isNotEmpty()) {
            builder.addFormDataPart("ms_lohang", shipmentCode)
        }
        if (manufacturingDate.isNotEmpty()) {
            builder.addFormDataPart("ngay_sx", manufacturingDate)
        }
        if (harvestDate.isNotEmpty()) {
            builder.addFormDataPart("dk_thuhoach", harvestDate)
        }
        if (shippedDate.isNotEmpty()) {
            builder.addFormDataPart("xuat_xuong", shippedDate)
        }

        builder.addFormDataPart("is_nhatky_sx", isNksx.toString())
        builder.addFormDataPart("is_baotieu", isAccreditation.toString())


        val listTags: ArrayList<String>? = ArrayList()
        listTags?.add(tag)

        if (listTags != null) {
            for (i in listTags.indices) {
                builder.addFormDataPart("tags[]", listTags[i])
                Log.d("tag[]", listTags[i])
            }
        }

        if (!listProducts_bsp.isEmpty()) {
            for (i in listProducts_bsp.indices) {
                builder.addFormDataPart("products_bsp_array[]", listProducts_bsp[i].id.toString())
            }
        }

        if (listCategory.isNotEmpty()) {
            for (i in listCategory.indices) {
                builder.addFormDataPart("categories[]", listCategory[i].id.toString())
            }
        }

        if (listImageDelete.isNotEmpty()) {
            for (i in listImageDelete.indices) {
                builder.addFormDataPart("deleted_images[]", listImageDelete[i].uri.toString())
            }
        }

        if (!listSuppliesProduct.isEmpty()) {
            for (i in listSuppliesProduct.indices) {
                builder.addFormDataPart("vat_tu_products_bsp[]", listSuppliesProduct[i].id.toString())
            }
        }

        if (!listSolutionProduct.isEmpty()) {
            for (i in listSolutionProduct.indices) {
                builder.addFormDataPart("giai_phap_products_bsp[]", listSolutionProduct[i].id.toString())
            }
        }

        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
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

    var dataProductDetail = MutableLiveData<ProductManagerDetail>()

    fun getProductDetail(product_Id: Long) {

        addDisposable(authService.getProductManagerDetail(product_Id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ProductManagerDetail>() {
                    override fun success(data: ProductManagerDetail?) {
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

    companion object {
        const val CATEGORY_LEVEL_1: Int = 1
        const val CATEGORY_LEVEL_2: Int = 2
        const val CATEGORY_LEVEL_3: Int = 3
        const val CATEGORY_LEVEL_4: Int = 4
    }
}