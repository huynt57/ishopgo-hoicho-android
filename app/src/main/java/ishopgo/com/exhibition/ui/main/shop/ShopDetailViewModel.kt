package ishopgo.com.exhibition.ui.main.shop

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.ProductFollow
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
    var shopFollow = MutableLiveData<Boolean>()
    var shopId = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun updateShopImage(shop_id: Long, follow: Boolean, url: String) {
        shopId.postValue(shop_id)
        shopImage.postValue(url)
        shopFollow.postValue(follow)
    }

    var editSusscess = MutableLiveData<Boolean>()


    fun editConfigBooth(name: String, description:String, image: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        if (name != "")
            builder.addFormDataPart("name", name)

        if (description != "")
            builder.addFormDataPart("introduction", description)

        var imagePart: MultipartBody.Part? = null

        if (!image.trim().isEmpty()) {
            val imageFile = File(appContext.cacheDir, "product_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 640, Uri.fromFile(imageFile))
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

    var postFollow = MutableLiveData<ProductFollow>()

    fun postProductFollow(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["id"] = productId
        addDisposable(authService.postProductPollow(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ProductFollow>() {
                    override fun success(data: ProductFollow?) {
                        postFollow.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var deleteSusscess = MutableLiveData<Boolean>()

    fun deleteBooth(booth_Id: Long) {

        addDisposable(authService.deleteBooth(booth_Id)
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
}