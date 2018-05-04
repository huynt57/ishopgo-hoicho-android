package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.domain.response.RefreshTokenResponse
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.LoginResponse
import ishopgo.com.exhibition.model.Profile
import okhttp3.RequestBody
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockAuthService(behavior: BehaviorDelegate<ApiService.Auth>) : ApiService.Auth {
    override fun login(phone: String, password: String, domain: String, token: String): Single<BaseResponse<LoginResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommunity(fields: MutableMap<String, Any>): Single<BaseResponse<List<Community>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sentPostCommunity(body: RequestBody): Single<BaseResponse<Any>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun register(body: RequestBody): Single<BaseResponse<Any>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changePassword(body: RequestBody): Single<BaseResponse<Any>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOTP(fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(osType: String): Single<BaseResponse<Any>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProfile(account_id: Long): Single<BaseResponse<Profile>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateProfile(body: RequestBody): Single<BaseResponse<Profile>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var delegate: BehaviorDelegate<ApiService.Auth> = behavior
    private val random = Random()

    override fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}