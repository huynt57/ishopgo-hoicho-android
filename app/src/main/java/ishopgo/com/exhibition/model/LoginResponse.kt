package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.BaseDataResponse


/**
 * Created by hoangnh on 4/27/2018.
 */
class LoginResponse : BaseDataResponse() {
    @SerializedName("token")
    @Expose
    val token: String = ""
    @SerializedName("id")
    @Expose
    val id: Long = 0
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