package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.chat.local.profile.UserInfoProvider
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml


/**
 * Created by hoangnh on 5/2/2018.
 */
class Profile : IdentityData(), UserInfoProvider {
    override fun provideIntroduction(): CharSequence {
        return "Giới thiệu: ${introduction ?: ""}"
    }

    override fun provideName(): CharSequence {
        return name ?: ""
    }

    override fun providePhone(): CharSequence {
        return "Số điện thoại: <b>${phone ?: ""}</b>".asHtml()
    }

    override fun provideCover(): CharSequence {
        return ""
    }

    override fun provideAvatar(): CharSequence {
        return image ?: ""
    }

    override fun provideDob(): CharSequence {
        return "Ngày sinh: <b>${birthday?.asDate()}</b>".asHtml()
    }

    override fun provideEmail(): CharSequence {
        return "Email: <b>${email ?: ""}</b>".asHtml()
    }

    override fun provideCompany(): CharSequence {
        return "Công ty: <b>${company ?: ""}</b>".asHtml()
    }

    override fun provideRegion(): CharSequence {
        return "Khu vực: <b>${region ?: ""}</b>".asHtml()
    }

    override fun provideAddress(): CharSequence {
        return "Địa chỉ: <b>${address ?: ""}</b>".asHtml()
    }

    override fun provideType(): CharSequence {
        return "Loại tài khoản: <b>${typeTextExpo ?: ""}</b>".asHtml()
    }

    override fun provideJoinedDate(): CharSequence {
        return "Ngày tham gia: <b>${createdAt?.asDate()}</b>".asHtml()
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("facebook_url")
    @Expose
    var facebookUrl: String? = null
    @SerializedName("facebook_name")
    @Expose
    var facebookName: String? = null
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("company")
    @Expose
    var company: String? = null
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("bank_number")
    @Expose
    var bankNumber: String? = null
    @SerializedName("bank_name")
    @Expose
    var bankName: String? = null
    @SerializedName("bank_account_name")
    @Expose
    var bankAccountName: String? = null
    @SerializedName("bank_address")
    @Expose
    var bankAddress: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("type")
    @Expose
    var type: Int = 0
    @SerializedName("type_text")
    @Expose
    var typeText: String? = null
    @SerializedName("type_text_expo")
    @Expose
    var typeTextExpo: String? = null
    @SerializedName("tax_code")
    @Expose
    var taxCode: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("introduction")
    @Expose
    var introduction: String? = null
}