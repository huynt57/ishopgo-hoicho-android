package ishopgo.com.exhibition.model.member

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerProvider


class MemberManager : IdentityData(), MemberManagerProvider {
    override fun provideName(): String {
        return "Tên thành viên: $name"
    }

    override fun providePhone(): String {
        return phone ?: ""
    }

    override fun provideEmail(): String {
        return "Email: $email"
    }

    override fun provideRegion(): String {
        return "Khu vực: $region"
    }

    override fun provideBooth(): String {
        return ""
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("facebook_url")
    @Expose
    var facebookUrl: String? = null
    @SerializedName("facebook_name")
    @Expose
    var facebookName: String? = null
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("company")
    @Expose
    var company: String? = null
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("tax_code")
    @Expose
    var taxCode: Any? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("ck_mh")
    @Expose
    var ckMh: String? = ""
    @SerializedName("ck_si")
    @Expose
    var ckSi: String? = ""
    @SerializedName("ck_le")
    @Expose
    var ckLe: String? = ""
    @SerializedName("bank_number")
    @Expose
    var bankNumber: Any? = null
    @SerializedName("bank_name")
    @Expose
    var bankName: Any? = null
    @SerializedName("bank_account_name")
    @Expose
    var bankAccountName: Any? = null
    @SerializedName("bank_address")
    @Expose
    var bankAddress: Any? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("type")
    @Expose
    var type: Int = 0
    @SerializedName("manager_phone")
    @Expose
    var managerPhone: String? = null
    @SerializedName("manager_name")
    @Expose
    var managerName: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("title_text")
    @Expose
    var titleText: String? = null
    @SerializedName("ck_mh_text")
    @Expose
    var ckMhText: String? = null
    @SerializedName("ck_si_text")
    @Expose
    var ckSiText: String? = null
    @SerializedName("ck_le_text")
    @Expose
    var ckLeText: String? = null
    @SerializedName("ck_mh_range_text")
    @Expose
    var ckMhRangeText: String? = null
    @SerializedName("ck_si_range_text")
    @Expose
    var ckSiRangeText: String? = null
    @SerializedName("ck_le_range_text")
    @Expose
    var ckLeRangeText: String? = null
    @SerializedName("is_use_ck_sp_mh")
    @Expose
    var isUseCkSpMh: Int = 0
    @SerializedName("is_use_ck_sp_si")
    @Expose
    var isUseCkSpSi: Int = 0
    @SerializedName("is_use_ck_sp_bl")
    @Expose
    var isUseCkSpBl: Int = 0
    @SerializedName("hhcd")
    @Expose
    var hhcd: Int = 0
    @SerializedName("is_ck_mh")
    @Expose
    var isCkMh: Int? = null
    @SerializedName("is_ck_si")
    @Expose
    var isCkSi: Int? = null
    @SerializedName("is_ck_le")
    @Expose
    var isCkLe: Int? = null
    @SerializedName("balance")
    @Expose
    var balance: Int = 0
    @SerializedName("with_draw")
    @Expose
    var withDraw: Int = 0
    @SerializedName("amount_with_drawed")
    @Expose
    var amountWithDrawed: Int = 0
    @SerializedName("money_spend")
    @Expose
    var moneySpend: Int = 0
    @SerializedName("date_care")
    @Expose
    var dateCare: String? = null
    @SerializedName("tags")
    @Expose
    var tags: List<Any>? = null
    @SerializedName("note_care")
    @Expose
    var noteCare: List<Any>? = null
    @SerializedName("number_order")
    @Expose
    var numberOrder: Int = 0
    @SerializedName("total_revenue")
    @Expose
    var totalRevenue: Int = 0
    @SerializedName("phu_trach_number")
    @Expose
    var phuTrachNumber: Int = 0
    @SerializedName("customer")
    @Expose
    var customer: Int = 0
    @SerializedName("type_text")
    @Expose
    var typeText: String? = null
    @SerializedName("install_app")
    @Expose
    var installApp: String? = null

}