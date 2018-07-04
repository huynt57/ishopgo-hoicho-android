package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class ExposRequest : LoadMoreRequest() {

    var time: Int = TYPE_CURRENT

    override fun toMap(): MutableMap<String, Any> {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = limit
        fields["offset"] = offset
        fields["time"] = time
        return fields
    }

    companion object {
        const val TYPE_COMPLETED = -1
        const val TYPE_CURRENT = 0
        const val TYPE_GOING = 1
    }
}