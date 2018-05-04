package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.domain.response.RefreshTokenResponse
import ishopgo.com.exhibition.model.Community.CommunityComment
import ishopgo.com.exhibition.model.ProductLike
import ishopgo.com.exhibition.model.Profile
import okhttp3.RequestBody
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockAuthService(behavior: BehaviorDelegate<ApiService.Auth>) : ApiService.Auth {
    override fun getCommentCommunity(post_id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<CommunityComment>>> {
        val ps = mutableListOf<CommunityComment>()
        for (i in 0..5)
            ps.add(generateCommunityComment(i))

        val response = BaseResponse<CommunityComment>()
        response.status = 1
        return delegate.returningResponse(response).getCommentCommunity(post_id, fields)
    }

    override fun postCommunityLike(post_id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postCommunityLike(post_id)
    }

    override fun postCommentCommunity(post_id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postCommentCommunity(post_id, body)
    }

    override fun postProductLike(product_id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postProductLike(product_id)
    }

    override fun getProductLike(product_id: Long): Single<BaseResponse<ProductLike>> {
        val c = ProductLike()
        c.status = 0

        val response = BaseResponse<ProductLike>()
        response.status = 1
        response.data = c
        return delegate.returningResponse(response).getProductLike(product_id)
    }

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

    private fun generateCommunityComment(i: Int): CommunityComment {
        val c = CommunityComment()
        c.id = 707
        c.content = "rất hay"
        c.accountName = "Nguyễn Huy Hoàng"
        c.accountImage = "https://static.ishopgo.com/17288/a95b6071b5578a862d1890de0e96c11favatarjpg.jpg"
        c.postId = 934
        c.status = 2

        if (i % 2 == 0) {
            val b = mutableListOf<String>()
            b.add("https://static.ishopgo.com/17288/fe0eb451a22ca727e6b2bd91baf8f3b4ozed-4e29d62a55f4dae4fc69899975657ab5wd-melasma-projpgjpg.jpg")
            c.images = b
        }

        if (i % 5 == 0) {
            val b = mutableListOf<String>()
            for (i in 0..1) {
                b.add("https://static.ishopgo.com/17288/fe0eb451a22ca727e6b2bd91baf8f3b4ozed-4e29d62a55f4dae4fc69899975657ab5wd-melasma-projpgjpg.jpg")
            }
            c.images = b
        }
        c.updatedAt = "2018/05/04 15:16:56"
        c.createdAt = "2018/05/04 15:16:56"
        c.commentCount = 0
        return c
    }

    private var delegate: BehaviorDelegate<ApiService.Auth> = behavior
    private val random = Random()

    override fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}