package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.domain.response.RefreshTokenResponse
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockAuthService(behavior: BehaviorDelegate<ApiService.Auth>) : ApiService.Auth {

    private var delegate: BehaviorDelegate<ApiService.Auth> = behavior
    private val random = Random()

    override fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}