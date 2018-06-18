package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoShopLocationRequest : LoadMoreRequest() {

    var expoId: Long = -1L
    var searchKeyword: String? = null

    override fun toMap(): MutableMap<String, Any> {
        val toMap = super.toMap()
        toMap["name"] = searchKeyword ?: ""
        return toMap
    }
}