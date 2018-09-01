package ishopgo.com.exhibition.domain.auth

import android.app.Application
import android.util.Log
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.RefreshTokenResponse
import ishopgo.com.exhibition.model.UserDataManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class AppAuthenticator(app: Application) : Authenticator {

    companion object {
        private const val TAG = "AppAuthenticator"
    }

    lateinit var authService: ApiService.Auth

    override fun authenticate(route: Route?, response: Response?): Request? {
        Log.d(TAG, "authen failed")

        response?.let {
            if (responseCount(response) >= 2) {
                return null
            }
        }

        var obtained = false

        val subscribeWith: BaseSingleObserver<RefreshTokenResponse> = authService.refreshToken()
                .subscribeWith(object : BaseSingleObserver<RefreshTokenResponse>() {
                    override fun success(data: RefreshTokenResponse?) {
                        data?.let {
                            obtained = true
                            UserDataManager.accessToken = it.newToken
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        Log.d(TAG, "onError() called $message")
                        if (UserDataManager.currentUserId > 0)
                            UserDataManager.deleteUserInfo()
                        obtained = false
                    }
                })

        subscribeWith.dispose()

        return if (obtained) {
            response?.request()?.newBuilder()?.header("Authorization", "Bearer " + UserDataManager.accessToken)?.build()
        } else
            null
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var failed:  Response? = response
        while (failed?.priorResponse() != null) {
            result++
            failed = failed.priorResponse()
        }

        return result
    }

}