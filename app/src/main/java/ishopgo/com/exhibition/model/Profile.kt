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
    var name: String = ""
    @SerializedName("phone")
    @Expose
    var phone: String = ""
    @SerializedName("facebook_url")
    @Expose
    var facebookUrl: String = ""
    @SerializedName("facebook_name")
    @Expose
    var facebookName: String = ""
    @SerializedName("birthday")
    @Expose
    var birthday: String = ""
    @SerializedName("email")
    @Expose
    var email: String = ""
    @SerializedName("company")
    @Expose
    var company: String = ""
    @SerializedName("region")
    @Expose
    var region: String = ""
    @SerializedName("address")
    @Expose
    var address: String = ""
    @SerializedName("created_at")
    @Expose
    var createdAt: String = ""
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String = ""
    @SerializedName("status")
    @Expose
    var status: String = ""
    @SerializedName("bank_number")
    @Expose
    var bankNumber: String = ""
    @SerializedName("bank_name")
    @Expose
    var bankName: String = ""
    @SerializedName("bank_account_name")
    @Expose
    var bankAccountName: String = ""
    @SerializedName("bank_address")
    @Expose
    var bankAddress: String = ""
    @SerializedName("image")
    @Expose
    var image: String = ""
    @SerializedName("type")
    @Expose
    var type: Int = 0
    @SerializedName("type_text")
    @Expose
    var typeText: String = ""
    @SerializedName("tax_code")
    @Expose
    var taxCode: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
}