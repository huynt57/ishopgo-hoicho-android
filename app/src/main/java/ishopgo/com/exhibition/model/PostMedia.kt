package ishopgo.com.exhibition.model

import android.graphics.Bitmap
import android.net.Uri
import ishopgo.com.exhibition.domain.response.IdentityData

/**
 * Created by hoangnh on 4/27/2018.
 */
class PostMedia : IdentityData() {
    var uri: Uri? = null
    var thumbBmp: Bitmap? = null
    var thumbUri: Uri? = null
}