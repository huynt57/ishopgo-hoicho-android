package ishopgo.com.exhibition.model.question

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class QuestionCategory : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
}