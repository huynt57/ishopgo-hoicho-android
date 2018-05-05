package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class LoadMoreRequest : Request() {

    var limit: Int = 0

    var offset: Int = 0

    fun toMap(): MutableMap<String, Any> {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = limit
        fields["offset"] = offset
        return fields
    }
}