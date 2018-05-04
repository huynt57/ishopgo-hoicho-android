package ishopgo.com.exhibition.model.Community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.community.CommunityProductProvider
import ishopgo.com.exhibition.ui.extensions.asMoney


/**
 * Created by hoangnh on 5/3/2018.
 */
class CommunityProduct : IdentityData(), CommunityProductProvider {
    override fun providerName(): String {
        return name
    }

    override fun providerPrice(): String {
        return price.asMoney()
    }

    override fun providerImage(): String {
        return image
    }

    override fun providerId(): Long {
        return id
    }

    @SerializedName("name")
    @Expose
    var name: String = ""
    @SerializedName("price")
    @Expose
    var price: Long = 0
    @SerializedName("image")
    @Expose
    var image: String = ""
}