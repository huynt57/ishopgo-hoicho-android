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
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
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

    fun createProductManager(name: String, code: String, title: String, price: Long, dvt: String,
                             provider_id: Long, brand_id: Long, madeIn: String, image: String, postMedias: ArrayList<PostMedia>,
                             description: String, status: Int, meta_description: String, meta_keyword: String, tag: String,
                             listCategory: ArrayList<Category>, listProducts_bsp: ArrayList<ProductManagerProvider>, is_featured: Int, wholesale_price_from: Long, wholesale_price_to: Long, wholesale_count_product: String) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("name", name)
        builder.addFormDataPart("code", code)
        builder.addFormDataPart("title", title)
        builder.addFormDataPart("price", price.toString())
        builder.addFormDataPart("dvt", dvt)
        builder.addFormDataPart("provider_id", provider_id.toString())
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

        if (listCategory.isNotEmpty()) {
            for (i in listCategory.indices) {
                builder.addFormDataPart("categories[]", listCategory[i].id.toString())
                Log.d("categories[]", listCategory[i].id.toString())
            }
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

    var editProductSusscess = MutableLiveData<Boolean>()

    fun editProductManager(productId: Long, name: String, code: String, title: String, price: Long, dvt: String,
                           provider_id: Long, brand_id: Long, madeIn: String, image: String, postMedias: ArrayList<PostMedia>,
                           description: String, status: Int, meta_description: String, meta_keyword: String, tag: String,
                           listCategory: List<Category>, listProducts_bsp: ArrayList<ProductManagerProvider>, is_featured: Int, wholesale_price_from: Long, wholesale_price_to: Long, wholesale_count_product: String, listImageDelete: ArrayList<PostMedia>) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("name", name)
        builder.addFormDataPart("code", code)
        builder.addFormDataPart("title", title)
        builder.addFormDataPart("price", price.toString())
        builder.addFormDataPart("dvt", dvt)
        builder.addFormDataPart("provider_id", provider_id.toString())
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

    var dataProductDetail = MutableLiveData<ProductManagerDetail>()

    fun getProductDetail(product_Id: Long) {

        addDisposable(isgService.getProductManagerDetail(product_Id)
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


    var categories = MutableLiveData<List<CategoryProvider>>()

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

    var childCategories = MutableLiveData<List<CategoryProvider>>()
    var childCategories_1 = MutableLiveData<List<CategoryProvider>>()
    var childCategories_2 = MutableLiveData<List<CategoryProvider>>()
    var childCategories_3 = MutableLiveData<List<CategoryProvider>>()

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

    var childCategoriesDetail = MutableLiveData<List<CategoryProvider>>()
    var childCategoriesDetail_2 = MutableLiveData<List<CategoryProvider>>()
    var childCategoriesDetail_3 = MutableLiveData<List<CategoryProvider>>()
    var childCategoriesDetail_4 = MutableLiveData<List<CategoryProvider>>()

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