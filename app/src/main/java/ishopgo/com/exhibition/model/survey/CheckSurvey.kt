package ishopgo.com.exhibition.model.survey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckSurvey {
    @SerializedName("status")
    @Expose
    var status: Int = 0
}