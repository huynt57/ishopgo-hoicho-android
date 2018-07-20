package ishopgo.com.exhibition.domain.request

class SearchMemberAdministratorRequest : LoadMoreRequest() {
    var name: String = ""
    var boothId: Long = -1L
}