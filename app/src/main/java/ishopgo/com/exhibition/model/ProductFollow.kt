package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductFollow {
    @SerializedName("status")
    @Expose
    var status: Int = 0
}