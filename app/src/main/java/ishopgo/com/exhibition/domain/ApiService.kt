package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class ApiService {

    interface NoAuth {

        @GET("categories")
        fun getCategories(): Single<BaseResponse<List<Category>>>

        @GET("get-banners")
        fun getBanners(): Single<BaseResponse<List<Banner>>>

        @GET("highlight-brands")
        fun getHighlightBrands(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Brand>>>

        @GET("highlight-products")
        fun getHighlightProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("brand-products/{brand-id}")
        fun getBrandProducts(@Path("brand-id") brandId: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>
      
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
    }

    interface Auth {

        @POST("refresh-token")
        fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>>

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

        @GET("expo/get-otp")
        fun getOTP(
            @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<BaseDataResponse>>

        @POST("expo/change-password")
        fun changePassword(
            @Body body: RequestBody
        ): Single<BaseResponse<BaseDataResponse>>
    }

}