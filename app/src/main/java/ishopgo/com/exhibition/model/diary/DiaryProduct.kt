package ishopgo.com.exhibition.model.diary

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class DiaryProduct : IdentityData() {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("account")
    @Expose
    var account: DiaryAccount? = null
    @SerializedName("images")
    @Expose
    var images: List<String>? = null
    @SerializedName("can_delete")
    @Expose
    var canDelete: Int? = null
}