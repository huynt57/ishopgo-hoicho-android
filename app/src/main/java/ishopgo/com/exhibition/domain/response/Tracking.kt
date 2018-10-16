package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tracking : IdentityData() {
    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("lng")
    @Expose
    var lng: String? = null
    @SerializedName("sort")
    @Expose
    var sort: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
    @SerializedName("value_sync")
    @Expose
    var valueSync: ValueSync? = null
}