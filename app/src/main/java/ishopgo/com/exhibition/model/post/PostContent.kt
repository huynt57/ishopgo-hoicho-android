package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostContent {
    @SerializedName("content")
    @Expose
    val content: String? = null
    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
    @SerializedName("link_share")
    @Expose
    val linkShare: String? = null
    @SerializedName("account_name")
    @Expose
    val accountName: String? = null
    @SerializedName("category_name")
    @Expose
    val categoryName: String? = null
}