package ishopgo.com.exhibition.ui.main.boothmanager

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.BoothManagerWrapper
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class BoothManagerViewModel : BaseListViewModel<List<BoothManager>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getBooth(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<BoothManagerWrapper>() {
                        override fun success(data: BoothManagerWrapper?) {
                            dataReturned.postValue(data?.booths ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var createSusscess = MutableLiveData<Boolean>()

    fun createBoothManager(phone: String, email: String, name: String, birthday: String, address: String, region: String, image: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("email", email)
                .addFormDataPart("name", name)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("address", address)
                .addFormDataPart("region", region)

        var imagePart: MultipartBody.Part? = null

        if (image.trim().isNotEmpty()) {
            val imageFile = File(appContext.cacheDir, "booth_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)
        }

        if (imagePart != null) {
            builder.addPart(imagePart)
        }

        addDisposable(authService.createBooth(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
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
}