package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class Brand : IdentityData(), HighlightBrandProvider {
    override fun provideImage(): String {
        return logo ?: ""
    }

    @SerializedName("logo")
    var logo: String? = null
    @SerializedName("name")
    var name: String? = null

}