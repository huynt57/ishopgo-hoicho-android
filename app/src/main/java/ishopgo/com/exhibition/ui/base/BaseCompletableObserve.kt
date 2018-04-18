package ishopgo.com.exhibition.ui.base

import io.reactivex.observers.DisposableCompletableObserver
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by xuanhong on 1/6/18. HappyCoding!
 */
abstract class BaseCompletableObserver : DisposableCompletableObserver(), IResponse<Any> {

    override fun onComplete() {

    }

    override fun onError(e: Throwable?) {
        when (e) {
            is HttpException -> {
                when (e.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        failure(BaseErrorSignal.ERROR_401, e.message ?: "")
                    }
                    else -> {
                        failure(BaseErrorSignal.ERROR_SERVER, e.message() ?: "")
                    }
                }
            }
            is UnknownHostException -> {
                failure(BaseErrorSignal.ERROR_NETWORK, e.message ?: "")
            }
            is SocketTimeoutException -> {
                failure(BaseErrorSignal.ERROR_NETWORK, e.message ?: "")
            }
            else -> {
                failure(BaseErrorSignal.ERROR_UNKNOWN, e?.message ?: "")
            }
        }

    }
}