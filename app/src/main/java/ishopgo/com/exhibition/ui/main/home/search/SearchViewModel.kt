package ishopgo.com.exhibition.ui.main.home.search

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchViewModel : BaseApiViewModel(), AppComponent.Injectable {

    var searchKey = MutableLiveData<String>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun search(key: String) {
        searchKey.postValue(key)
    }

}