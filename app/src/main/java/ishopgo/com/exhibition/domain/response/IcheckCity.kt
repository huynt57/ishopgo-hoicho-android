package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class IcheckCity {
    @SerializedName("cities")
    @Expose
    var cities: List<City>? = null

    class City :IdentityData() {
        @SerializedName("city_name")
        @Expose
        var cityName: String? = null
        @SerializedName("phone_code")
        @Expose
        var phoneCode: String? = null
    }
}