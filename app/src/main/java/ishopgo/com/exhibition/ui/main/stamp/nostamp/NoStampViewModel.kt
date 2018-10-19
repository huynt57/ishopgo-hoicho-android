package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.ProductManagerRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.ManagerBooth
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import okhttp3.MultipartBody

class NoStampViewModel : BaseListViewModel<List<StampNoListNew>>(), AppComponent.Injectable {

    var total = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getNoStampNew(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<StampNoListNew>>() {
                        override fun success(data: List<StampNoListNew>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var getDataNoStampDetail = MutableLiveData<StampNoDetail>()

    fun getDetailNoStamp(stampId: Long) {
        addDisposable(authService.getNoStampDetail(stampId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<StampNoDetail>() {
                    override fun success(data: StampNoDetail?) {
                        getDataNoStampDetail.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var getDataNoStampCreated = MutableLiveData<StampNoDetail>()

    fun getCreatedNoStamp() {
        addDisposable(authService.getNoStampCreated()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<StampNoDetail>() {
                    override fun success(data: StampNoDetail?) {
                        getDataNoStampCreated.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var editNoStampSusscess = MutableLiveData<Boolean>()

    fun editNoStampDetail(stampId: Long, name: String, quantity: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM).addFormDataPart("name", name).addFormDataPart("quantity", quantity)
        addDisposable(authService.editNoStampDetail(stampId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        editNoStampSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var addNoStampSusscess = MutableLiveData<Boolean>()

    fun addNoStampDetail(product_id: Long, stamp_assign_product_code: String, coatings: String, limited_access: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("product_id", product_id.toString())
                .addFormDataPart("stamp_assign_product_code", stamp_assign_product_code)
                .addFormDataPart("coatings", coatings)
                .addFormDataPart("limited_access", limited_access)

        addDisposable(authService.createNoStampNew(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        addNoStampSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataBrands = MutableLiveData<List<Brand>>()

    fun getBrand(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            addDisposable(noAuthService.getAllBrands(fields)
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

    var dataProductSearch = MutableLiveData<List<Product>>()
    var totalProduct = MutableLiveData<Int>()

    fun searchProductAssign(params: Request) {
        if (params is SearchProductRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["name"] = params.keyword

            addDisposable(isgService.getProductManager(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManageProduct>() {
                        override fun success(data: ManageProduct?) {
                            dataProductSearch.postValue(data?.product ?: mutableListOf())
                            totalProduct.postValue(data?.total ?: 0)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var saveStampAssignSucccess = MutableLiveData<Boolean>()

    fun saveStampAssign(stampId: Long, productId: Long, limited_access: String, limited_access_message: String, quantity_assign: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("product_id", productId.toString())
                .addFormDataPart("limited_access", limited_access)
                .addFormDataPart("limited_access_message", limited_access_message)
                .addFormDataPart("quantity_assign", quantity_assign)

        addDisposable(authService.saveStampAssign(stampId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        saveStampAssignSucccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var createTrackingSucccess = MutableLiveData<Boolean>()

    fun createTracking(stampId: Long, title: String, boothId: Long) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("value", boothId.toString())

        addDisposable(authService.addTracking(stampId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createTrackingSucccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var editTrackingSucccess = MutableLiveData<Boolean>()

    fun editTracking(stampId: Long, title: String, boothId: Long) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("value", boothId.toString())

        addDisposable(authService.editTracking(stampId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        editTrackingSucccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var downloadSuccess = MutableLiveData<Boolean>()

    fun downloadStamp(stampId: Long, quantity_assign: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("quantity_assign", quantity_assign)

        addDisposable(authService.downloadNoStamp(stampId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        downloadSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var generateStamp = MutableLiveData<String>()

    fun getGenerateStamp() {

        addDisposable(authService.generateStamp()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<String>() {
                    override fun success(data: String?) {
                        generateStamp.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var openSearchProduct = MutableLiveData<Long>()

    fun openSearchProduct(stampId: Long) {
        openSearchProduct.postValue(stampId)
    }

    var resultProduct = MutableLiveData<Product>()

    fun resultProduct(product: Product) {
        resultProduct.postValue(product)
    }

    var shopRelates = MutableLiveData<List<BoothManager>>()

    fun loadShopRelates(shopId: Long) {
        addDisposable(noAuthService.getShopRelate(shopId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<BoothManager>>() {
                    override fun success(data: List<BoothManager>?) {
                        shopRelates.postValue(data ?: listOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var deleteTrackingSuccess = MutableLiveData<Boolean>()

    fun deleteTracking(trackingId: Long) {
        addDisposable(authService.deleteTracking(trackingId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        deleteTrackingSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

}