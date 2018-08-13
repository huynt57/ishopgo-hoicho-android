package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class IcheckCategory : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("parent_id")
    @Expose
    var parentId: Int? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("discount")
    @Expose
    var discount: Any? = null
    @SerializedName("children")
    @Expose
    var children: List<Int>? = null
    @SerializedName("product_count")
    @Expose
    var productCount: Int? = null
}