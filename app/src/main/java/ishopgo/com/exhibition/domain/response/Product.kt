package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class Product : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("price")
    @Expose
    var price: Long = 0
    @SerializedName("promotion_price")
    @Expose
    var promotionPrice: Long = 0
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("department_name")
    @Expose
    var departmentName: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
}