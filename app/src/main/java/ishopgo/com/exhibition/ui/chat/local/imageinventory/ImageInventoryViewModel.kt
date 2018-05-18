package ishopgo.com.exhibition.ui.chat.local.imageinventory

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.ImageInventory
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 12/30/17. HappyCoding!
 */
class ImageInventoryViewModel : BaseApiViewModel(), AppComponent.Injectable {
    companion object {
        private val TAG = "ImageInventoryViewModel"
    }

    var images: MutableLiveData<List<ImageInventory>> = MutableLiveData()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun getImagesInventory(offset: Int) {
        val fields = mutableMapOf<String, Any>()
        fields.put("limit", Const.PAGE_LIMIT)
        fields.put("offset", offset.toString())

        addDisposable(isgService.chat_getImageInventory(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<ImageInventory>>() {
                    override fun success(data: List<ImageInventory>?) {
                        val result = data ?: listOf()
                        images.postValue(result)
                    }

                    override fun failure(status: Int, message: String) {
                        Log.d(TAG, "failure $status $message")
                    }

                })
        )
    }

}