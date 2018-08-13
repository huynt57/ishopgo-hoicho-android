package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IcheckShopDetail : IdentityData() {
    @SerializedName("gln_code")
    @Expose
    var glnCode: String? = null
    @SerializedName("internal_code")
    @Expose
    var internalCode: String? = null
    @SerializedName("avatar")
    @Expose
    var avatar: Any? = null
    @SerializedName("cover")
    @Expose
    var cover: Any? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("phones")
    @Expose
    var phones: Any? = null
    @SerializedName("skype")
    @Expose
    var skype: Any? = null
    @SerializedName("facebook")
    @Expose
    var facebook: Any? = null
    @SerializedName("email")
    @Expose
    var email: Any? = null
    @SerializedName("website")
    @Expose
    var website: Any? = null
    @SerializedName("country_id")
    @Expose
    var countryId: Int? = null
    @SerializedName("other")
    @Expose
    var other: Any? = null
    @SerializedName("prefix")
    @Expose
    var prefix: String? = null
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("icheck_id")
    @Expose
    var icheckId: Any? = null
    @SerializedName("tax_code")
    @Expose
    var taxCode: Any? = null
    @SerializedName("is_verify")
    @Expose
    var isVerify: Boolean? = null
    @SerializedName("expiration_date")
    @Expose
    var expirationDate: Any? = null
    @SerializedName("scan_count")
    @Expose
    var scanCount: Int? = null
    @SerializedName("description")
    @Expose
    var description: Any? = null
    @SerializedName("createdAt")
    @Expose
    var createdAt: Any? = null
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: Any? = null
    @SerializedName("country")
    @Expose
    var country: IcheckCountry? = null
    @SerializedName("product_count")
    @Expose
    var productCount: Int? = null
    @SerializedName("attributes")
    @Expose
    var attributes: List<Any>? = null
    @SerializedName("review_count")
    @Expose
    var reviewCount: Int? = null
    @SerializedName("star")
    @Expose
    var star: Double? = null
}