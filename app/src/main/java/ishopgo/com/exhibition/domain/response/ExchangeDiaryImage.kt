package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ExchangeDiaryImage : IdentityData() {
    @SerializedName("exchange_diary_product_id")
    @Expose
    var exchangeDiaryProductId: Int? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("name")
    @Expose
    var name: Any? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}