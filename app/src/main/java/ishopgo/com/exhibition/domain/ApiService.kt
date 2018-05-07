package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Booth
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.Community.CommunityComment
import ishopgo.com.exhibition.model.ProductLike
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.User
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
        fun getHighlightBrands(): Single<BaseResponse<List<Brand>>>

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

        @GET("get-product-booth/{id}")
        fun getBoothProducts(@Path("id") boothId: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("comment-product/{id}")
        fun getProductComments(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<ProductComment>>>

        @GET("shop/{id}/rates")
        fun getShopRatings(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<ShopRate>>>

        @FormUrlEncoded
        @POST("login")
        fun login(
                @Field("phone") phone: String,
                @Field("password") password: String,
                @Field("id_app") domain: String,
                @Field("device_token_android") token: String
        ): Single<BaseResponse<User>>

        @POST("register")
        fun register(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("community")
        fun getCommunity(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<Community>>>


        @GET("community/list-comment/{id}")
        fun getCommentCommunity(
                @Path("id") post_id: Long,
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<MutableList<CommunityComment>>>

        @GET("get-otp")
        fun getOTP(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<Any>>

        @GET("shop/{id}")
        fun getShopInfo(@Path("id") id: Long): Single<BaseResponse<ShopDetail>>
    }

    interface Auth {

        @POST("change-password")
        fun changePassword(
                @Body body: RequestBody
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

        @POST("like/product/{id}")
        fun postProductLike(
                @Path("id") product_id: Long
        ): Single<BaseResponse<Any>>

        @GET("like/product/{id}")
        fun getProductLike(
                @Path("id") product_id: Long
        ): Single<BaseResponse<ProductLike>>

        @POST("community/like-post/{post-id}")
        fun postCommunityLike(
                @Path("post-id") post_id: Long
        ): Single<BaseResponse<Any>>

        @POST("community/comment-post/{id}")
        fun postCommentCommunity(
                @Path("id") post_id: Long,
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @POST("share-product/{id}")
        fun postProductShare(
                @Path("id") id: Long
        ): Single<BaseResponse<Any>>

        @POST("comment-product/{id}")
        fun postCommentProduct(
                @Path("id") product_id: Long,
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("config")
        fun getConfigBooth(): Single<BaseResponse<Booth>>

        @POST("config/update")
        fun editConfigBooth(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>
    }

    interface ISGApi {

        @POST("refresh-token")
        fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>>

        @GET("notifications")
        fun getNotifications(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Notification>>>

        @POST("read-notifications")
        fun readNotification(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<Any>>
    }

}