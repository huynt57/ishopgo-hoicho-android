package ishopgo.com.exhibition.ui.main.map

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchBoothRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.domain.response.Kiosk
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoDetailViewModel : BaseListViewModel<List<Kiosk>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ExpoShopLocationRequest) {
            addDisposable(noAuthService.getExpoShopLocations(params.expoId, params.toMap())
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Kiosk>>() {
                        override fun success(data: List<Kiosk>?) {
                            dataReturned.postValue(data ?: listOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }

                    })
            )
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var availableBooths = MutableLiveData<List<BoothManager>>()

    fun loadAvailableBooths(request: Request) {
        if (request is SearchBoothRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = request.limit
            fields["offset"] = request.offset
            request.keyword?.let {
                fields["booth_name"] = it
            }

            addDisposable(authService.getBooth(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<BoothManager>>() {
                        override fun success(data: List<BoothManager>?) {
                            availableBooths.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var expoDetail = MutableLiveData<ExpoConfig>()

    fun loadExpoDetail(expoId: Long) {
            addDisposable(noAuthService.getExpoDetail(expoId)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ExpoConfig>() {
                        override fun success(data: ExpoConfig?) {
                            data?.let {
                                expoDetail.postValue(it)
                            }
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
    }

    var boothAssigned = MutableLiveData<Any>()

    fun assignBooth(positionId: Long, boothId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["id_booth"] = boothId

        addDisposable(authService.assignBooth(positionId, fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        boothAssigned.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var ticket = MutableLiveData<Ticket>()

    fun getTicket(fairId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["fair_id"] = fairId

        addDisposable(authService.createTicket(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Ticket>() {
                    override fun success(data: Ticket?) {
                        ticket.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))

    }
}