package ishopgo.com.exhibition.model.question

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class QuestionObject : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("status")
    @Expose
    var status: Int = 0
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: Long = -1L
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
}