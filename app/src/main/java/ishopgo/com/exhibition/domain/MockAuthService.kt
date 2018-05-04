package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.domain.response.RefreshTokenResponse
import ishopgo.com.exhibition.model.Profile
import okhttp3.RequestBody
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockAuthService(behavior: BehaviorDelegate<ApiService.Auth>) : ApiService.Auth {
    override fun sentPostCommunity(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).sentPostCommunity(body)
    }

    override fun changePassword(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).changePassword(body)
    }

    override fun logout(osType: String): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).logout(osType)
    }

    override fun getProfile(account_id: Long): Single<BaseResponse<Profile>> {
        val c = Profile()
        c.id = 18398
        c.name = "Nnguyễn Huy Hoàng"
        c.phone = "0989013403"
        c.facebookUrl = ""
        c.facebookName = ""
        c.birthday = "1995/11/30 00:00:00"
        c.email = "nguyenhuyhoang16@gmail.com"
        c.company = "Isg"
        c.region = "Hà Nội"
        c.address = "Dương Đình Nghệ"
        c.createdAt = "2018/04/27 15:35:25"
        c.updatedAt = "2018/05/02 22:05:43"
        c.status = "Kích hoạt"
        c.bankNumber = ""
        c.bankName = ""
        c.bankAccountName = ""
        c.bankAddress = ""
        c.image = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"
        c.type = 5
        c.typeText = "NCC"
        c.taxCode = ""
        c.title = ""

        val response = BaseResponse<Profile>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).getProfile(account_id)
    }

    override fun updateProfile(body: RequestBody): Single<BaseResponse<Profile>> {
        val c = Profile()
        c.id = 18398
        c.name = "Nnguyễn Huy Hoàng"
        c.phone = "0989013403"
        c.facebookUrl = ""
        c.facebookName = ""
        c.birthday = "1995/11/30 00:00:00"
        c.email = "nguyenhuyhoang16@gmail.com"
        c.company = "Ishopgo"
        c.region = "Hà Nội"
        c.address = "Dương Đình Nghệ"
        c.createdAt = "2018/04/27 15:35:25"
        c.updatedAt = "2018/05/02 22:05:43"
        c.status = "Kích hoạt"
        c.bankNumber = ""
        c.bankName = ""
        c.bankAccountName = ""
        c.bankAddress = ""
        c.image = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"
        c.type = 5
        c.typeText = "NCC"
        c.taxCode = ""
        c.title = ""

        val response = BaseResponse<Profile>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).updateProfile(body)
    }

    private var delegate: BehaviorDelegate<ApiService.Auth> = behavior
    private val random = Random()

    override fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}