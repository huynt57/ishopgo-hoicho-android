package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchProductRequest : LoadMoreRequest() {

    var keyword: String = ""
    var categoryId: Long = 0L
    var brandId: Long = 0L
    var boothId: Long = 0L

    override fun toString(): String {
        return "keyword = $keyword, limit = $limit, offset = $offset"
    }
}