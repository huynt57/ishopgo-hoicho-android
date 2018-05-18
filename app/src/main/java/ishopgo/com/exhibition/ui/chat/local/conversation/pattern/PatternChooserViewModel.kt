package ishopgo.com.exhibition.ui.chat.local.conversation.pattern

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/14/18. HappyCoding!
 */
class PatternChooserViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var patterns = MutableLiveData<List<TextPattern>>()

    fun loadPatterns(offset: Int) {
        val fields = mutableMapOf<String, Any>()
        fields.put("limit", Const.PAGE_LIMIT)
        fields.put("offset", offset)

        addDisposable(isgService.inbox_getPatterns(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<TextPattern>>() {
                    override fun success(data: List<TextPattern>?) {
                        data?.let { patterns.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

}