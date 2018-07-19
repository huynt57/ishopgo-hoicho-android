package ishopgo.com.exhibition.ui.chat.local.profile

import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml

/**
 * Created by xuanhong on 6/15/18. HappyCoding!
 */
class ConverterMemberProfile : Converter<Profile, UserInfoProvider> {
    override fun convert(from: Profile): UserInfoProvider {
        return object : UserInfoProvider {
            override fun provideAvatar(): CharSequence {
                return from.image ?: ""
            }

            override fun provideCover(): CharSequence {
                return ""
            }

            override fun provideName(): CharSequence {
                return from.name ?: ""
            }

            override fun info(): CharSequence {
                val builder = StringBuilder()
                builder.append("Số điện thoại: <b>${from.phone ?: ""}</b>")
                builder.append("<br>")
                builder.append("Email: <b>${from.email ?: ""}</b>")
                builder.append("<br>")
                builder.append("Ngày sinh: <b>${from.birthday?.asDate() ?: ""}</b>")
                builder.append("<br>")
                builder.append("Công ty: <b>${from.company ?: ""}</b>")
                builder.append("<br>")
                builder.append("Khu vực: <b>${from.region ?: ""}</b>")
                builder.append("<br>")
                builder.append("Địa chỉ: <b>${from.address ?: ""}</b>")
                builder.append("<br>")
                builder.append("Loại tài khoản: <b>${from.typeTextExpo ?: ""}</b>")
                builder.append("<br>")
                builder.append("Ngày tham gia: <b>${from.createdAt?.asDate() ?: ""}</b>")
                builder.append("<br>")
                builder.append("Giới thiệu: <b>${from.introduction ?: ""}</b>")
                return builder.toString().asHtml()
            }

        }
    }
}