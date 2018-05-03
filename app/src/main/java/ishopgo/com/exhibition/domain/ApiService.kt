package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.LoginResponse
import ishopgo.com.exhibition.model.Profile
import okhttp3.RequestBody
import retrofit2.http.*

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

        @GET("search-product")
        fun searchProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("search-booth")
        fun searchShops(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Shop>>>

        @GET("product/{id}")
        fun getProductDetail(@Path("id") id: Long): Single<BaseResponse<ProductDetail>>

        @GET("relate-products")
        fun getRelateProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("comment-product/{id}")
        fun getProductComments(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<ProductComment>>>

        @GET("shop_rate/{id}")
        fun getShopRatings(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<ProductComment>>>
    }

    interface Auth {

        @FormUrlEncoded
        @POST("login")
        fun login(
                @Field("phone") phone: String,
                @Field("password") password: String,
                @Field("id_app") domain: String,
                @Field("device_token_android") token: String
        ): Single<BaseResponse<LoginResponse>>

        @POST("register")
        fun register(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>


        @POST("change-password")
        fun changePassword(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>


        @GET("get-otp")
        fun getOTP(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<Any>>

        @POST("refresh-token")
        fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>>

        @GET("logout")
        fun logout(
                @Query("type") osType: String
        ): Single<BaseResponse<Any>>

        @GET("profile")
        fun getProfile(
                @Query("account_id") account_id: Long
        ): Single<BaseResponse<Profile>>

        @POST("profile")
        fun updateProfile(
                @Body body: RequestBody
        ): Single<BaseResponse<Profile>>

        @POST("community/create-post")
        fun sentPostCommunity(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("community")
        fun getCommunity(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<Community>>>
    }

}