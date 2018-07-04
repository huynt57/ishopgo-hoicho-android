package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.Booth
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.model.community.ManagerCommunity
import ishopgo.com.exhibition.model.member.ManageMember
import ishopgo.com.exhibition.model.post.BoothPostManager
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostContent
import ishopgo.com.exhibition.model.post.PostsManager
import ishopgo.com.exhibition.model.postmenu.PostMenuManager
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.model.question.QuestionCategory
import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.model.question.QuestionManager
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.model.search_sale_point.ManagerSearchSalePoint
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.model.survey.CheckSurvey
import ishopgo.com.exhibition.model.survey.Survey
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class ApiService {

    interface NoAuth {

        @GET("categories")
        fun getCategories(): Single<BaseResponse<List<Category>>>

        @GET("sub-categories")
        fun getSubCategories(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Category>>>

        @GET("booth/get-category/{id}")
        fun getBoothCategories(@Path("id") boothId: Long): Single<BaseResponse<List<Category>>>

        @GET("get-banners")
        fun getBanners(): Single<BaseResponse<List<Banner>>>

        @GET("highlight-brands")
        fun getHighlightBrands(): Single<BaseResponse<List<Brand>>>

        @GET("highlight-products")
        fun getHighlightProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("suggested-products")
        fun getSuggestedProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("get-product-category")
        fun getCategoriedProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("viewed-products")
        fun getViewedProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("brand-products/{brand-id}")
        fun getBrandProducts(@Path("brand-id") brandId: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<SearchProducts>>

        @GET("search-product")
        fun searchProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<ManagerProduct>>

        @GET("search-booth")
        fun searchShops(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<ManagerShop>>

        @GET("product/{id}")
        fun getProductDetail(@Path("id") id: Long): Single<BaseResponse<ProductDetail>>

        @GET("relate-products")
        fun getRelateProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("newest-products")
        fun getNewestProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<NewestProducts>>

        @GET("promotion-products")
        fun getPromotionProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

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
        ): Single<BaseResponse<ManagerCommunity>>


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
        fun getShopInfo(@Path("id") id: Long): Single<BaseResponse<ManagerShopDetail>>

        @GET("search-sale-point")
        fun searchSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerSearchSalePoint>>

        @GET("booth/detail-sale-point")
        fun getSalePointDetail(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerSalePointDetail>>

        @GET("general-info/get-posts")
        fun getPost(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<PostMenuManager>>

        @GET("general-info/get-posts/{id}")
        fun getPostDetail(@Path("id") post_id: Long): Single<BaseResponse<PostContent>>

        @GET("general-info/get-categories")
        fun getPostCategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<PostCategory>>>

        @GET("question/get-posts")
        fun getQuestion(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<QuestionManager>>

        @GET("question/get-posts/{id}")
        fun getQuestionDetail(@Path("id") question_id: Long): Single<BaseResponse<QuestionDetail>>

        @GET("question/get-categories")
        fun getQuestionCategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<QuestionCategory>>>

        @GET("fair")
        fun getExpos(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<ExpoConfig>>>

        @GET("fair/detail/{id}")
        fun getExpoDetail(@Path("id") fairId: Long): Single<BaseResponse<ExpoConfig>>

        @GET("map/list/{id}")
        fun getExpoShopLocations(@Path("id") expoId: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Kiosk>>>

        @GET("introduction")
        fun getIntroduction(): Single<BaseResponse<Introduction>>

        @GET("general-info/get-booth-posts")
        fun getBoothPost(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<BoothPostManager>>
    }

    interface Auth {

        @GET("community")
        fun getCommunity(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<ManagerCommunity>>

        @GET("shop/{id}")
        fun getShopInfo(@Path("id") id: Long): Single<BaseResponse<ManagerShopDetail>>

        @GET("product/{id}")
        fun getProductDetail(@Path("id") id: Long): Single<BaseResponse<ProductDetail>>

        @GET("members/{id}")
        fun getMemberDetail(
                @Path("id") memberId: Long
        ): Single<BaseResponse<Member>>

        @GET("favorite-products")
        fun getFavoriteProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

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

        @GET("members")
        fun getMember(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>>

        @GET("members")
        fun getDeletedMember(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>>

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

        @GET("booth/get-sale-point")
        fun getSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<SalePoint>>>

        @POST("booth/add-sale-point")
        fun createSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("sale-point/change-status/{id}")
        fun changeStatusSalePoint(@Path("id") salePoint_id: Long): Single<BaseResponse<Any>>

        @GET("booths")
        fun getBooth(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<BoothManager>>>

        @POST("booth/add")
        fun createBooth(@Body body: RequestBody): Single<BaseResponse<Any>>

        @DELETE("booth/{id}")
        fun deleteBooth(@Path("id") booth_id: Long): Single<BaseResponse<Any>>

        @POST("booth/add-from-member/{id}")
        fun registerBooth(@Path("id") account_id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("follow")
        fun postProductPollow(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ProductFollow>>

        @GET("get-sale-point/{id}")
        fun getProductSalePoint(@Path("id") product_id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<ProductSalePoint>>>

        @POST("add-sale-point")
        fun createProductSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("shop/{id}/rate")
        fun createRatingShop(@Path("id") product_id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @GET("survey/check-survey")
        fun checkSurvey(): Single<BaseResponse<CheckSurvey>>

        @GET("survey")
        fun getSurvey(): Single<BaseResponse<Survey>>

        @POST("survey/add-survey-account/{id}")
        fun postSurvey(@Path("id") id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("ticket/add")
        @FormUrlEncoded
        fun createTicket(@FieldMap fields: MutableMap<String, Any>): Single<BaseResponse<Ticket>>

        @GET("ticket/account")
        fun getMyTicket(): Single<BaseResponse<List<Ticket>>>

        @GET("detail-sale-point")
        fun getInfoMemberSalePoint(@Query("phone") phone: String
        ): Single<BaseResponse<SearchSalePoint>>

        @GET("follow-product")
        fun getProductFollow(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("list-follow-booth")
        fun getBoothFollow(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<BoothFollow>>>

        @GET("ticket")
        fun getListTicket(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Ticket>>>

        @GET("customer-visit")
        fun getVisitors(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageVisitor>>

        @POST("fair/add")
        fun addExpo(@Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("fair/update/{id}")
        fun editExpo(@Path("id") id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("fair/setup-map/{id}")
        fun settingExpo(@Path("id") expoId: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @DELETE("fair/delete/{id}")
        fun deleteExpo(@Path("id") expoId: Long): Single<BaseResponse<Any>>

        @POST("map/choose-booth/{id}")
        @FormUrlEncoded
        fun assignBooth(@Path("id") position: Long, @FieldMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @GET("product/{id}")
        fun getProductManagerDetail(@Path("id") id: Long): Single<BaseResponse<ProductManagerDetail>>
    }

    interface ISGApi {

        @POST("refresh-token")
        fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>>

        @GET("notifications")
        fun getNotifications(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Notification>>>

        @GET("notifications/{id}")
        fun getNotificationDetail(@Path("id") id: Long): Single<BaseResponse<Notification>>

        @POST("read-notifications")
        fun readNotification(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @GET("get-products")
        fun getProductManager(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageProduct>>

        @GET("get-cities")
        fun getRegions(): Single<BaseResponse<MutableList<Region>>>

        @GET("get-districts")
        fun getDistricts(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<District>>>

        @GET("providers")
        fun getProviders(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<Provider>>>

        @GET("brands")
        fun getBrands(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<Brand>>>

        @POST("add-product")
        fun createProductManager(@Body body: RequestBody): Single<BaseResponse<Any>>

        @GET("product")
        fun getProductManagerDetail(@Query("product_id") productID: Long): Single<BaseResponse<ProductManagerDetail>>

        @POST("restore-members/{id}")
        fun restoreMembers(@Path("id") member_id: Long): Single<BaseResponse<Any>>

        @DELETE("delete-members/{id}")
        fun deleteMember(@Path("id") member_id: Long): Single<BaseResponse<Any>>

        @GET("brands")
        fun getBrand(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Brand>>>

        @POST("update-brands/{id}")
        fun updateBrand(@Path("id") brand_id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("create-brands")
        fun createBrand(@Body body: RequestBody): Single<BaseResponse<Any>>

        @DELETE("delete-brands/{id}")
        fun deleteBrand(@Path("id") brand_id: Long): Single<BaseResponse<Any>>

        @GET("general-info/get-posts")
        fun getPost(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<PostsManager>>

        @GET("general-info/get-posts/{id}")
        fun getPostDetail(@Path("id") post_id: Long): Single<BaseResponse<PostContent>>

        @GET("general-info/get-categories")
        fun getPostCategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<PostCategory>>>

        @POST("general-info/create-categories")
        fun createPostGategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @GET("question/get-posts")
        fun getQuestion(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<QuestionManager>>

        @GET("question/get-posts/{id}")
        fun getQuestionDetail(@Path("id") question_id: Long): Single<BaseResponse<QuestionDetail>>

        @GET("question/get-categories")
        fun getQuestionCategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<QuestionCategory>>>

        @FormUrlEncoded
        @POST("chat/update-sample-messages")
        fun chat_updateSampleMessages(
                @FieldMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<TextPattern>>

        @DELETE("chat/delete-sample-messages/{id}")
        fun chat_removeSampleMessage(
                @Path("id") id: Long
        ): Single<BaseResponse<Any>>

        @POST("chat/send-chat")
        fun chat_sendChat(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("chat/get-inbox")
        fun inbox_getConversations(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<LocalConversationItem>>>

        @GET("chat/get-message")
        fun inbox_getMessages(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<LocalMessageItem>>>

        @GET("chat/get-info-group")
        fun chat_conversationInfo(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ConversationInfo>>

        @GET("chat/count-unread-inbox")
        fun chatUnreadInbox(): Single<BaseResponse<Int>>

        @GET("chat/get-sample-messages")
        fun inbox_getPatterns(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<TextPattern>>>

        @GET("chat/get-image-warehouse")
        fun chat_getImageInventory(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<ImageInventory>>>

        @POST("chat/update-info-group")
        @FormUrlEncoded
        fun chat_updateInfoGroup(
                @FieldMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<ConversationInfo>>

        @GET("chat/get-contact")
        fun inbox_getContact(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<ContactItem>>>

        @FormUrlEncoded
        @POST("chat/add-member-group")
        fun inbox_addMemberGroup(
                @FieldMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<Any>>

        @FormUrlEncoded
        @POST("chat/create-new-chat")
        fun inbox_createNewChat(
                @FieldMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<NewConversation>>

        @GET("find-info")
        fun getUserByPhone(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<PhoneInfo>>

        @GET("count-notification")
        fun getNotificationCount(): Single<BaseResponse<Int>>

        @POST("edit-product/{id}")
        fun editProduct(@Path("id") brand_id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>
    }

}