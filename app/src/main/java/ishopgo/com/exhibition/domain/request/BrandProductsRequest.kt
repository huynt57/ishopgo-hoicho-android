package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class BrandProductsRequest : LoadMoreRequest() {

    var brandId: Long = 0
    var name: String? = null
}