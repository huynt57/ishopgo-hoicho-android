package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class IcheckDistrict {
    @SerializedName("districts")
    @Expose
    var districts: List<District>? = null

    class District :IdentityData() {
        @SerializedName("district_name")
        @Expose
        var districtName: String? = null
    }
}