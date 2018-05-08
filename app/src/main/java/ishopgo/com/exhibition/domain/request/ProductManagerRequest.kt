package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class ProductManagerRequest : Request() {

    var limit: Int = 0

    var offset: Int = 0

    var name: String = ""

    var code: String = ""

    fun toMap(): MutableMap<String, Any> {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = limit
        fields["offset"] = offset
        fields["name"] = name
        fields["code"] = code
        return fields
    }
}