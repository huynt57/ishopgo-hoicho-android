package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class District {
    @SerializedName("districtid")
    @Expose
    var districtid: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("provinceid")
    @Expose
    var provinceid: String? = null
    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("lng")
    @Expose
    var lng: String? = null
}