package ishopgo.com.exhibition.domain

import com.google.gson.JsonObject
import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.model.administrator.AdministratorPermissions
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.model.community.ManagerCommunity
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.model.member.ManageMember
import ishopgo.com.exhibition.model.post.BoothPostManager
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostContent
import ishopgo.com.exhibition.model.post.PostsManager
import ishopgo.com.exhibition.model.postmenu.PostMenuManager
import ishopgo.com.exhibition.model.product_manager.ManageProduct
import ishopgo.com.exhibition.model.question.QuestionCategory
import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.model.question.QuestionManager
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.model.search_sale_point.ManagerSearchSalePoint
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.model.survey.CheckSurvey
import ishopgo.com.exhibition.model.survey.Survey
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class ApiService {

    interface NoAuth {

        @POST("expo/login-fb")
        fun loginFacebook(@Body body: RequestBody): Single<BaseResponse<LoginFacebook>>

        @GET("expo/all-brands")
        fun getAllBrands(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerBrand>>

        @GET("expo/categories")
        fun getCategories(): Single<BaseResponse<List<Category>>>

        @GET("expo/booth/get-type-booth")
        fun getBoothTypes(): Single<BaseResponse<List<String>>>

        @GET("expo/sub-categories")
        fun getSubCategories(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Category>>>

        @GET("expo/booth/get-category/{id}")
        fun getBoothCategories(@Path("id") boothId: Long): Single<BaseResponse<List<Category>>>

        @GET("expo/get-banners")
        fun getBanners(): Single<BaseResponse<List<Banner>>>

        @GET("expo/highlight-brands")
        fun getHighlightBrands(): Single<BaseResponse<List<Brand>>>

        @GET("expo/highlight-products")
        fun getHighlightProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/suggested-products")
        fun getSuggestedProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/get-product-category")
        fun getCategoriedProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/viewed-products")
        fun getViewedProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/brand-products/{brand-id}")
        fun getBrandProducts(@Path("brand-id") brandId: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<SearchProducts>>

        @GET("expo/search-product")
        fun searchProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<ManagerProduct>>

        @GET("expo/search-booth")
        fun searchShops(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<ManagerShop>>

        @GET("expo/product/{id}")
        fun getProductDetail(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<ProductDetail>>

        @GET("expo/relate-products")
        fun getRelateProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/newest-products")
        fun getNewestProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<NewestProducts>>

        @GET("expo/promotion-products")
        fun getPromotionProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/get-product-booth/{id}")
        fun getBoothProducts(@Path("id") boothId: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/comment-product/{id}")
        fun getProductComments(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<ProductComment>>>

        @GET("expo/shop/{id}/rates")
        fun getShopRatings(@Path("id") id: Long, @QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<ShopRate>>>

        @GET("expo/get-relate-shop/{id}")
        fun getShopRelate(@Path("id") id: Long): Single<BaseResponse<List<BoothManager>>>

        @FormUrlEncoded
        @POST("expo/login")
        fun login(
                @Field("phone") phone: String,
                @Field("password") password: String,
                @Field("id_app") domain: String,
                @Field("device_token_android") token: String
        ): Single<BaseResponse<User>>

        @POST("expo/register")
        fun register(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("expo/community")
        fun getCommunity(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<ManagerCommunity>>


        @GET("expo/community/list-comment/{id}")
        fun getCommentCommunity(
                @Path("id") post_id: Long,
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<MutableList<CommunityComment>>>

        @GET("expo/get-otp")
        fun getOTP(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<Any>>

        @GET("expo/shop/{id}")
        fun getShopInfo(@Path("id") id: Long): Single<BaseResponse<ManagerShopDetail>>

        @GET("expo/search-sale-point")
        fun searchSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerSearchSalePoint>>

        @GET("expo/booth/detail-sale-point")
        fun getSalePointDetail(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerSalePointDetail>>

//        @GET("boothexpo//list-sale-point/{id}")
//        fun getBoothSalePoints(@Path("id") boothId: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<SalePoint>>>

        @GET("expo/general-info/get-posts")
        fun getPost(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<PostMenuManager>>

        @GET("expo/general-info/get-posts/{id}")
        fun getPostDetail(@Path("id") post_id: Long): Single<BaseResponse<PostContent>>

        @GET("expo/general-info/get-categories")
        fun getPostCategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<PostCategory>>>

        @GET("expo/question/get-posts")
        fun getQuestion(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<QuestionManager>>

        @GET("expo/question/get-posts/{id}")
        fun getQuestionDetail(@Path("id") question_id: Long): Single<BaseResponse<QuestionDetail>>

        @GET("expo/question/get-categories")
        fun getQuestionCategory(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<QuestionCategory>>>

        @GET("expo/fair")
        fun getExpos(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<ExpoConfig>>>

        @GET("expo/fair/detail/{id}")
        fun getExpoDetail(@Path("id") fairId: Long): Single<BaseResponse<ExpoConfig>>

        @GET("expo/map/list/{id}")
        fun getExpoShopLocations(@Path("id") expoId: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Kiosk>>>

        @GET("expo/introduction")
        fun getIntroduction(): Single<BaseResponse<Introduction>>

        @GET("expo/general-info/get-booth-posts")
        fun getBoothPost(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<BoothPostManager>>

        @GET("agri/get-product-diary")
        fun getProductDiary(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<DiaryProduct>>>

        @GET
        fun getStampLinkScan(@Url url: String): Single<BaseResponse<String>>

        @GET("expo/get-config-footer")
        fun getConfigFooter(): Single<BaseResponse<String>>
    }

    interface Auth {

        @GET("expo/friend/list")
        fun getFriends(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<List<ContactItem>>>

        @GET("expo/community")
        fun getCommunity(
                @QueryMap fields: MutableMap<String, Any>
        ): Single<BaseResponse<ManagerCommunity>>

        @GET("expo/shop/{id}")
        fun getShopInfo(@Path("id") id: Long): Single<BaseResponse<ManagerShopDetail>>

        @GET("expo/product/{id}")
        fun getProductDetail(@Path("id") id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ProductDetail>>

        @GET("expo/members/{id}")
        fun getMemberDetail(
                @Path("id") memberId: Long
        ): Single<BaseResponse<Member>>

        @GET("expo/favorite-products")
        fun getFavoriteProducts(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @POST("expo/change-password")
        fun changePassword(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @FormUrlEncoded
        @POST("expo/products/push-top")
        fun pushTop(@FieldMap params: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("expo/refresh-token")
        fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>>

        @GET("expo/logout")
        fun logout(
                @Query("type") osType: String
        ): Single<BaseResponse<Any>>

        @GET("expo/members")
        fun getMember(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>>

        @GET("expo/members")
        fun getDeletedMember(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>>

        @GET("expo/profile")
        fun getProfile(
                @Query("account_id") account_id: Long
        ): Single<BaseResponse<Profile>>

        @POST("expo/profile")
        fun updateProfile(
                @Body body: RequestBody
        ): Single<BaseResponse<Profile>>

        @POST("expo/community/create-post")
        fun sentPostCommunity(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @POST("expo/like/product/{id}")
        fun postProductLike(
                @Path("id") product_id: Long
        ): Single<BaseResponse<Any>>

        @GET("expo/like/product/{id}")
        fun getProductLike(
                @Path("id") product_id: Long
        ): Single<BaseResponse<ProductLike>>

        @POST("expo/community/like-post/{post-id}")
        fun postCommunityLike(
                @Path("post-id") post_id: Long
        ): Single<BaseResponse<Any>>

        @POST("expo/community/comment-post/{id}")
        fun postCommentCommunity(
                @Path("id") post_id: Long,
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @POST("expo/share-product/{id}")
        fun postProductShare(
                @Path("id") id: Long
        ): Single<BaseResponse<Any>>

        @POST("expo/comment-product/{id}")
        fun postCommentProduct(
                @Path("id") product_id: Long,
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("expo/config")
        fun getConfigBooth(): Single<BaseResponse<BoothConfig>>

        @POST("expo/config/update")
        fun editConfigBooth(
                @Body body: RequestBody
        ): Single<BaseResponse<Any>>

        @GET("expo/booth/get-sale-point")
        fun getSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<SalePoint>>>

        @POST("expo/booth/add-sale-point")
        fun createSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("expo/sale-point/change-status/{id}")
        fun changeStatusSalePoint(@Path("id") salePoint_id: Long): Single<BaseResponse<Any>>

        @GET("expo/booths")
        fun getBooth(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerBooth>>

        @POST("expo/booth/add")
        fun createBooth(@Body body: RequestBody): Single<BaseResponse<Any>>

        @DELETE("expo/booth/{id}")
        fun deleteBooth(@Path("id") booth_id: Long): Single<BaseResponse<Any>>

        @POST("expo/booth/add-from-member/{id}")
        fun registerBooth(@Path("id") account_id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("expo/follow")
        fun postProductPollow(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ProductFollow>>

        @GET("expo/get-sale-point/{id}")
        fun getProductSalePoint(@Path("id") product_id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<ProductSalePoint>>>

        @POST("expo/add-sale-point")
        fun createProductSalePoint(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("expo/shop/{id}/rate")
        fun createRatingShop(@Path("id") product_id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @GET("expo/survey/check-survey")
        fun checkSurvey(): Single<BaseResponse<CheckSurvey>>

        @GET("expo/survey")
        fun getSurvey(): Single<BaseResponse<Survey>>

        @POST("expo/survey/add-survey-account/{id}")
        fun postSurvey(@Path("id") id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @FormUrlEncoded
        @POST("expo/ticket/add")
        fun createTicket(@FieldMap fields: MutableMap<String, Any>): Single<BaseResponse<Ticket>>

        @GET("expo/ticket/account")
        fun getMyTicket(): Single<BaseResponse<List<Ticket>>>

        @GET("expo/detail-sale-point")
        fun getInfoMemberSalePoint(@Query("phone") phone: String
        ): Single<BaseResponse<SearchSalePoint>>

        @GET("expo/follow-product")
        fun getProductFollow(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<Product>>>

        @GET("expo/list-follow-booth")
        fun getBoothFollow(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<BoothFollow>>>

        @GET("expo/ticket")
        fun getListTicket(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerTicket>>

        @GET("expo/customer-visit")
        fun getVisitors(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageVisitor>>

        @POST("expo/fair/add")
        fun addExpo(@Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("expo/fair/update/{id}")
        fun editExpo(@Path("id") id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("expo/fair/setup-map/{id}")
        fun settingExpo(@Path("id") expoId: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @DELETE("expo/fair/delete/{id}")
        fun deleteExpo(@Path("id") expoId: Long): Single<BaseResponse<Any>>

        @FormUrlEncoded
        @POST("expo/map/choose-booth/{id}")
        fun assignBooth(@Path("id") position: Long, @FieldMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @GET("expo/product/{id}")
        fun getProductManagerDetail(@Path("id") id: Long): Single<BaseResponse<ProductDetail>>

        @GET("expo/list-administrators")
        fun getAdministrator(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<Administrator>>>

        @DELETE("expo/delete-administrators/{id}")
        fun deleteAdministrator(@Path("id") id: Long): Single<BaseResponse<Any>>

        @GET("expo/account-permissions")
        fun getAdministratorPermissions(): Single<BaseResponse<MutableList<AdministratorPermissions>>>

        @GET("expo/booth-permissions")
        fun getBoothPermissions(): Single<BaseResponse<MutableList<AdministratorPermissions>>>

        @GET("expo/search-members")
        fun getMemberPermissions(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>>

        @POST("expo/add-administrators")
        fun addAdministrator(@Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("expo/edit-administrators/{id}")
        fun editAdministrator(@Path("id") id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @GET("expo/get-permissions")
        fun getAccountPermissions(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<String>>>

        @POST("agri/add-product-diary")
        fun createProductDiary(@Body body: RequestBody): Single<BaseResponse<Any>>

        @DELETE("agri/delete-product-diary/{id}")
        fun deleteProductDiary(@Path("id") id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>>

        @POST("expo/remove-sale-point")
        fun deleteProductInSalePoint(@Body body: RequestBody): Single<BaseResponse<Any>>

        @GET("expo/stamp")
        fun loadStampManager(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<StampManager>>>

        @GET("expo/stamp/list")
        fun loadNoStamp(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<StampNoList>>>

        @GET("expo/stamp/{id}/edit")
        fun getNoStampDetail(@Path("id") id: Long): Single<BaseResponse<StampNoDetail>>

        @GET("expo/stamp/create")
        fun getNoStampCreated(): Single<BaseResponse<StampNoDetail>>

        @POST("expo/stamp/{id}/update")
        fun editNoStampDetail(@Path("id") id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("expo/stamp")
        fun createNoStamp(@Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("expo/stamp/{id}/assign")
        fun saveStampAssign(@Path("id") id: Long, @Body body: RequestBody): Single<BaseResponse<Any>>

        @GET("expo/stamp/assign")
        fun getStampDistribution(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<StampDistribution>>>

        @GET("expo/stamp/{id}/assign")
        fun loadProductAssignNoStamp(@Path("id") id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<StampSearchProduct>>

        @GET("expo/stamp/scan")
        fun loadListScanStamp(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<StampUserListScan>>>

        @GET("expo/stamp/warning")
        fun loadStampWarning(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<StampListWarning>>>

        @POST("expo/stamp/warning")
        fun evictionStampWarning(@Body body: RequestBody): Single<BaseResponse<Any>>

        @PUT("expo/stamp/warning/{code}")
        fun restoreStampWarning(@Path("code") code: String, @Body body: RequestBody): Single<BaseResponse<Any>>

        @GET("expo/stamp/orders")
        fun getStampOrders(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<StampListBuy>>>

        @GET("expo/stamp/orders/statistical")
        fun getStampOrderStatistical(): Single<BaseResponse<StampOrdersStatistical>>

        @GET("expo/get-list-relate-shops/{id}")
        fun getBoothRelated(@Path("id") id: Long): Single<BaseResponse<List<BoothManager>>>

        @GET("expo/get-images-from-config")
        fun getImagesForConfig(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<List<CertImages>>>
    }


    interface IcheckApi {
        @POST("contribute")
        fun updateIcheckProduct(@Body body: JsonObject): Single<IcheckRep<Any>>

        @POST("local")
        fun createSalePoint(@Body body: JsonObject): Single<IcheckRep<Any>>
    }

    interface ISGApi {
        //    "http://sandbox.icheck.com.vn/scan/8934677020110"
        @GET
        fun getIcheckProduct(@Url url: String): Single<IcheckRep<IcheckProduct>>

        //    "http://sandbox.icheck.com.vn//products/8934677020110/informations/25221"
        @GET
        fun getIcheckProductDetail(@Url url: String): Single<IcheckRep<IcheckProductDetail>>

        // "http://geteway.icheck.com.vn/app/locations/4548623236315?geo=21.735235,121.850293&page=1"
        @GET
        fun getIcheckSalePoint(@Url url: String): Single<IcheckRep<IcheckSalePointManager>>

        @GET
        fun getIcheckDetailSalePoint(@Url url: String): Single<IcheckRep<IcheckSalePointDetailManager>>

        @GET
        fun getIcheckProductSalePoint(@Url url: String): Single<IcheckRep<IcheckProductManager>>

        @GET
        fun getIcheckProductRelated(@Url url: String): Single<IcheckRep<List<IcheckProduct>>>

        @GET
        fun getIcheckProductOnShop(@Url url: String): Single<IcheckRep<List<IcheckProduct>>>

        @GET
        fun getIcheckReview(@Url url: String): Single<IcheckRep<List<IcheckReview>>>

        @GET
        fun getIcheckShopInfo(@Url url: String): Single<IcheckRep<IcheckShopDetail>>

        @GET
        fun loadIcheckShopProduct(@Url url: String): Single<IcheckRep<List<IcheckProduct>>>

        @GET
        fun loadIcheckShopCategory(@Url url: String): Single<IcheckRep<IcheckCategoryManager>>

        @GET
        fun loadIcheckShopCategoryProduct(@Url url: String): Single<IcheckRep<List<IcheckProduct>>>

        @GET
        fun loadIcheckShopCity(@Url url: String): Single<IcheckRepAffiliate<IcheckCity>>

        @GET
        fun loadIcheckShopDistrict(@Url url: String): Single<IcheckRepAffiliate<IcheckDistrict>>

        @GET
        fun loadIcheckCategory(@Url url: String): Single<IcheckRep<List<IcheckCategory>>>

        @GET("scan-barcode")
        fun scanBarcode(
                @Query("barcode") barcode: String
        ): Single<BaseResponse<Product>>

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
        fun getBrands(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerBrand>>

        @POST("add-product")
        fun createProductManager(@Body body: RequestBody): Single<BaseResponse<Any>>

        @POST("restore-members/{id}")
        fun restoreMembers(@Path("id") member_id: Long): Single<BaseResponse<Any>>

        @DELETE("delete-members/{id}")
        fun deleteMember(@Path("id") member_id: Long): Single<BaseResponse<Any>>

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

        @FormUrlEncoded
        @POST("chat/update-info-group")
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