package ishopgo.com.exhibition.domain.request

class SearchCommunityRequest : LoadMoreCommunityRequest() {

    var content: String = ""
    var account_id: Long = 0L
}