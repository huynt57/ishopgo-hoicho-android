package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class LoginFacebook {
    @SerializedName("checkPhone")
    @Expose
    var checkPhone: Boolean = false
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("id_booth")
    @Expose
    var idBooth: Long? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("hotline_error")
    @Expose
    var hotlineError: String? = null
}