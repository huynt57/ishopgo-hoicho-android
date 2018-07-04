package ishopgo.com.exhibition.model.administrator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class Administrator : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
}