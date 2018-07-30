package ishopgo.com.exhibition.model

import com.google.gson.annotations.SerializedName

class ManagerBooth {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("booths")
    var booths: List<BoothManager>? = null
}