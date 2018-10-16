package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ScuentificCouncils {
    @SerializedName("advisors")
    @Expose
    var advisors: MutableList<Advisor>? = null

    class Advisor : IdentityData() {
        @SerializedName("username")
        @Expose
        var username: Any? = null
        @SerializedName("facebook_id")
        @Expose
        var facebookId: Any? = null
        @SerializedName("google_id")
        @Expose
        var googleId: Any? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("phone")
        @Expose
        var phone: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("address")
        @Expose
        var address: String? = null
        @SerializedName("image")
        @Expose
        var image: String? = null
        @SerializedName("position")
        @Expose
        var position: Any? = null
        @SerializedName("remember_token")
        @Expose
        var rememberToken: String? = null
        @SerializedName("type")
        @Expose
        var type: Int? = null
        @SerializedName("discount")
        @Expose
        var discount: Any? = null
        @SerializedName("status")
        @Expose
        var status: Int? = null
        @SerializedName("birthday")
        @Expose
        var birthday: Any? = null
        @SerializedName("provider_id")
        @Expose
        var providerId: Any? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
        @SerializedName("rank")
        @Expose
        var rank: Any? = null
        @SerializedName("bank_number")
        @Expose
        var bankNumber: Any? = null
        @SerializedName("bank_address")
        @Expose
        var bankAddress: Any? = null
        @SerializedName("bank_name")
        @Expose
        var bankName: Any? = null
        @SerializedName("bank_account_name")
        @Expose
        var bankAccountName: Any? = null
        @SerializedName("otp")
        @Expose
        var otp: Any? = null
        @SerializedName("device_token_ios")
        @Expose
        var deviceTokenIos: Any? = null
        @SerializedName("device_token_android")
        @Expose
        var deviceTokenAndroid: Any? = null
        @SerializedName("order_money")
        @Expose
        var orderMoney: Any? = null
        @SerializedName("wrong_money")
        @Expose
        var wrongMoney: Any? = null
        @SerializedName("cod_money")
        @Expose
        var codMoney: Any? = null
        @SerializedName("order_cnt")
        @Expose
        var orderCnt: Any? = null
        @SerializedName("facebook_url")
        @Expose
        var facebookUrl: Any? = null
        @SerializedName("facebook_token")
        @Expose
        var facebookToken: Any? = null
        @SerializedName("facebook_name")
        @Expose
        var facebookName: Any? = null
        @SerializedName("token_expire")
        @Expose
        var tokenExpire: Any? = null
        @SerializedName("domain")
        @Expose
        var domain: Any? = null
        @SerializedName("shop_name")
        @Expose
        var shopName: Any? = null
        @SerializedName("theme_id")
        @Expose
        var themeId: Int? = null
        @SerializedName("domain_type")
        @Expose
        var domainType: Any? = null
        @SerializedName("shop_status")
        @Expose
        var shopStatus: Any? = null
        @SerializedName("token_auto_login")
        @Expose
        var tokenAutoLogin: String? = null
        @SerializedName("shop_id")
        @Expose
        var shopId: Int? = null
        @SerializedName("is_shop")
        @Expose
        var isShop: Int? = null
        @SerializedName("region")
        @Expose
        var region: String? = null
        @SerializedName("type_text")
        @Expose
        var typeText: String? = null
        @SerializedName("company_store")
        @Expose
        var companyStore: String? = null
        @SerializedName("cr_lock")
        @Expose
        var crLock: String? = null
        @SerializedName("is_ctv")
        @Expose
        var isCtv: Int? = null
        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null
        @SerializedName("debt")
        @Expose
        var debt: Int? = null
        @SerializedName("use_sample")
        @Expose
        var useSample: Int? = null
        @SerializedName("time_otp")
        @Expose
        var timeOtp: Any? = null
        @SerializedName("debt_day")
        @Expose
        var debtDay: String? = null
        @SerializedName("is_use_ck_sp_mh")
        @Expose
        var isUseCkSpMh: Int? = null
        @SerializedName("is_use_ck_sp_bl")
        @Expose
        var isUseCkSpBl: Int? = null
        @SerializedName("amount_can_be_withdrawed")
        @Expose
        var amountCanBeWithdrawed: Int? = null
        @SerializedName("job_status")
        @Expose
        var jobStatus: Int? = null
        @SerializedName("customer_shop_id")
        @Expose
        var customerShopId: String? = null
        @SerializedName("tax_code")
        @Expose
        var taxCode: Any? = null
        @SerializedName("cron_reset_ck_status")
        @Expose
        var cronResetCkStatus: Int? = null
        @SerializedName("date_debt")
        @Expose
        var dateDebt: Any? = null
        @SerializedName("debt_limit")
        @Expose
        var debtLimit: Int? = null
        @SerializedName("latest_personal_tax")
        @Expose
        var latestPersonalTax: Int? = null
        @SerializedName("customer_number")
        @Expose
        var customerNumber: Int? = null
        @SerializedName("number_order")
        @Expose
        var numberOrder: Int? = null
        @SerializedName("phu_trach_number")
        @Expose
        var phuTrachNumber: Int? = null
        @SerializedName("install_app")
        @Expose
        var installApp: Int? = null
        @SerializedName("lat")
        @Expose
        var lat: String? = null
        @SerializedName("lng")
        @Expose
        var lng: String? = null
        @SerializedName("title")
        @Expose
        var title: Any? = null
        @SerializedName("shop_note")
        @Expose
        var shopNote: Any? = null
        @SerializedName("branch_id")
        @Expose
        var branchId: Any? = null
        @SerializedName("type_platform")
        @Expose
        var typePlatform: Int? = null
        @SerializedName("id_app")
        @Expose
        var idApp: Any? = null
        @SerializedName("date_start")
        @Expose
        var dateStart: String? = null
        @SerializedName("date_end")
        @Expose
        var dateEnd: String? = null
        @SerializedName("status_use")
        @Expose
        var statusUse: Int? = null
        @SerializedName("city")
        @Expose
        var city: Any? = null
        @SerializedName("district")
        @Expose
        var district: String? = null
        @SerializedName("package_name_android")
        @Expose
        var packageNameAndroid: Any? = null
        @SerializedName("package_name_ios")
        @Expose
        var packageNameIos: Any? = null
        @SerializedName("isi_ios")
        @Expose
        var isiIos: String? = null
        @SerializedName("introduction")
        @Expose
        var introduction: String? = null
        @SerializedName("expo_map")
        @Expose
        var expoMap: Any? = null
        @SerializedName("booth_name")
        @Expose
        var boothName: Any? = null
        @SerializedName("type_account")
        @Expose
        var typeAccount: Int? = null
        @SerializedName("type_web")
        @Expose
        var typeWeb: Int? = null
        @SerializedName("key_firebase")
        @Expose
        var keyFirebase: Any? = null
        @SerializedName("domain_firebase")
        @Expose
        var domainFirebase: Any? = null
        @SerializedName("scheme_ios")
        @Expose
        var schemeIos: Any? = null
        @SerializedName("parent_id")
        @Expose
        var parentId: Any? = null
        @SerializedName("count_product")
        @Expose
        var countProduct: Int? = null
        @SerializedName("fb_client_id")
        @Expose
        var fbClientId: Any? = null
        @SerializedName("fb_client_secret")
        @Expose
        var fbClientSecret: Any? = null
        @SerializedName("upgrade_status")
        @Expose
        var upgradeStatus: Int? = null
        @SerializedName("is_business")
        @Expose
        var isBusiness: Int? = null
        @SerializedName("mst")
        @Expose
        var mst: String? = null
        @SerializedName("so_dkkd")
        @Expose
        var soDkkd: String? = null
        @SerializedName("ngay_dkkd")
        @Expose
        var ngayDkkd: String? = null
        @SerializedName("noi_cap_dkkd")
        @Expose
        var noiCapDkkd: String? = null
    }
}