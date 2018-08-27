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
    @SerializedName("children")
    @Expose
    var children: List<Int>? = null
    @SerializedName("childrens")
    @Expose
    var  childrens: Int? = null
    @SerializedName("product_count")
    @Expose
    var productCount: Int? = null
    @SerializedName("discount")
    @Expose
    var  discount: Int? = null
    @SerializedName("rank")
    @Expose
    var  rank: Int? = null
    @SerializedName("priority")
    @Expose
    var  priority: Int? = null
    @SerializedName("category_level1")
    @Expose
    var  categoryLevel1: Any? = null
    @SerializedName("category_level0")
    @Expose
    var  categoryLevel0: Any? = null
}