package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.BaseDataResponse
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.model.LoginResponse
import ishopgo.com.exhibition.model.Profile
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
interface ApiService{
    @FormUrlEncoded
    @POST("expo/login")
    fun login(
            @Field("phone") phone: String,
            @Field("password") password: String,
            @Field("id_app") domain: String,
            @Field("device_token_android") token: String
    ): Single<BaseResponse<LoginResponse>>

    @POST("expo/register")
    fun register(
            @Body body: RequestBody
    ): Single<BaseResponse<BaseDataResponse>>

    @GET("expo/get-otp")
    fun getOTP(
            @QueryMap fields: MutableMap<String, Any>
    ): Single<BaseResponse<BaseDataResponse>>

    @POST("expo/change-password")
    fun changePassword(
            @Body body: RequestBody
    ): Single<BaseResponse<BaseDataResponse>>

    @POST("expo/refresh-token")
    fun refreshToken(): Single<BaseResponse<LoginResponse>>


    @GET("expo/logout")
    fun logout(
            @Query("type") osType: String
    ): Single<BaseResponse<BaseDataResponse>>

    @GET("expo/profile")
    fun getProfile(
            @Query("account_id") account_id: Long
    ): Single<BaseResponse<Profile>>

    @POST("expo/profile")
    fun updateProfile(
            @Body body: RequestBody
    ): Single<BaseResponse<Profile>>
}
