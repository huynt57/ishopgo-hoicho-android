package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tracking : IdentityData() {
    @SerializedName("lat")
    @Expose
    var lat: Double = 0.0
    @SerializedName("lng")
    @Expose
    var lng: Double = 0.0
    @SerializedName("sort")
    @Expose
    var sort: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
    @SerializedName("value_name")
    @Expose
    var valueName: String? = null
    @SerializedName("value_type")
    @Expose
    var valueType: String? = null
    @SerializedName("value_phone")
    @Expose
    var valuePhone: String? = null
    @SerializedName("value_address")
    @Expose
    var valueAddress: String? = null
    @SerializedName("value_district")
    @Expose
    var valueDistrict: String? = null
    @SerializedName("value_city")
    @Expose
    var valueCity: String? = null
}