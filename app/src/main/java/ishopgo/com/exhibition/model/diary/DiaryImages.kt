package ishopgo.com.exhibition.model.diary

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DiaryImages {
    @SerializedName("image")
    @Expose
    var image: String? = null
}