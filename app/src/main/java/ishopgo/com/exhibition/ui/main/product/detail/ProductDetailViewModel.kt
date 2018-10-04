package ishopgo.com.exhibition.ui.main.product.detail

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.functions.Function5
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.*
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.showStackTrace
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var sameShopProducts = MutableLiveData<List<Product>>()

    fun loadSameShopProducts(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId
        addDisposable(noAuthService.getRelateProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        sameShopProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var viewedProducts = MutableLiveData<List<Product>>()

    fun loadViewedProducts(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId

        addDisposable(noAuthService.getViewedProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        viewedProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var favoriteProducts = MutableLiveData<List<Product>>()

    fun loadFavoriteProducts(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId

        addDisposable(authService.getFavoriteProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        favoriteProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    fun loadData(productId: Long, stampId: String, stampCode: String, stampType: String, deviceId: String) {

        val fieldsLoadDetail = mutableMapOf<String, Any>()
        if (stampId.isNotBlank()) {
            fieldsLoadDetail["stampId"] = stampId
        }
        if (stampCode.isNotBlank()) {
            fieldsLoadDetail["stampCode"] = stampCode
        }

        if (stampType.isNotBlank()) {
            fieldsLoadDetail["type"] = stampType
        }

        if (deviceId.isNotBlank()) {
            fieldsLoadDetail["device_id"] = deviceId
        }

        val d = if (UserDataManager.currentUserId > 0) authService.getProductDetail(productId, fieldsLoadDetail) else noAuthService.getProductDetail(productId, fieldsLoadDetail)


        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId
        val relateProducts = noAuthService.getRelateProducts(fields)

        val fields1 = mutableMapOf<String, Any>()
        fields1["limit"] = 5
        val productComment = noAuthService.getProductComments(productId, fields1)

        val isUserLoggedIn = UserDataManager.currentUserId > 0
        if (isUserLoggedIn) {
            val fields2 = mutableMapOf<String, Any>()
            fields2["limit"] = 10
            fields2["offset"] = 0
            fields2["product_id"] = productId

            val vp = noAuthService.getViewedProducts(fields2)

            val fields3 = mutableMapOf<String, Any>()
            fields3["limit"] = 10
            fields3["offset"] = 0
            fields3["product_id"] = productId

            val fp = authService.getFavoriteProducts(fields3)

            addDisposable(Single.zip(
                    d,
                    relateProducts,
                    productComment,
                    vp,
                    fp,
                    Function5<BaseResponse<ProductDetail>, BaseResponse<List<Product>>, BaseResponse<List<ProductComment>>, BaseResponse<List<Product>>, BaseResponse<List<Product>>, Unit> { t1: BaseResponse<ProductDetail>, t2: BaseResponse<List<Product>>, t3: BaseResponse<List<ProductComment>>, t4: BaseResponse<List<Product>>, t5: BaseResponse<List<Product>> ->
                        t1.data?.let {
                            detail.postValue(it)
                        }

                        sameShopProducts.postValue(t2.data ?: listOf())
                        productComments.postValue(t3.data ?: listOf())
                        viewedProducts.postValue(t4.data ?: listOf())
                        favoriteProducts.postValue(t5.data ?: listOf())
                    }

            )
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<Unit>() {
                        override fun onSuccess(t: Unit?) {
                            loadingStatus.postValue(false)
                        }

                        override fun onError(e: Throwable?) {
                            resolveError(BaseErrorSignal.ERROR_UNKNOWN, e?.showStackTrace() ?: "")
                        }

                    }))
        } else {
            addDisposable(Single.zip(d, relateProducts, productComment, Function3<BaseResponse<ProductDetail>, BaseResponse<List<Product>>, BaseResponse<List<ProductComment>>, Unit> { t1, t2, t3 ->
                t1.data?.let {
                    detail.postValue(it)
                }

                sameShopProducts.postValue(t2.data ?: listOf())
                productComments.postValue(t3.data ?: listOf())
            })
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<Unit>() {
                        override fun onSuccess(t: Unit?) {
                            loadingStatus.postValue(false)
                        }

                        override fun onError(e: Throwable?) {
                            resolveError(BaseErrorSignal.ERROR_UNKNOWN, e?.showStackTrace() ?: "")
                        }

                    }))
        }

    }

    var detail = MutableLiveData<ProductDetail>()

//    fun loadProductDetail(productId: Long) {
//        val request = if (UserDataManager.currentUserId > 0) authService.getProductDetail(productId) else noAuthService.getProductDetail(productId)
//        addDisposable(request
//                .subscribeOn(Schedulers.single())
//                .subscribeWith(object : BaseSingleObserver<ProductDetail>() {
//                    override fun success(data: ProductDetail?) {
//                        data?.let {
//                            detail.postValue(it)
//                        }
//                    }
//
//                    override fun failure(status: Int, message: String) {
//                        resolveError(status, message)
//                    }
//
//                })
//        )
//    }

    var productComments = MutableLiveData<List<ProductComment>>()

    fun loadProductComments(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 5
        addDisposable(noAuthService.getProductComments(productId, fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<ProductComment>>() {
                    override fun success(data: List<ProductComment>?) {
                        productComments.postValue(data ?: listOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var getProductLike = MutableLiveData<ProductLike>()

    fun getProductLike(productId: Long) {
        addDisposable(authService.getProductLike(productId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ProductLike>() {
                    override fun success(data: ProductLike?) {
                        getProductLike.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    fun postShareProduct(productId: Long) {
        addDisposable(authService.postProductShare(productId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {

                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var postLikeSuccess = MutableLiveData<Any>()

    fun postProductLike(productId: Long) {
        addDisposable(authService.postProductLike(productId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        postLikeSuccess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var productSalePoint = MutableLiveData<List<ProductSalePoint>>()

    fun getProductSalePoint(params: Request) {
        if (params is ProductSalePointRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getProductSalePoint(params.productId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ProductSalePoint>>() {
                        override fun success(data: List<ProductSalePoint>?) {
                            productSalePoint.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    var postCommentSuccess = MutableLiveData<Boolean>()

    fun postCommentProduct(productId: Long, content: String, parentId: Long, postMedias: ArrayList<PostMedia> = ArrayList(), rate: Float) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .addFormDataPart("rate", rate.toString())
        if (parentId != -1L) {
            builder.addFormDataPart("parent_id", parentId.toString())
        }
        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
                uri?.let {
                    val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                    imageFile.deleteOnExit()
                    Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                    val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                    builder.addFormDataPart("images[]", imageFile.name, imageBody)
                }
            }
        }
        addDisposable(authService.postCommentProduct(productId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        postCommentSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var createSalePointSuccess = MutableLiveData<Boolean>()

    fun createProductSalePoint(productId: Long, price: String, phone: String, name: String, city: String, district: String, address: String) {
        val fields = mutableMapOf<String, Any>()
        fields["product_id"] = productId
        fields["price"] = price
        fields["name"] = name
        fields["city"] = city
        fields["district"] = district
        fields["address"] = address
        fields["phone"] = phone

        addDisposable(authService.createProductSalePoint(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createSalePointSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var loadRegion = MutableLiveData<MutableList<Region>>()

    fun loadRegion() {
        addDisposable(isgService.getRegions()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<MutableList<Region>>() {
                    override fun success(data: MutableList<Region>?) {
                        loadRegion.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var getDataInforMember = MutableLiveData<SearchSalePoint>()

    fun getInfoMemberSalePoint(phone: String) {
        addDisposable(authService.getInfoMemberSalePoint(phone)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<SearchSalePoint>() {
                    override fun success(data: SearchSalePoint?) {
                        getDataInforMember.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                    }
                }))
    }


    var loadDistrict = MutableLiveData<MutableList<District>>()

    fun loadDistrict(province_id: String) {
        val fields = mutableMapOf<String, Any>()
        fields["province_id"] = province_id

        addDisposable(isgService.getDistricts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<MutableList<District>>() {
                    override fun success(data: MutableList<District>?) {
                        loadDistrict.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var conversation = MutableLiveData<NewConversation>()

    fun createConversation(params: Request) {
        if (params is CreateConversationRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["type"] = params.type
            params.member.mapIndexed { index, memId ->
                fields["member[$index]"] = memId
            }

            addDisposable(isgService.inbox_createNewChat(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<NewConversation>() {
                        override fun success(data: NewConversation?) {
                            conversation.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))

        }
    }

    var productDiary = MutableLiveData<MutableList<DiaryProduct>>()

    fun getProductDiary(params: Request) {
        if (params is ProductDiaryRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["product_id"] = params.productId
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(noAuthService.getProductDiary(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<MutableList<DiaryProduct>>() {
                        override fun success(data: MutableList<DiaryProduct>?) {
                            productDiary.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))

        }
    }


    var createProductDiary = MutableLiveData<Boolean>()

    fun createProductDiary(productId: Long, title: String, content: String, postMedias: ArrayList<PostMedia> = ArrayList(), stampCode: String, lat: String, lng:String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .addFormDataPart("title", title)
                .addFormDataPart("product_id", productId.toString())
                .addFormDataPart("lat", lat)
                .addFormDataPart("lng", lng)

        if (stampCode.isNotEmpty())
            builder.addFormDataPart("stampCode", stampCode)

        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
                uri?.let {
                    val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                    imageFile.deleteOnExit()
                    Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                    val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                    builder.addFormDataPart("images[]", imageFile.name, imageBody)
                }
            }
        }

        addDisposable(authService.createProductDiary(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createProductDiary.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var deleteProductDiary = MutableLiveData<Boolean>()

    fun deleteProductDiary(nksxId: Long, productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["product_id"] = productId

        addDisposable(authService.deleteProductDiary(nksxId, fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        deleteProductDiary.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var getListBGBN = MutableLiveData<List<ListBGBN>>()

    fun getListBGBN(params: Request) {
        if (params is ListBGBNRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["product_id"] = params.product_id
            fields["booth_id"] = params.booth_id
            fields["q"] = params.keyword
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getListBGBN(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ListBGBN>>() {
                        override fun success(data: List<ListBGBN>?) {
                            getListBGBN.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))
        }
    }

    var createExchangeDiarySuccess = MutableLiveData<Boolean>()

    fun createExchangeDiary(name: String, content: String, senderId: Long, senderType: String, receiverId: Long, receiverType: String, expiryDate: String, lat: String, lng: String,
                            productId: Long, quantity: String, quantity_prefix: String, stamp_assign_code: String, images: ArrayList<PostMedia>) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .addFormDataPart("name", name)
                .addFormDataPart("product_id", productId.toString())
                .addFormDataPart("sender_id", senderId.toString())
                .addFormDataPart("receiver_id", receiverId.toString())
                .addFormDataPart("sender_type", senderType)
                .addFormDataPart("receiver_type", receiverType)
                .addFormDataPart("expiry_date", expiryDate)
                .addFormDataPart("lat", lat)
                .addFormDataPart("lng", lng)
                .addFormDataPart("quantity", quantity)
                .addFormDataPart("quantity_prefix", quantity_prefix)
                .addFormDataPart("stamp_assign_code", stamp_assign_code)

        if (images.isNotEmpty()) {
            for (i in images.indices) {
                val uri = images[i].uri
                uri?.let {
                    val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                    imageFile.deleteOnExit()
                    Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
                    val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                    builder.addFormDataPart("images[]", imageFile.name, imageBody)
                }

            }
        }

        addDisposable(authService.createExchangeDiaryProduct(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createExchangeDiarySuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }
}