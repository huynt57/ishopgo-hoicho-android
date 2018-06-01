package ishopgo.com.exhibition.model.survey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class Survey : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("status")
    @Expose
    var status: Int = 0
    @SerializedName("questions")
    @Expose
    var questions: MutableList<SurveyQuestion>? = null
}