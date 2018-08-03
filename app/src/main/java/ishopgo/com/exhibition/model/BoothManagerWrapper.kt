package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BoothManagerWrapper {
    @SerializedName("booths")
    @Expose
    var booths: List<BoothManager>? = null
    @SerializedName("total")
    @Expose
    var total: Int? = null
}