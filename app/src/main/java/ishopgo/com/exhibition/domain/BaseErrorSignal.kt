package ishopgo.com.exhibition.domain

/**
 * Created by xuanhong on 1/6/18. HappyCoding!
 */
open class BaseErrorSignal(
        var errorCode: Int = 0,
        var errorMessage: String = ""
) {

    companion object {
        const val ERROR_HAS_MESSAGE = 0
        const val ERROR_401 = 100
        const val ERROR_NETWORK = 101
        const val ERROR_SERVER = 102
        const val ERROR_UNKNOWN = 103
    }
}