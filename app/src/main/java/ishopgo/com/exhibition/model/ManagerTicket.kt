package ishopgo.com.exhibition.model

import com.google.gson.annotations.SerializedName

class ManagerTicket {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("ticket")
    var ticket: List<Ticket>? = null
}