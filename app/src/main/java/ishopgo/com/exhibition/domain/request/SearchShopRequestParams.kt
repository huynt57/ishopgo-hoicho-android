package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchShopRequestParams : LoadMoreRequestParams() {

    var keyword: String = ""

    override fun toString(): String {
        return "keyword = $keyword, limit = $limit, offset = $offset"
    }
}