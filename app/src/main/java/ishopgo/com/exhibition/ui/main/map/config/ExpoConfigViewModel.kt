package ishopgo.com.exhibition.ui.main.map.config

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoConfigViewModel : BaseListViewModel<List<ExpoConfig>>(), AppComponent.Injectable {

    @Inject
    lateinit var appContext: Application

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(noAuthService.getExpos(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ExpoConfig>>() {
                        override fun success(data: List<ExpoConfig>?) {
                            dataReturned.postValue(data ?: listOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }

                    })
            )

        }
    }

    var addSuccess = MutableLiveData<Boolean>()
    fun addExpo(avatar: Uri, name: String, startTime: String, endTime: String, address: String, description: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("start_time", startTime)
                .addFormDataPart("end_time", endTime)
                .addFormDataPart("address", address)
                .addFormDataPart("description", description)

        avatar.let {
            val imageFile = File(appContext.cacheDir, "postImage13213.jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, it, 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            builder.addFormDataPart("image", imageFile.name, imageBody)
        }

        addDisposable(authService.addExpo(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        addSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}