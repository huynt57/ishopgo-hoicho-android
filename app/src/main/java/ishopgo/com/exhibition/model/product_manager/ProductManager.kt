package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider

class ProductManager : IdentityData(), ProductManagerProvider {
    override fun provideStatus(): String {
        var provideStatus = "Không hiển thị"
        if (status == STATUS_DISPLAY_SHOW) {
            provideStatus = "Hiển thị dạng chuẩn "
        }
        if (status == STATUS_DISPLAY_LANDING_PAGE)
            provideStatus = "Hiển thị dạng landing page"

        return provideStatus
    }

    override fun provideDepartment(): String {
        return department ?: ""
    }

    override fun provideId(): Long {
        return id
    }

    override fun provideName(): String {
        return name ?: ""
    }

    override fun provideImage(): String {
        return image ?: ""
    }

    override fun provideCode(): String {
        return "MSP: $code"
    }

    override fun provideTTPrice(): String {
        return ttPrice?.asMoney() ?: "0 đ"
    }

    override fun providePrice(): String {
        return price?.asMoney() ?: "0 đ"
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("tt_price")
    @Expose
    var ttPrice: Long? = null
    @SerializedName("price")
    @Expose
    var price: Long? = null
    @SerializedName("status")
    var status: Int? = null
    @SerializedName("department")
    var department: String? = null
    @SerializedName("created_at")
    var provider_price: Long = 0
    @SerializedName("provider_code")
    var provider_code: Int = 0
    @SerializedName("barcode")
    var barcode: String? = null
    @SerializedName("category")
    var category: String? = null
    @SerializedName("total")
    var total: Int = 0

    companion object {
        var STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        var STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dạng landing page
    }
}