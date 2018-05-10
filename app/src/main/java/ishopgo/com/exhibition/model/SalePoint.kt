package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.salepoint.SalePointProvider

class SalePoint : IdentityData(), SalePointProvider {
    override fun provideWasStatus(): Boolean {
        return wasStatus()
    }

    override fun provideCity(): String {
        return "Thành phố: $city"
    }

    override fun provideDistrict(): String {
        return "Quận huyện: $district"
    }

    override fun providePrice(): String {
        return price?.asMoney() ?: "0 đ"
    }

    override fun provideStatus(): String {
        return if (wasStatus()) "Hiển thị" else "Ẩn"
    }

    override fun provideProductName(): String {
        return "Sản phẩm: $productName"
    }

    override fun provideManagerPhone(): String {
        return provideManagerPhone()
    }

    override fun provideId(): Long {
        return id
    }

    override fun provideName(): String {
        return "Tên điểm bán: $name"
    }

    override fun providePhone(): String {
        return phone ?: ""
    }

    override fun provideAddress(): String {
        return "Địa chỉ: $address"
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("price")
    @Expose
    var price: Long? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    companion object {
        val STATUS_SHOW = 1
    }

    fun wasStatus() = status == STATUS_SHOW
}