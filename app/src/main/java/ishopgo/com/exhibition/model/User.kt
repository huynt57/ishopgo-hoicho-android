package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


/**
 * Created by hoangnh on 4/27/2018.
 */
class User : IdentityData() {
    @SerializedName("token")
    @Expose
    var token: String = ""
    @SerializedName("name")
    @Expose
    var name: String = ""
    @SerializedName("image")
    @Expose
    var image: String = ""
    @SerializedName("type")
    @Expose
    var type: String = ""
}