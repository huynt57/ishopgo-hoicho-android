package ishopgo.com.exhibition.model

import android.text.Spanned
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asPhone
import ishopgo.com.exhibition.ui.main.ticket.TicketProvider
import ishopgo.com.exhibition.ui.main.ticketmanager.TicketManagerProvider


class Ticket : IdentityData(), TicketProvider, TicketManagerProvider {
    override fun provideBoothName(): String {
        return name ?: ""
    }

    override fun provideTicketAddress(): String {
        return "Địa chỉ: ${address ?: ""}, ${district ?: ""}, ${city ?: ""}"
    }

    override fun provideTicketCode(): String {
        return code ?: ""
    }

    override fun provideBoothPhone(): String {
        return phone?.asPhone() ?: ""
    }

    override fun provideDateScan(): String {
        return "Ngày quét: ${createdAt ?: ""}"
    }

    override fun provideEmail(): Spanned {
        return "Email: <b>${email?.asDateTime() ?: ""}</b>".asHtml()
    }

    override fun provideTicketName(): String {
        return ticketName ?: ""
    }

    override fun provideName(): Spanned {
        return "Họ và tên: <b>${name ?: ""}</b>".asHtml()
    }

    override fun provideCode(): String {
        return code ?: ""
    }

    override fun providePhone(): Spanned {
        return "Số điện thoại: <b>${phone?.asPhone() ?: ""}</b>".asHtml()
    }

    override fun provideAddress(): Spanned {
        return "Địa chỉ: <b>${address ?: ""}, ${district ?: ""}, ${city ?: ""}</b>".asHtml()
    }

    override fun provideBanner(): String {
        return banner ?: ""
    }

    override fun provideCreateAt(): Spanned {
        return "Thời gian: <b>${createdAt?.asDateTime() ?: ""}".asHtml()
    }

    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("ticket_name")
    @Expose
    var ticketName: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
}