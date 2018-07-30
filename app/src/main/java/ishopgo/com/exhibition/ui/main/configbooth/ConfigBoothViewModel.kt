package ishopgo.com.exhibition.ui.main.configbooth

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.showStackTrace
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by hoangnh on 5/7/2018.
 */
class ConfigBoothViewModel : BaseApiViewModel(), AppComponent.Injectable {
    val TAG = "ConfigBoothViewModel"
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    @Inject
    lateinit var appContext: Application

    val relateBooths = MutableLiveData<List<BoothManager>>()

    fun loadRelateBooths(request: Request) {
        if (request is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = request.limit
            fields["offset"] = request.offset

            addDisposable(authService.getBooth(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<BoothManagerWrapper>() {
                        override fun success(data: BoothManagerWrapper?) {
                            relateBooths.postValue(data?.booths ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var editSusscess = MutableLiveData<Boolean>()


    fun editConfigBooth(name: String, hotline: String, introduction: String, image: Uri?, logo: Uri?, city: String?, district: String?, title: String?) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("introduction", introduction)
                .addFormDataPart("hotline", hotline)

        city?.let {
            builder.addFormDataPart("city", it)
        }

        district?.let {
            builder.addFormDataPart("district", it)
        }

        title?.let {
            builder.addFormDataPart("title", it)
        }

        if (image != null) {
            val imageFile = File(appContext.cacheDir, "banner_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, image, 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            val imagePart = MultipartBody.Part.createFormData("banner", imageFile.name, imageBody)
            builder.addPart(imagePart)
        }

        if (logo != null) {
            val imageFile = File(appContext.cacheDir, "logo_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, logo, 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            val imagePart = MultipartBody.Part.createFormData("logo", imageFile.name, imageBody)
            builder.addPart(imagePart)
        }

        addDisposable(authService.editConfigBooth(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        editSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))

    }

    var getConfigBooth = MutableLiveData<BoothConfig>()
    var boothTypes = MutableLiveData<List<String>>()

    fun getConfigBooth() {
        val types = noAuthService.getBoothTypes()
        val configBooth = authService.getConfigBooth()

        addDisposable(Single.zip(types, configBooth, BiFunction { t1: BaseResponse<List<String>>, t2: BaseResponse<BoothConfig> ->
            boothTypes.postValue(t1.data ?: listOf())
            getConfigBooth.postValue(t2.data)
        })
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<Unit>() {
                    override fun onSuccess(t: Unit?) {

                    }

                    override fun onError(e: Throwable?) {
                        resolveError(BaseErrorSignal.ERROR_UNKNOWN, e?.showStackTrace() ?: "")
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