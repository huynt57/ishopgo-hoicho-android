package ishopgo.com.exhibition.model.survey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostAnswer {
    @SerializedName("idQuestion")
    @Expose
    var idQuestion: Long? = null
    @SerializedName("idAnswer")
    @Expose
    var idAnswer: Long? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
}