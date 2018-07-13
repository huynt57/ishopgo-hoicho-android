package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.ProductDetailComment
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class RatingProductViewModel : BaseApiViewModel() {

    var isCommentEnable = MutableLiveData<ProductDetailComment>()

    fun enableCommentRating(data: ProductDetailComment) {
        isCommentEnable.postValue(data)
    }

    var isSusscess = MutableLiveData<Boolean>()

    fun commentSusscess() {
        isSusscess.postValue(true)
    }

}