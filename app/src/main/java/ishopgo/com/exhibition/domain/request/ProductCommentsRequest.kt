package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class ProductCommentsRequest : Request() {

    var productId: Long = -1L
    var lastId: Long = -1L
    var parentId: Long = -1L
    var limit: Int = 0

}