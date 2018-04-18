package ishopgo.com.exhibition.domain

/**
 * Created by xuanhong on 1/6/18. HappyCoding!
 */
interface IResponse<T> {
    /**
     * Will be called when the API call is totally successful in both HTTP and business logic
     *
     * @param data    Converted "data" object in the root JSON response
     */
    abstract fun success(data: T?)

    /**
     * Will be called when the API call is failed in either HTTP or business logic
     *
     * @param status  status code, UNKNOWN_ERROR for null response or connection error
     * @param message failure message:
     */
    abstract fun failure(status: Int, message: String)
}