package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Introduction {

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
}