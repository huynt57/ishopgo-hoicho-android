package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseViewModel

/**
 * Created by hoangnh on 4/23/2018.
 */
class LoginViewModel : BaseViewModel(), AppComponent.Injectable {
    var loginSuccess = MutableLiveData<Boolean>()
    var registerSuccess = MutableLiveData<Boolean>()
    var forgetSentSuccess = MutableLiveData<Boolean>()
    var loadRegion = MutableLiveData<MutableList<Region>>()


    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun loginAccount(account: String, password: String) {
        loginSuccess.postValue(true)
    }

    fun registerAccount(phone: String, email: String, fullname: String, company: String, birthday: String,
                        region_id: String, address: String, password: String, register_type: String) {
        registerSuccess.postValue(true)
    }

    fun forgetAccount(phone: String) {
        forgetSentSuccess.postValue(true)
    }

    fun loadRegion(offset: Int, regionName: String) {
        val listRegion = mutableListOf<Region>()

        for (i in 0..20) {
            val region = Region()
            region.name = "Khu vực $i"
            region.provinceid = i.toString()
            region.type = i.toString()
            listRegion.add(region)
        }

        loadRegion.postValue(listRegion)
    }
}