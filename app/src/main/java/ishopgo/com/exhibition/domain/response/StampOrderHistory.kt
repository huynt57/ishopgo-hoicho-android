package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StampOrderHistory : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("user_name")
    @Expose
    var userName: String? = null
    @SerializedName("user_email")
    @Expose
    var userEmail: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("status_id")
    @Expose
    var statusId: Long? = null
    @SerializedName("status_name")
    @Expose
    var statusName: String? = null
}