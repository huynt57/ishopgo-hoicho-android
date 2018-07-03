package ishopgo.com.exhibition.domain.request

class BoothPostRequest : LoadMoreRequest() {
    var category_id: Long = -1L
    var booth_id: Long = -1L
    var name: String = ""
}