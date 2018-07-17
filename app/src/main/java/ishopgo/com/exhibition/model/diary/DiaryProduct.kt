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
    var images: List<DiaryImages>? = null
    @SerializedName("can_delete")
    @Expose
    var canDelete: Int? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = null
    @SerializedName("product_id")
    @Expose
    var productId: Long? = null
}