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
    val name: String = ""
    @SerializedName("phone")
    @Expose
    val phone: String = ""
    @SerializedName("facebook_url")
    @Expose
    val facebookUrl: String = ""
    @SerializedName("facebook_name")
    @Expose
    val facebookName: String = ""
    @SerializedName("birthday")
    @Expose
    val birthday: String = ""
    @SerializedName("email")
    @Expose
    val email: String = ""
    @SerializedName("company")
    @Expose
    val company: String = ""
    @SerializedName("region")
    @Expose
    val region: String = ""
    @SerializedName("address")
    @Expose
    val address: String = ""
    @SerializedName("created_at")
    @Expose
    val createdAt: String = ""
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String = ""
    @SerializedName("status")
    @Expose
    val status: String = ""
    @SerializedName("bank_number")
    @Expose
    val bankNumber: String = ""
    @SerializedName("bank_name")
    @Expose
    val bankName: String = ""
    @SerializedName("bank_account_name")
    @Expose
    val bankAccountName: String = ""
    @SerializedName("bank_address")
    @Expose
    val bankAddress: String = ""
    @SerializedName("image")
    @Expose
    val image: String = ""
    @SerializedName("type")
    @Expose
    val type: Int = 0
    @SerializedName("type_text")
    @Expose
    val typeText: String = ""
    @SerializedName("tax_code")
    @Expose
    val taxCode: String = ""
    @SerializedName("title")
    @Expose
    val title: String = ""
}