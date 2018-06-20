package ishopgo.com.exhibition.ui.main.product.detail

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.NewConversation
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider
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

    var sameShopProducts = MutableLiveData<List<ProductProvider>>()

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

    var viewedProducts = MutableLiveData<List<ProductProvider>>()

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

    var favoriteProducts = MutableLiveData<List<ProductProvider>>()

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

    var detail = MutableLiveData<ProductDetailProvider>()

    fun loadProductDetail(productId: Long) {
        val request = if (UserDataManager.currentUserId > 0) authService.getProductDetail(productId) else noAuthService.getProductDetail(productId)
        addDisposable(request
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ProductDetail>() {
                    override fun success(data: ProductDetail?) {
                        data?.let {
                            detail.postValue(it)
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var productComments = MutableLiveData<List<ProductCommentProvider>>()

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
                .addFormDataPart("parent_id", parentId.toString())
                .addFormDataPart("rate", rate.toString())

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

}