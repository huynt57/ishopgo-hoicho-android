package ishopgo.com.exhibition.model

import android.text.Spanned
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.detail.ProductSalePointProvider


class ProductSalePoint : IdentityData(), ProductSalePointProvider {
    override fun provideAddress(): String {
        return "${address ?:""}, ${district?:""}, ${city?:""}"
    }

    override fun providePrice(): String {
        return price?.asMoney() ?: "0 đ"
    }

    override fun providePhone(): String {
        return phone ?: ""
    }

    override fun provideName(): Spanned {
        return "<b>${name ?: ""}</b>".asHtml()
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
    var status: Int = 0
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = 0
    @SerializedName("chat_id")
    @Expose
    var chatId: Long? = 0
}