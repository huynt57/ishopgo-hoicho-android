package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asPhone
import ishopgo.com.exhibition.ui.main.boothfollow.BoothFollowProvider


class BoothFollow : IdentityData(), BoothFollowProvider {
    override fun provideQrCode(): String {
        return qrcode ?: ""
    }

    override fun provideMemberCNT(): String {
        return "Số thành viên quan tâm: $memberCnt"
    }

    override fun provideAddress(): String {
        return "${address?.trim() ?: ""}, ${district?.trim() ?: ""}, ${city?.trim()
                ?: ""}"
    }

    override fun provideName(): String {
        return name ?: ""
    }

    override fun providePhone(): String {
        return phone?.asPhone() ?: ""
    }

    override fun provideNumberProduct(): String {
        return "Số sản phẩm: $numberProduct"
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("number_product")
    @Expose
    var numberProduct: Int? = null
    @SerializedName("member_cnt")
    @Expose
    var memberCnt: Int? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
}