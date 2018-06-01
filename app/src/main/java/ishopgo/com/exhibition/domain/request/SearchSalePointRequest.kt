package ishopgo.com.exhibition.domain.request

class SearchSalePointRequest : LoadMoreRequest() {

    var keyword: String = ""

    override fun toString(): String {
        return "keyword = $keyword, limit = $limit, offset = $offset"
    }
}