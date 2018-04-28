package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


/**
 * Created by hoangnh on 4/27/2018.
 */
class LoginResponse : IdentityData() {
    @SerializedName("token")
    @Expose
    val token: String = ""
    @SerializedName("name")
    @Expose
    val name: String = ""
    @SerializedName("image")
    @Expose
    val image: String = ""
    @SerializedName("type")
    @Expose
    val type: String = ""
}