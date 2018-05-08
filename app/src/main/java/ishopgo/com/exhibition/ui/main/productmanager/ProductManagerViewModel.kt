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
import ishopgo.com.exhibition.model.Brand
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.model.product_manager.ProductManager
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProductManagerViewModel : BaseListViewModel<List<ProductManagerProvider>>(), AppComponent.Injectable {
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

    fun createProductManager(name: String, code: String, title: String, tt_price: String, price: String, provider_price: String, dvt: String,
                             provider_id: String, brand_id: String, madeIn: String, image: String, postMedias: ArrayList<PostMedia>,
                             description: String, status: Int, meta_description: String, meta_keyword: String, tag:String,
                             listCategory: ArrayList<String>?, listProducts_bsp: ArrayList<ProductManagerProvider>, is_featured: Int) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("name", name)
        builder.addFormDataPart("code", code)
        builder.addFormDataPart("title", title)
        builder.addFormDataPart("tt_price", tt_price)
        builder.addFormDataPart("price", price)
        builder.addFormDataPart("provider_price", provider_price)
        builder.addFormDataPart("dvt", dvt)
        builder.addFormDataPart("provider_id", provider_id)
        builder.addFormDataPart("department_id", brand_id)
        builder.addFormDataPart("madeIn", madeIn)
        builder.addFormDataPart("description", description)
        builder.addFormDataPart("status", status.toString())
        builder.addFormDataPart("meta_description", meta_description)
        builder.addFormDataPart("meta_keyword", meta_keyword)
        builder.addFormDataPart("is_featured", is_featured.toString())
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
                builder.addFormDataPart("products_bsp_array[]", listProducts_bsp[i].provideId().toString())
                Log.d("products_bsp_array[]", listProducts_bsp[i].provideId().toString())
            }
        }

        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
                Log.d("listImage[]", uri.toString())
                val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                imageFile.deleteOnExit()
                Toolbox.reEncodeBitmap(appContext, uri, 2048, Uri.fromFile(imageFile))
                val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                builder.addFormDataPart("images[]", imageFile.name, imageBody)
                Log.d("listImage[]", imageBody.toString())
            }
        }

        var imagePart: MultipartBody.Part? = null

        if (image.trim().isNotEmpty()) {
            val imageFile = File(appContext.cacheDir, "product_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 2048, Uri.fromFile(imageFile))
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

    var dataBrands = MutableLiveData<MutableList<Brand>>()

    fun getBrand(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["limit"] = params.offset
            addDisposable(isgService.getBrands(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<MutableList<Brand>>() {
                        override fun success(data: MutableList<Brand>?) {
                            data?.let {
                                dataBrands.postValue(it)
                            }
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))
        }
    }

    var dataProvider = MutableLiveData<MutableList<Provider>>()

    fun getProvider(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["limit"] = params.offset
            addDisposable(isgService.getProviders(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<MutableList<Provider>>() {
                        override fun success(data: MutableList<Provider>?) {
                            data?.let {
                                dataProvider.postValue(it)
                            }
                        }


                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))
        }
    }
}