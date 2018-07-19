package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class PostObject : IdentityData() {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("short_content")
    @Expose
    var shortContent: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("status_text")
    @Expose
    var statusText: String? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
}