package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


/**
 * Created by hoangnh on 5/2/2018.
 */
class Profile : IdentityData() {
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
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("bank_number")
    @Expose
    var bankNumber: String? = null
    @SerializedName("bank_name")
    @Expose
    var bankName: String? = null
    @SerializedName("bank_account_name")
    @Expose
    var bankAccountName: String? = null
    @SerializedName("bank_address")
    @Expose
    var bankAddress: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("type")
    @Expose
    var type: Int = 0
    @SerializedName("type_text")
    @Expose
    var typeText: String? = null
    @SerializedName("type_text_expo")
    @Expose
    var typeTextExpo: String? = null
    @SerializedName("tax_code")
    @Expose
    var taxCode: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("introduction")
    @Expose
    var introduction: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
}