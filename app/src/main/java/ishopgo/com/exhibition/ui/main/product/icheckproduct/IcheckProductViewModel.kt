package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.base.BaseIcheckAffiliateSingleObserver
import ishopgo.com.exhibition.ui.base.BaseIcheckSingleObserver
import okhttp3.MultipartBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.Gson


class IcheckProductViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var detail = MutableLiveData<IcheckProductDetail>()

    fun loadDetail(request: String) {
        addDisposable(isgService.getIcheckProductDetail(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckProductDetail>() {
                    override fun success(data: IcheckProductDetail?) {
                        data?.let { detail.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataSalePoint = MutableLiveData<IcheckSalePointManager>()

    fun loadSalePoint(request: String) {
        addDisposable(isgService.getIcheckSalePoint(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckSalePointManager>() {
                    override fun success(data: IcheckSalePointManager?) {
                        data?.let { dataSalePoint.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataSalePointDetail = MutableLiveData<IcheckSalePointDetail>()

    fun getSalePointDetail(request: String) {
        addDisposable(isgService.getIcheckDetailSalePoint(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckSalePointDetailManager>() {
                    override fun success(data: IcheckSalePointDetailManager?) {
                        data?.let { dataSalePointDetail.postValue(it.local) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataProductSalePoint = MutableLiveData<List<IcheckProduct>>()

    fun loadProductSalePointDetail(request: String) {
        addDisposable(isgService.getIcheckProductSalePoint(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckProductManager>() {
                    override fun success(data: IcheckProductManager?) {
                        data?.let { dataProductSalePoint.postValue(it.list) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataProductRelated = MutableLiveData<List<IcheckProduct>>()

    fun loadProductRelated(request: String) {
        addDisposable(isgService.getIcheckProductRelated(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckProduct>>() {
                    override fun success(data: List<IcheckProduct>?) {
                        data?.let { dataProductRelated.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataProductOnShop = MutableLiveData<List<IcheckProduct>>()

    fun loadProductOnShop(request: String) {
        addDisposable(isgService.getIcheckProductOnShop(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckProduct>>() {
                    override fun success(data: List<IcheckProduct>?) {
                        data?.let { dataProductOnShop.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataReview = MutableLiveData<List<IcheckReview>>()

    fun loadIcheckReview(request: String) {
        addDisposable(isgService.getIcheckReview(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckReview>>() {
                    override fun success(data: List<IcheckReview>?) {
                        data?.let { dataReview.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataShopInfo = MutableLiveData<IcheckShopDetail>()

    fun loadIcheckShopInfo(request: String) {
        addDisposable(isgService.getIcheckShopInfo(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckShopDetail>() {
                    override fun success(data: IcheckShopDetail?) {
                        data?.let { dataShopInfo.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataShopCategory = MutableLiveData<List<IcheckCategory>>()

    fun loadIcheckShopCategory(request: String) {

        addDisposable(isgService.loadIcheckShopCategory(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckCategoryManager>() {
                    override fun success(data: IcheckCategoryManager?) {
                        data?.let { dataShopCategory.postValue(it.category?.roots) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataShopCategoryProduct = MutableLiveData<List<IcheckProduct>>()

    fun loadIcheckShopCategoryProduct(request: String) {
        addDisposable(isgService.loadIcheckShopCategoryProduct(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckProduct>>() {
                    override fun success(data: List<IcheckProduct>?) {
                        data?.let { dataShopCategoryProduct.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataCity = MutableLiveData<IcheckCity>()

    fun loadIcheckShopCity(request: String) {
        addDisposable(isgService.loadIcheckShopCity(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckAffiliateSingleObserver<IcheckCity>() {
                    override fun success(data: IcheckCity?) {
                        dataCity.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataDistrict = MutableLiveData<IcheckDistrict>()

    fun loadIcheckShopDistrict(request: String) {
        addDisposable(isgService.loadIcheckShopDistrict(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckAffiliateSingleObserver<IcheckDistrict>() {
                    override fun success(data: IcheckDistrict?) {
                        data?.let { dataDistrict.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataCategory = MutableLiveData<List<IcheckCategory>>()

    fun loadIcheckCategory(request: String) {

        addDisposable(isgService.loadIcheckCategory(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckCategory>>() {
                    override fun success(data: List<IcheckCategory>?) {
                        data?.let { dataCategory.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataCategory_2 = MutableLiveData<List<IcheckCategory>>()

    fun loadIcheckCategory_2(request: String) {

        addDisposable(isgService.loadIcheckCategory(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckCategory>>() {
                    override fun success(data: List<IcheckCategory>?) {
                        data?.let { dataCategory_2.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataCategory_3 = MutableLiveData<List<IcheckCategory>>()

    fun loadIcheckCategory_3(request: String) {

        addDisposable(isgService.loadIcheckCategory(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckCategory>>() {
                    override fun success(data: List<IcheckCategory>?) {
                        data?.let { dataCategory_3.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataCategory_4 = MutableLiveData<List<IcheckCategory>>()

    fun loadIcheckCategory_4(request: String) {

        addDisposable(isgService.loadIcheckCategory(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckCategory>>() {
                    override fun success(data: List<IcheckCategory>?) {
                        data?.let { dataCategory_4.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var openIcheckCategory = MutableLiveData<Boolean>()

    fun openIcheckCategoryFragment() {
        openIcheckCategory.postValue(true)
    }

    var dataResultCategoryId = MutableLiveData<Long>()
    var dataResultCategoryName = MutableLiveData<String>()

    fun resultCateogory(categoryId: Long, categoryName: String) {
        dataResultCategoryId.postValue(categoryId)
        dataResultCategoryName.postValue(categoryName)
    }

    var updateIcheckSucccess = MutableLiveData<Boolean>()

    fun updateIcheckProduct(gtin_code: String, name: String, price: Long, image: String, attachments: List<String>, category_1: Long, category_2: Long, category_3: Long, category_4: Long,
                            description: String) {

        val paramObject = JsonObject()
        val icheckProduct = IcheckProductPost.ProductPost()
        icheckProduct.gtin_code = gtin_code
        icheckProduct.name = name
        icheckProduct.price = price
        icheckProduct.image = image
        icheckProduct.attachments = attachments
        if (category_1 != 0L)
            icheckProduct.category_1 = category_1
        if (category_2 != 0L)
            icheckProduct.category_2 = category_2
        if (category_3 != 0L)
            icheckProduct.category_3 = category_3
        if (category_4 != 0L)
            icheckProduct.category_4 = category_4

        icheckProduct.description = description

//        val vendor = IcheckProductPost.Vendor()
//        vendor.name = ""
//        vendor.address = ""
//        vendor.phone = ""
//        vendor.email = ""
//        vendor.country = 0L

        paramObject.add("product", Gson().toJsonTree(icheckProduct))
//        paramObject.add("vendor", Gson().toJsonTree(vendor))

        addDisposable(icheckService.updateIcheckProduct(paramObject)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        updateIcheckSucccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var createSalePointSucccess = MutableLiveData<Boolean>()

    fun createIcheckSalePoint(gtin_code: String, name: String, price: Long, phone: String, city: Long, district: Long, address: String, lat: Float, long: Float, referrerPhone: String, categoryId: Long) {
        val paramObject = JsonObject()
        paramObject.addProperty("gtin_code", gtin_code)
        paramObject.addProperty("phone", phone)
        paramObject.addProperty("name", name)
        paramObject.addProperty("city", city)
        paramObject.addProperty("district", district)
        paramObject.addProperty("address", address)
        if (lat != 0.0F)
            paramObject.addProperty("lat", lat)
        if (long != 0.0F)
            paramObject.addProperty("long", long)
        paramObject.addProperty("referrer_phone", referrerPhone)
        paramObject.addProperty("category_id", categoryId)
        paramObject.addProperty("price", price)

        addDisposable(icheckService.createSalePoint(paramObject)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createSalePointSucccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}