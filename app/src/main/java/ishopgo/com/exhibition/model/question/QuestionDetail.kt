package ishopgo.com.exhibition.model.question

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuestionDetail {

    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("answer")
    @Expose
    var answer: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: Long = -1L
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
    @SerializedName("link_share")
    @Expose
    var linkShare: String? = null

}