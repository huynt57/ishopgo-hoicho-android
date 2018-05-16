package ishopgo.com.exhibition.model.question

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class QuestionManager {
    @SerializedName("total")
    @Expose
    var total: Int = 0
    @SerializedName("object")
    @Expose
    var objects: List<QuestionObject>? = null
}