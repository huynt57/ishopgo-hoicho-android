package ishopgo.com.exhibition.ui.main.account

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.model.AccountMenuItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class AccountViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var menu = MutableLiveData<List<AccountMenuProvider>>()

    fun loadMenu() {
        val items = mutableListOf<AccountMenuProvider>()
        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_PROFILE,
                R.drawable.ic_menu_profile,
                "Thông tin cá nhân"
        ))
        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_CHANGE_PASSWORD,
                R.drawable.ic_menu_change_password,
                "Đổi mật khẩu"
        ))
        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_LOGOUT,
                R.drawable.ic_menu_logout,
                "Đăng xuất",
                textColor = R.color.md_red_500
        ))

        menu.postValue(items)

    }

    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        loggedOut.postValue(true)
    }

}
