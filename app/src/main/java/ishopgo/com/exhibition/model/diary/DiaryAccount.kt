package ishopgo.com.exhibition.model.diary

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DiaryAccount {
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
}