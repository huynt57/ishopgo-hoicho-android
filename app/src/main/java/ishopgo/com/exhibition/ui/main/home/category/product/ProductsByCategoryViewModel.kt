package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.CategoriedProductsRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class ProductsByCategoryViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var childCategories = MutableLiveData<List<CategoryProvider>>()

    fun loadChildCategory(category: Category) {
        val fields = mutableMapOf<String, Any>()
        fields["category_id"] = category.id
        addDisposable(noAuthService.getSubCategories(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                    override fun success(data: List<Category>?) {
                        data?.map { it.parent = category }
                        childCategories.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var products = MutableLiveData<List<ProductProvider>>()

    fun loadProductsByCategory(request: Request) {
        if (request is CategoriedProductsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = request.limit
            fields["offset"] = request.offset
            fields["category_id"] = request.categoryId

            addDisposable(noAuthService.getCategoriedProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                        override fun success(data: List<Product>?) {
                            products.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }

                    })
            )
        }
    }


}