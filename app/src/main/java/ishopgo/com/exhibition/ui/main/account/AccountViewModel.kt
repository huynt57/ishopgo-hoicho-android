package ishopgo.com.exhibition.ui.main.account

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.AccountMenuItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import okhttp3.MultipartBody

class AccountViewModel : BaseApiViewModel(), AppComponent.Injectable {
    val TAG = "AccountViewModel"
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var menu = MutableLiveData<List<AccountMenuItem>>()
    var menuAdministrator = MutableLiveData<List<AccountMenuItem>>()
    var changePassword = MutableLiveData<Boolean>()
    var getOTP = MutableLiveData<Boolean>()

    fun loadMenu() {
        val items = mutableListOf<AccountMenuItem>()

        if (UserDataManager.currentType == "Chủ gian hàng") {
            items.add(AccountMenuItem(Const.AccountAction.ACTION_SETTING_BOTTH, R.drawable.ic_booth_manager, "Gian hàng của tôi"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_MY_QR, R.drawable.ic_my_qr, "Mã QR gian hàng"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_PRODUCTS, R.drawable.ic_shopping_favorite_2, "Sản phẩm quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_BOOTHS, R.drawable.ic_star_white, "Gian hàng quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_VISITORS, R.drawable.ic_shop_customer_white, "Khách hàng quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_PRODUCT_MANAGER, R.drawable.ic_shopping_white, "Quản lý sản phẩm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_TICKET, R.drawable.ic_ticket, "Vé thăm quan của tôi"))
//            items.add(AccountMenuItem(Const.AccountAction.ACTION_NOTIFICATION, R.drawable.ic_notification, "Quản lý thông báo"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_REFERENCES, R.drawable.ic_notification, "Thành viên đã giới thiệu"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_BRAND_MANAGER, R.drawable.ic_brands, "Quản lý thương hiệu"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_sale_point, "Quản lý điểm bán"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_ADMINISTRATOR, R.drawable.ic_authorities, "Quản trị viên gian hàng"))

            val childs = ArrayList<AccountMenuItem>()
            childs.add(AccountMenuItem(Const.AccountAction.ACTION_BUY_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Đặt mua tem", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_NO_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Tạo lô tem in", isParent = false))

//            childs.add(AccountMenuItem(Const.AccountAction.ACTION_DISTRICBUTION_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Kích hoạt lô tem", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_STAMP_MANAGER, R.drawable.ic_qr_code_highlight_24dp, "Danh sách tem đã kích hoạt", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_STAMP_WARNING, R.drawable.ic_qr_code_highlight_24dp, "Quản lý tem cảnh báo", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_LIST_SCAN_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Danh sách người quét", isParent = false))

            items.add(AccountMenuItem(Const.AccountAction.ACTION_CREATE_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Tạo mã tem chống giả", childs))

            items.add(AccountMenuItem(Const.AccountAction.ACTION_ADMINISTRATOR, R.drawable.ic_authorities, "Phân quyền quản trị viên"))
        }

        if (UserDataManager.currentType == "Thành viên") {
            items.add(AccountMenuItem(Const.AccountAction.ACTION_REGISTER_BOOTH, R.drawable.ic_register, "Đăng ký gian hàng"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_SURVEY, R.drawable.ic_question, "Bảng khảo sát"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_PRODUCTS, R.drawable.ic_shopping_favorite_2, "Sản phẩm quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_BOOTHS, R.drawable.ic_star_white, "Gian hàng quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_TICKET, R.drawable.ic_ticket, "Vé tham quan của tôi"))
//            items.add(AccountMenuItem(Const.AccountAction.ACTION_NOTIFICATION, R.drawable.ic_notification, "Xem thông báo"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_REFERENCES, R.drawable.ic_notification, "Thành viên đã giới thiệu"))
//            items.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_sale_point, "Điểm bán của tôi"))
        }

        if (UserDataManager.currentType == "Chủ hội chợ") {
            items.add(AccountMenuItem(Const.AccountAction.ACTION_MEMBER_MANAGER, R.drawable.ic_customer_white, "Quản lý thành viên"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_BOOTH_MANAGER, R.drawable.ic_store_white, "Quản lý gian hàng"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_store_white, "Quản lý điểm bán"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_BRAND_MANAGER, R.drawable.ic_brands, "Quản lý thương hiệu"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_PRODUCT_MANAGER, R.drawable.ic_shopping_white, "Quản lý sản phẩm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_TICKET_MANAGER, R.drawable.ic_sight_seeing, "Quản lý vé tham quan"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_CONFIG_EXPO, R.drawable.ic_sight_seeing, "Quản lý hội chợ"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_PRODUCTS, R.drawable.ic_shopping_favorite_2, "Sản phẩm quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_BOOTHS, R.drawable.ic_star_white, "Gian hàng quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_NOTIFICATION, R.drawable.ic_notification, "Quản lý thông báo"))

            val childs = ArrayList<AccountMenuItem>()
            childs.add(AccountMenuItem(Const.AccountAction.ACTION_BUY_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Đặt mua tem", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_NO_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Tạo lô tem in", isParent = false))

//            childs.add(AccountMenuItem(Const.AccountAction.ACTION_DISTRICBUTION_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Kích hoạt lô tem", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_STAMP_MANAGER, R.drawable.ic_qr_code_highlight_24dp, "Danh sách tem đã kích hoạt", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_STAMP_WARNING, R.drawable.ic_qr_code_highlight_24dp, "Quản lý tem cảnh báo", isParent = false))

            childs.add(AccountMenuItem(Const.AccountAction.ACTION_LIST_SCAN_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Danh sách người quét", isParent = false))

            items.add(AccountMenuItem(Const.AccountAction.ACTION_CREATE_STAMP, R.drawable.ic_qr_code_highlight_24dp, "Tạo mã tem chống giả", childs))

            items.add(AccountMenuItem(Const.AccountAction.ACTION_ADMINISTRATOR, R.drawable.ic_authorities, "Phân quyền quản trị viên"))
        }

        if (UserDataManager.currentType == "Quản trị viên") {
            items.add(AccountMenuItem(Const.AccountAction.ACTION_SURVEY, R.drawable.ic_question, "Bảng khảo sát"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_BOOTHS, R.drawable.ic_star_white, "Gian hàng quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_PRODUCTS, R.drawable.ic_shopping_favorite_2, "Sản phẩm quan tâm"))
//            items.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_sale_point, "Điểm bán của tôi"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_REGISTER_BOOTH, R.drawable.ic_register, "Đăng ký gian hàng"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_REFERENCES, R.drawable.ic_notification, "Thành viên đã giới thiệu"))

            val itemsAdmninistrator = mutableListOf<AccountMenuItem>()

            val listPermission = Const.listPermission

            if (listPermission.isNotEmpty()) {
                for (i in listPermission.indices) {
                    if (Const.Permission.EXPO_BOOTH_CUSTOMER == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_VISITORS, R.drawable.ic_shop_customer_white, "Khách hàng quan tâm"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.LIST_PRODUCT == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_PRODUCT_MANAGER, R.drawable.ic_shopping_white, "Quản lý sản phẩm"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.MANAGER_BRAND == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_BRAND_MANAGER, R.drawable.ic_brands, "Quản lý thương hiệu"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.EXPO_BOOTH_SALE_POINT == listPermission[i]) {
                        items.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_store_white, "Quản lý điểm bán"))
                        break
                    }
                }

//                for (i in listPermission.indices) {
//                    if (Const.Permission.EXPO_FAIR_LIST == listPermission[i]) {
//                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_CONFIG_EXPO, R.drawable.ic_sight_seeing, "Quản lý hội chợ"))
//                        break
//                    }
//                }

                menuAdministrator.postValue(itemsAdmninistrator)
            }
        }

        if (UserDataManager.currentType == "Nhân viên gian hàng") {
            items.add(AccountMenuItem(Const.AccountAction.ACTION_SURVEY, R.drawable.ic_question, "Bảng khảo sát"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_BOOTHS, R.drawable.ic_star_white, "Gian hàng quan tâm"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_FAVORITE_PRODUCTS, R.drawable.ic_shopping_favorite_2, "Sản phẩm quan tâm"))
//            items.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_sale_point, "Điểm bán của tôi"))
            items.add(AccountMenuItem(Const.AccountAction.ACTION_REGISTER_BOOTH, R.drawable.ic_register, "Đăng ký gian hàng"))

            val itemsAdmninistrator = mutableListOf<AccountMenuItem>()

            val listPermission = Const.listPermission

            if (listPermission.isNotEmpty()) {
                for (i in listPermission.indices) {
                    if (Const.Permission.EXPO_BOOTH_CUSTOMER == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_VISITORS, R.drawable.ic_shop_customer_white, "Khách hàng quan tâm"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.MANAGER_MEMBER == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_MEMBER_MANAGER, R.drawable.ic_customer_white, "Quản lý thành viên"))
                        break
                    }
                }
                for (i in listPermission.indices) {
                    if (Const.Permission.MANAGER_PROVIDER == listPermission[i]
                            || Const.Permission.DELETE_PROVIDER == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_BOOTH_MANAGER, R.drawable.ic_store_white, "Quản lý gian hàng"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.MANAGER_BRAND == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_BRAND_MANAGER, R.drawable.ic_brands, "Quản lý thương hiệu"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.LIST_PRODUCT == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_PRODUCT_MANAGER, R.drawable.ic_shopping_white, "Quản lý sản phẩm"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.EXPO_FAIR_LIST == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_CONFIG_EXPO, R.drawable.ic_sight_seeing, "Quản lý hội chợ"))
                        break
                    }
                }

                for (i in listPermission.indices) {
                    if (Const.Permission.EXPO_BOOTH_SALE_POINT == listPermission[i]) {
                        itemsAdmninistrator.add(AccountMenuItem(Const.AccountAction.ACTION_SALE_POINT, R.drawable.ic_store_white, "Quản lý điểm bán"))
                        break
                    }
                }

                menuAdministrator.postValue(itemsAdmninistrator)
            }
        }

        // common
//        items.add(AccountMenuItem(Const.AccountAction.ACTION_REPORT, R.drawable.ic_feedback, "Báo lỗi - Đóng góp"))
        items.add(AccountMenuItem(Const.AccountAction.ACTION_CHANGE_PASSWORD, R.drawable.ic_password_white, "Đổi mật khẩu"))
        items.add(AccountMenuItem(Const.AccountAction.ACTION_LOGOUT, R.drawable.ic_logout, "Đăng xuất", textColor = R.color.md_red_500))

        menu.postValue(items)

    }

    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        addDisposable(authService.logout("android")
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : DisposableSingleObserver<Any>() {
                    override fun onError(e: Throwable?) {
                        Log.d(TAG, e.toString())
                    }

                    override fun onSuccess(t: Any?) {
                        loggedOut.postValue(true)
                    }
                })
        )
    }

    fun changePassword(phone: String, otp: String, password: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("otp", otp)
                .addFormDataPart("password", password)

        addDisposable(authService.changePassword(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        changePassword.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun getOTP(phone: String) {
        val fields = mutableMapOf<String, Any>()
        fields["phone"] = phone

        addDisposable(noAuthService.getOTP(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        getOTP.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                }))
    }

}
