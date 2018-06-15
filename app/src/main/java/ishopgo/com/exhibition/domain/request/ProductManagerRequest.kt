package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class ProductManagerRequest : Request() {

    var limit: Int = 0

    var offset: Int = 0

    var name: String = ""

    var code: String = ""

    var productId: Long = -1L

}