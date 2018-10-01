package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ExchangeDiaryProduct : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null
    @SerializedName("expiry_date")
    @Expose
    var expiryDate: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Int? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("sender_id")
    @Expose
    var senderId: Int? = null
    @SerializedName("sender_type")
    @Expose
    var senderType: Int? = null
    @SerializedName("sender_type_name")
    @Expose
    var senderTypeName: String? = null
    @SerializedName("sender_booth_name")
    @Expose
    var senderBoothName: String? = null
    @SerializedName("sender_booth_phone")
    @Expose
    var senderBoothPhone: String? = null
    @SerializedName("sender_booth_admin_name")
    @Expose
    var senderBoothAdminName: String? = null
    @SerializedName("receiver_id")
    @Expose
    var receiverId: Int? = null
    @SerializedName("receiver_type")
    @Expose
    var receiverType: Int? = null
    @SerializedName("receiver_type_name")
    @Expose
    var receiverTypeName: String? = null
    @SerializedName("receiver_booth_name")
    @Expose
    var receiverBoothName: String? = null
    @SerializedName("receiver_booth_phone")
    @Expose
    var receiverBoothPhone: String? = null
    @SerializedName("receiver_booth_admin_name")
    @Expose
    var receiverBoothAdminName: String? = null
    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("lng")
    @Expose
    var lng: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: String? = null
    @SerializedName("stamp_id")
    @Expose
    var stampId: Int? = null
    @SerializedName("stamp_code")
    @Expose
    var stampCode: String? = null
    @SerializedName("images")
    @Expose
    var images: List<ExchangeDiaryImage>? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}