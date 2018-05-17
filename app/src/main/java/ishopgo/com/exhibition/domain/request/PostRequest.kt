package ishopgo.com.exhibition.domain.request

class PostRequest : Request() {
    var limit: Int = 0

    var offset: Int = 0

    var type: Int = 0

    var category_id: Long = -1L

    var name: String = ""
}