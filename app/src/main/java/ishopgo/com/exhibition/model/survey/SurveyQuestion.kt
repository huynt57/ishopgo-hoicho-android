package ishopgo.com.exhibition.model.survey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class SurveyQuestion : IdentityData() {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("answers")
    @Expose
    var answers: MutableList<SurveyAnswer>? = null
}