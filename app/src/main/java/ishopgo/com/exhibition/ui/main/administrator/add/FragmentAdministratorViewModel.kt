package ishopgo.com.exhibition.ui.main.administrator.add

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class FragmentAdministratorViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var showFragmentMember = MutableLiveData<Boolean>()

    fun showFragmentMember() {
        showFragmentMember.postValue(true)
    }

    var getDataMember = MutableLiveData<MemberManager>()

    fun getDataMember(data: MemberManager) {
        getDataMember.postValue(data)
    }
}