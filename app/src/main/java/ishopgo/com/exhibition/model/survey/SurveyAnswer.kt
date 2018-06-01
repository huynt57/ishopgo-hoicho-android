package ishopgo.com.exhibition.model.survey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class SurveyAnswer : IdentityData() {
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("type")
    @Expose
    var type: Int? = null
}