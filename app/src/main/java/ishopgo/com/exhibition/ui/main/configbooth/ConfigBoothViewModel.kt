package ishopgo.com.exhibition.ui.main.configbooth

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.Booth
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
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

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    var editSusscess = MutableLiveData<Boolean>()


    fun editConfigBooth(name: String, hotline: String, introduction: String, infor: String, address: String, image: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("introduction", introduction)
                .addFormDataPart("info", infor)
                .addFormDataPart("hotline", hotline)
                .addFormDataPart("address", address)

        var imagePart: MultipartBody.Part? = null

        if (!image.trim().isEmpty()) {
            val imageFile = File(appContext.cacheDir, "product_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 2048, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            imagePart = MultipartBody.Part.createFormData("banner", imageFile.name, imageBody)
        }

        if (imagePart != null) {
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

    var getConfigBooth = MutableLiveData<Booth>()

    fun getConfigBooth() {
        addDisposable(authService.getConfigBooth()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Booth>() {
                    override fun success(data: Booth?) {
                        getConfigBooth.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}