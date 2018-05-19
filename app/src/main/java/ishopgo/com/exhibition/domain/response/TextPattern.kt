package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class TextPattern {

    @SerializedName("id")
    @Expose
    var id: Long = 0

    @SerializedName("content")
    @Expose
    var content: String? = null

}