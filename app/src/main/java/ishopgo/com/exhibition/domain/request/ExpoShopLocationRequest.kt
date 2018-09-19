package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoShopLocationRequest : LoadMoreRequest() {

    var expoId: Long = -1L
    var searchKeyword: String? = null
    var typeFilter: Int? = 0

    override fun toMap(): MutableMap<String, Any> {
        val fields = super.toMap()
        fields["name"] = searchKeyword ?: ""
        if (typeFilter != 0)
            fields["type_filter"] = typeFilter ?: 0
        return fields
    }
}