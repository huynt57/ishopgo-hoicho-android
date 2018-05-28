package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.main.boothmanager.BoothManagerProvider


class BoothManager : IdentityData(), BoothManagerProvider {
    override fun provideName(): String {
        return name ?: ""
    }

    override fun providePhone(): String {
        return "SĐT: ${phone ?: ""}"
    }

    override fun provideCompanyStore(): String {
        return "Công ty/cửa hàng: $companyStore"
    }

    override fun provideRegion(): String {
        return "Khu vực: $region"
    }

    override fun provideNumberProduct(): String {
        return "Số sản phẩm: $numberProduct"
    }

    override fun provideMemberCNT(): String {
        return "Số thành viên quan tâm: $numberProduct"
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("company_store")
    @Expose
    var companyStore: String? = null
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("number_product")
    @Expose
    var numberProduct: Int? = null
    @SerializedName("member_cnt")
    @Expose
    var memberCnt: Int? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: Any? = null
}