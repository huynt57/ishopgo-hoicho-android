package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class BoothManager : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("booth_name")
    @Expose
    var boothName: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("company_store")
    @Expose
    var companyStore: String? = null
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("number_product")
    @Expose
    var numberProduct: Int? = null
    @SerializedName("member_cnt")
    @Expose
    var memberCnt: Int? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
}