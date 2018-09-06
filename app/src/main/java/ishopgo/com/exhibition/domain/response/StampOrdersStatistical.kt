package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StampOrdersStatistical {
    @SerializedName("stamp_total")
    @Expose
    var stampTotal: String? = null
    @SerializedName("stamp_active")
    @Expose
    var stampActive: String? = null
    @SerializedName("stamp_empty")
    @Expose
    var stampEmpty: Int? = null
    @SerializedName("stamp_warning")
    @Expose
    var stampWarning: Int? = null
    @SerializedName("stamp_user_scan")
    @Expose
    var stampUserScan: Int? = null
    @SerializedName("stamp_detail_total")
    @Expose
    var stampDetailTotal: Int? = null
}