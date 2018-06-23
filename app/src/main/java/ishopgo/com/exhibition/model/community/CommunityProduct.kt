package ishopgo.com.exhibition.model.community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData

/**
 * Created by hoangnh on 5/3/2018.
 */
class CommunityProduct : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("price")
    @Expose
    var price: Long? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
}