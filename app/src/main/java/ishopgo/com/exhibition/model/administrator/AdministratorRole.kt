package ishopgo.com.exhibition.model.administrator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AdministratorRole {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("value")
    @Expose
    var value: Boolean? = null
}