package ishopgo.com.exhibition.domain.response

import ishopgo.com.exhibition.domain.request.LoadMoreRequest

class FilterProductRequest : LoadMoreRequest() {
    var sort_by: String = ""

    var sort_type: String = ""

    var type_filter: MutableList<Int> = mutableListOf()
}