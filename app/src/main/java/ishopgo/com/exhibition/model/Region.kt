package ishopgo.com.exhibition.model

import com.google.gson.annotations.SerializedName

/**
 * Created by hoangnh on 4/24/2018.
 */
class Region {
    @SerializedName("provinceid")
    var provinceid: String? = null
    @SerializedName("name")
    var name: String = ""
    @SerializedName("type")
    var type: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Region) return false

        val region = other as Region?
        return provinceid == region!!.provinceid
    }

    override fun hashCode(): Int = provinceid!!.hashCode()

    override fun toString(): String = name
}