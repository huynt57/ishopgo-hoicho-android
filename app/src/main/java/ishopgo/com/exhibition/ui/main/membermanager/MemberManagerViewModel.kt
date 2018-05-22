package ishopgo.com.exhibition.ui.main.membermanager

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.DeletedMemberRequest
import ishopgo.com.exhibition.domain.request.MemberRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.member.ManageMember
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class MemberManagerViewModel : BaseListViewModel<List<MemberManagerProvider>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var totalProduct = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is MemberRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["start_time"] = params.start_time
            fields["end_time"] = params.end_time
            fields["phone"] = params.phone
            fields["name"] = params.name
            fields["region"] = params.region

            addDisposable(isgService.getMember(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManageMember>() {
                        override fun success(data: ManageMember?) {
                            dataReturned.postValue(data?.member ?: mutableListOf())
                            totalProduct.postValue(data?.total ?: 0)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var deleteSusscess = MutableLiveData<Boolean>()

    fun deleteMember(member_Id: Long) {

        addDisposable(isgService.deleteMember(member_Id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        deleteSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
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

    fun loadDeletedMember(params: Request) {
        if (params is DeletedMemberRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["start_time"] = params.start_time
            fields["end_time"] = params.end_time
            fields["phone"] = params.phone
            fields["name"] = params.name
            fields["region"] = params.region
            fields["deleted_at"] = params.deleted_at

            addDisposable(isgService.getDeletedMember(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManageMember>() {
                        override fun success(data: ManageMember?) {
                            dataReturned.postValue(data?.member ?: mutableListOf())
                            totalProduct.postValue(data?.total ?: 0)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var restoreSusscess = MutableLiveData<Boolean>()

    fun restoreMembers(member_Id: Long) {

        addDisposable(isgService.restoreMembers(member_Id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        restoreSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}