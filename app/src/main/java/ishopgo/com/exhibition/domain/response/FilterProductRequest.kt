package ishopgo.com.exhibition.domain.response

import ishopgo.com.exhibition.domain.request.SearchByNameRequest

open class FilterProductRequest : SearchByNameRequest() {
    var sort_by: String = SORT_BY_NAME

    var sort_type: String = SORT_TYPE_ASC

    var type_filter: MutableList<Int> = mutableListOf()

    companion object {
        const val SORT_BY_NAME = "name"
        const val SORT_TYPE_ASC = "asc"
    }
}