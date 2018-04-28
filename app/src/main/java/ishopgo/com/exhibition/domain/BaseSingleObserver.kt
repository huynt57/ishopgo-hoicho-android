package ishopgo.com.exhibition.domain

import io.reactivex.observers.DisposableSingleObserver
import ishopgo.com.exhibition.domain.response.BaseResponse
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by xuanhong on 1/6/18. HappyCoding!
 */
open abstract class BaseSingleObserver<T> : DisposableSingleObserver<BaseResponse<T>>(), IResponse<T> {

    override fun onError(e: Throwable?) {
        when (e) {
            is HttpException -> {
                when (e.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        failure(BaseErrorSignal.ERROR_401, e.message())
                    }
                    else -> {
                        failure(BaseErrorSignal.ERROR_SERVER, e.message())
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

    override fun onSuccess(t: BaseResponse<T>?) {
        t?.let {
            // Has converted body
            when {
                it.status > BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                    // Success in business logic
                    success(it.data)
                }
                it.status == BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                    // Fail in business logic
                    failure(BaseErrorSignal.ERROR_HAS_MESSAGE, it.message ?: "")
                }
                else -> {
                    failure(BaseErrorSignal.ERROR_UNKNOWN, it.message ?: "")
                }
            }
        }
    }

}