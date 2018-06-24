package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class RatingProductViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var isCommentEnable = MutableLiveData<ProductDetail>()

    fun enableCommentRating(data: ProductDetail) {
        isCommentEnable.postValue(data)
    }

    var isSusscess = MutableLiveData<Boolean>()

    fun commentSusscess() {
        isSusscess.postValue(true)
    }

}