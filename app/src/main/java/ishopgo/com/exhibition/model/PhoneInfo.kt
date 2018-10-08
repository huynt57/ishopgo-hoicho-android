package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class PhoneInfo {
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("company")
    @Expose
    var company: String? = null
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null
    @SerializedName("tax_code")
    @Expose
    var taxCode: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
}