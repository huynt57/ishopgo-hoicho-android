package ishopgo.com.exhibition.domain.request

class ListBGBNRequest : LoadMoreRequest() {
    var product_id: Long = -1L
    var booth_id: Long = -1L
    var keyword: String = ""
}