package ishopgo.com.exhibition.model.survey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostSurvey {
    @SerializedName("suvey")
    @Expose
    var suvey: PostAnswer? = null
}