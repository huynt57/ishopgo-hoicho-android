package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class Member {

    @SerializedName("id")
    var id: Long = 0
    @SerializedName("name")
    var name: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("facebook_url")
    var facebookUrl: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("title_text")
    var titleText: String? = null
    @SerializedName("facebook_name")
    var facebookName: String? = null
    @SerializedName("birthday")
    var birthday: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("company")
    var company: String? = null
    @SerializedName("region")
    var region: String? = null
    @SerializedName("tax_code")
    var taxCode: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("ck_mh")
    var ckMh: String? = ""
    @SerializedName("ck_si")
    var ckSi: String? = ""
    @SerializedName("ck_le")
    var ckLe: String? = ""
    @SerializedName("bank_number")
    var bankNumber: String? = null
    @SerializedName("bank_name")
    var bankName: String? = null
    @SerializedName("bank_account_name")
    var bankAccountName: String? = null
    @SerializedName("bank_address")
    var bankAddress: String? = null
    @SerializedName("manager_phone")
    var managerPhone: String? = null
    @SerializedName("manager_name")
    var managerName: String? = null
    @SerializedName("image")
    var image: String? = null
    @SerializedName("type")
    var type: Int = 0
    @SerializedName("balance")
    var balance: Long = 0
    @SerializedName("hhcd")
    var hhcd: Long = 0
    @SerializedName("with_draw")
    var withDraw: Long = 0
    @SerializedName("number_order")
    var numberOrder: Long = 0
    @SerializedName("amount_with_drawed")
    var withDrawed: Long = 0
    @SerializedName("money_spend")
    var moneySpend: Long = 0
    @SerializedName("total_revenue")
    var totalRevenue: Long = 0
    @SerializedName("phu_trach_number")
    var phuTrachNumber: Int = 0
    @SerializedName("date_care")
    var dateCare: String? = null
    @SerializedName("customer")
    var customer: Int = 0
    @SerializedName("type_text")
    var typeText: String? = null
    @SerializedName("install_app")
    var installApp: String? = null
    @SerializedName("note_care")
    var noteCare: List<MemberNote>? = null
    @SerializedName("tags")
    var tags: List<String>? = null

    @SerializedName("is_ck_mh")
    var is_CkMh: Int = 0
    @SerializedName("is_ck_si")
    var is_CkSi: Int = 0
    @SerializedName("is_ck_le")
    var is_CkLe: Int = 0
    @SerializedName("ck_mh_text")
    var ckMhText: String? = null
    @SerializedName("ck_si_text")
    var ckSiText: String? = null
    @SerializedName("ck_le_text")
    var ckLeText: String? = null
    @SerializedName("ck_mh_range_text")
    var ckMhRangeText: String? = null
    @SerializedName("ck_si_range_text")
    var ckSiRangeText: String? = null
    @SerializedName("ck_le_range_text")
    var ckLeRangeText: String? = null
    @SerializedName("is_use_ck_sp_mh")
    var isUseCkSpMh: Int = 0
    @SerializedName("is_use_ck_sp_si")
    var isUseCkSpSi: Int = 0
    @SerializedName("is_use_ck_sp_bl")
    var isUseCkSpBl: Int = 0
}
