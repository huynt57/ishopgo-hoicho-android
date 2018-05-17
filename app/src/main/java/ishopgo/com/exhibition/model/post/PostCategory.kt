package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class PostCategory:IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("type")
    @Expose
    var type: Int? = null
    @SerializedName("style")
    @Expose
    var style: Int? = null
    @SerializedName("meta_keyword")
    @Expose
    var metaKeyword: String? = null
    @SerializedName("meta_description")
    @Expose
    var metaDescription: String? = null
}