package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckProductPost : IdentityData() {
    class ProductPost {
        @SerializedName("gtin_code")
        var gtin_code: String? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("price")
        var price: Long? = null
        @SerializedName("image")
        var image: String? = null
        @SerializedName("category_1")
        var category_1: Long? = null
        @SerializedName("category_2")
        var category_2: Long? = null
        @SerializedName("category_3")
        var category_3: Long? = null
        @SerializedName("category_4")
        var category_4: Long? = null
        @SerializedName("description")
        var description: String? = null
        @SerializedName("attachments")
        var attachments: List<String>? = null

    }

    class Vendor {
        @SerializedName("name")
        var name: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("phone")
        var phone: String? = null
        @SerializedName("email")
        var email: String? = null
        @SerializedName("country")
        var country: Long? = null
    }
}