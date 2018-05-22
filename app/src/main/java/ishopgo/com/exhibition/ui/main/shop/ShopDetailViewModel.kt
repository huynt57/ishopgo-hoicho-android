package ishopgo.com.exhibition.ui.main.shop

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {
    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    var shopImage = MutableLiveData<String>()
    var shopName = MutableLiveData<String>()
    var shopId = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun updateShopImage(shop_id: Long, name: String, url: String) {
        shopId.postValue(shop_id)
        shopImage.postValue(url)
        shopName.postValue(name)
    }

    var editSusscess = MutableLiveData<Boolean>()


    fun editConfigBooth(name: String, image: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        if (name != "")
            builder.addFormDataPart("name", name)

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
}