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

        @POST("refresh-token")
        fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>>

    }

}