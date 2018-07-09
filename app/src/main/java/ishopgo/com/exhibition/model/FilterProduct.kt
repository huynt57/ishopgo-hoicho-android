package ishopgo.com.exhibition.model

class FilterProduct {
    var filter: MutableList<Int> = mutableListOf()
    var sort_by: String = SORT_BY_NAME
    var sort_type: String = SORT_TYPE_ASC

    companion object {
        const val SORT_BY_NAME = "name"
        const val SORT_TYPE_ASC = "asc"
    }
}