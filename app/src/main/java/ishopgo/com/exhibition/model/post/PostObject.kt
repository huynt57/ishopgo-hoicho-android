package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class PostObject : IdentityData() {

    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("short_content")
    @Expose
    val shortContent: String? = null
    @SerializedName("image")
    @Expose
    val image: String? = null
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
    @SerializedName("status")
    @Expose
    val status: Int? = null
    @SerializedName("status_text")
    @Expose
    val statusText: String? = null
    @SerializedName("account_name")
    @Expose
    val accountName: String? = null
    @SerializedName("category_id")
    @Expose
    val categoryId: Int? = null
    @SerializedName("category_name")
    @Expose
    val categoryName: String? = null
}