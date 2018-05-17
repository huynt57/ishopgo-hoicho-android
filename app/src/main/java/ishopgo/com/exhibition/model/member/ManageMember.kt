package ishopgo.com.exhibition.model.member

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ManageMember {
    @SerializedName("member")
    @Expose
    var member: List<MemberManager>? = null
    @SerializedName("total")
    @Expose
    var total: Int = 0
}