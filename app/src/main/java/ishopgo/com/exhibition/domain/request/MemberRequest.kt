package ishopgo.com.exhibition.domain.request

class MemberRequest : Request() {

    var limit: Int = 0

    var offset: Int = 0

    var start_time: String = ""

    var end_time: String = ""

    var phone: String = ""

    var name: String = ""

    var region: String = ""
}