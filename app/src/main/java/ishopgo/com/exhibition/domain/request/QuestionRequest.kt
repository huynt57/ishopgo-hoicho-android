package ishopgo.com.exhibition.domain.request

class QuestionRequest : Request() {
    var limit: Int = 0

    var offset: Int = 0

    var category_id: Long = -1L

    var key_search: String = ""

    var status: Int = 0
}