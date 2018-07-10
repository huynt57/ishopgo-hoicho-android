package ishopgo.com.exhibition.domain.request

import ishopgo.com.exhibition.domain.response.FilterProductRequest

/**
 * Created by xuanhong on 5/10/18. HappyCoding!
 */
class CategoriedProductsRequest : FilterProductRequest() {

    var categoryId: Long = -1L
}