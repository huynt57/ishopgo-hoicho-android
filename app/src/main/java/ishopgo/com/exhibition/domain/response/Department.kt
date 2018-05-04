package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class Department : IdentityData() {

    @SerializedName("name")
    @Expose
    var name: String? = null

}