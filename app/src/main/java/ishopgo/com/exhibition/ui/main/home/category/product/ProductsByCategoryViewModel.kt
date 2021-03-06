package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.CategoriedProductsRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class ProductsByCategoryViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var childCategories = MutableLiveData<List<Category>>()

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

    var products = MutableLiveData<List<Product>>()

    fun loadProductsByCategory(request: Request) {
        if (request is CategoriedProductsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = request.limit
            fields["offset"] = request.offset
            fields["category_id"] = request.categoryId
            if (request.city.isNotEmpty())
                fields["city"] = request.city
            if (request.district.isNotEmpty())
                fields["district"] = request.district
            request.sort_by?.let { fields["sort_by"] = it }
            request.sort_type?.let { fields["sort_type"] = it }

            if (request.type_filter.isNotEmpty()) {
                val listType = request.type_filter
                for (i in listType.indices)
                    fields["type_filter[$i]"] = listType[i]

            }

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

}