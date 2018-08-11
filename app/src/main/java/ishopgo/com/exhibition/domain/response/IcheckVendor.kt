package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckVendor {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("website")
    var website: String? = null
    @SerializedName("product_count")
    var productCount: String? = null
    @SerializedName("is_verify")
    var isVerify: Boolean? = null
    @SerializedName("star")
    var star: String? = null
    @SerializedName("country")
    var country: IcheckCountry? = null
}