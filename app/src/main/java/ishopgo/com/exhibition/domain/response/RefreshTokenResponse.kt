package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class RefreshTokenResponse {

    @SerializedName("token")
    @Expose
    var newToken: String = ""

}