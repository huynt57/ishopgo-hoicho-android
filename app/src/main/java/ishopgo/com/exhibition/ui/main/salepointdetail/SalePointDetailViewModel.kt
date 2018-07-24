package ishopgo.com.exhibition.ui.main.salepointdetail

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.NewConversation
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import okhttp3.MultipartBody

class SalePointDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var getData = MutableLiveData<ManagerSalePointDetail>()

    fun loadData(phone: String, productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["phone"] = phone
        fields["product_id"] = productId

        addDisposable(noAuthService.getSalePointDetail(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ManagerSalePointDetail>() {
                    override fun success(data: ManagerSalePointDetail?) {
                        getData.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
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

    var deleteSuccess = MutableLiveData<Boolean>()
    var deleteSuccessCurrent = MutableLiveData<Boolean>()

    fun deleteProductInSalePoint(phone: String, productId: Long, productCurrent: Boolean) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("product_id", productId.toString())

        addDisposable(authService.deleteProductInSalePoint(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        if (productCurrent) deleteSuccessCurrent.postValue(true) else deleteSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))

    }
}