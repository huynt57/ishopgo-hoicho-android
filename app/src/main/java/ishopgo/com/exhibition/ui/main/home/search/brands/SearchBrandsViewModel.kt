package ishopgo.com.exhibition.ui.main.home.search.brands

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchBrandsRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.ManagerBrand
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class SearchBrandsViewModel : BaseListViewModel<List<Brand>>(), AppComponent.Injectable {
    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is SearchBrandsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["id_app"] = UserDataManager.appId

            params.name?.let {
                fields["name"] = it
            }

            addDisposable(noAuthService.getAllBrands(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerBrand>() {
                        override fun success(data: ManagerBrand?) {
                            data?.let {
                                total.postValue(data.total)
                                dataReturned.postValue(it.brand ?: mutableListOf())
                            }
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}