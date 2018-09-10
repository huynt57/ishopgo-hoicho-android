package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.ProductDetail


/**
 * Created by hoangnh on 5/7/2018.
 */
class BoothConfig : IdentityData() {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("introduction")
    @Expose
    var introduction: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("info")
    @Expose
    var info: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("logo")
    @Expose
    var logo: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("cert_images")
    @Expose
    var certImages: List<ProductDetail.ListCert>? = null
}