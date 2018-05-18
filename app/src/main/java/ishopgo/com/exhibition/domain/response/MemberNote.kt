package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class MemberNote {
    @SerializedName("note")
    internal var note: String? = null
    @SerializedName("date_note")
    internal var dateNote: String? = null

}