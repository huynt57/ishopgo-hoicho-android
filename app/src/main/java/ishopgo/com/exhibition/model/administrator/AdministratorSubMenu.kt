package ishopgo.com.exhibition.model.administrator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AdministratorSubMenu {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("value")
    @Expose
    var value: Boolean? = null
    @SerializedName("role")
    @Expose
    var role: List<AdministratorRole>? = null

    var isSelected: Boolean = false
}