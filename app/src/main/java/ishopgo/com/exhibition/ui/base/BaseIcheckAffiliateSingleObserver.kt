package ishopgo.com.exhibition.ui.base

import io.reactivex.observers.DisposableSingleObserver
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.domain.IResponse
import ishopgo.com.exhibition.domain.response.IcheckRep
import ishopgo.com.exhibition.domain.response.IcheckRepAffiliate
import ishopgo.com.exhibition.ui.extensions.showStackTrace
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open abstract class BaseIcheckAffiliateSingleObserver<T> : DisposableSingleObserver<IcheckRepAffiliate<T>>(), IResponse<T> {
    override fun onError(e: Throwable?) {
        when (e) {
            is HttpException -> {
                when (e.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        failure(BaseErrorSignal.ERROR_401, e.showStackTrace())
                    }
                    else -> {
                        failure(BaseErrorSignal.ERROR_SERVER, e.showStackTrace())
                    }
                }
            }
            is UnknownHostException -> {
                failure(BaseErrorSignal.ERROR_NETWORK, e.showStackTrace())
            }
            is SocketTimeoutException -> {
                failure(BaseErrorSignal.ERROR_NETWORK, e.showStackTrace())
            }
            else -> {
                failure(BaseErrorSignal.ERROR_UNKNOWN, e?.showStackTrace() ?: "")
            }
        }
    }

    override fun onSuccess(t: IcheckRepAffiliate<T>?) {
        t?.let {
            // Has converted body
            when {
                it.statusCode > BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                    // Success in business logic
                    success(it.data)
                }
                it.statusCode == BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                    // Fail in business logic
                    failure(BaseErrorSignal.ERROR_HAS_MESSAGE, "")
                }
                else -> {
                    failure(BaseErrorSignal.ERROR_UNKNOWN, "")
                }
            }
        }
    }

}