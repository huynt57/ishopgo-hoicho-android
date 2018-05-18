package ishopgo.com.exhibition.ui.main.questmanager

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class QuestionSearchViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var searchKey = MutableLiveData<String>()

    fun search(TAG: String) {
        searchKey.postValue(TAG)
    }
}