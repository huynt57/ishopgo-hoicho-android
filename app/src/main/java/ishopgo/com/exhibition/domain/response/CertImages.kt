package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CertImages :IdentityData(){
    @SerializedName("image")
    @Expose
    var image: String? = null
}