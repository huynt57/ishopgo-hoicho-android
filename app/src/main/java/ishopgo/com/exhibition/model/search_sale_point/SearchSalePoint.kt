package ishopgo.com.exhibition.model.search_sale_point

import android.text.Spanned
import ishopgo.com.exhibition.domain.response.IdentityData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.home.search.sale_point.SearchSalePointProvider


class SearchSalePoint : IdentityData(), SearchSalePointProvider {
    override fun provideAddress(): Spanned {
        return "Địa chỉ: <b>${address ?: ""}, ${district ?: ""}, ${city ?: ""}</b>".asHtml()
    }

    override fun provideName(): Spanned {
        return "Tên điểm bán: <b>${name ?: ""}</b>".asHtml()
    }

    override fun providePhone(): String {
        return phone ?: ""
    }

    override fun provideCountProduct(): String {
        return "${countProduct ?: 0} sản phẩm"
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
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("count_product")
    @Expose
    var countProduct: Int? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = 0
}