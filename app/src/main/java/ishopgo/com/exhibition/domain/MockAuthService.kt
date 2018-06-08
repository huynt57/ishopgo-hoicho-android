package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.Booth
import ishopgo.com.exhibition.model.member.ManageMember
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.model.survey.CheckSurvey
import ishopgo.com.exhibition.model.survey.Survey
import okhttp3.RequestBody
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockAuthService(behavior: BehaviorDelegate<ApiService.Auth>) : ApiService.Auth {
    override fun getMember(fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).getMember(fields)
    }

    override fun getDeletedMember(fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).getDeletedMember(fields)
    }

    override fun getListTicket(fields: MutableMap<String, Any>): Single<BaseResponse<List<Ticket>>> {
        val response = BaseResponse<Ticket>()
        response.status = 1

        return delegate.returningResponse(response).getListTicket(fields)
    }

    override fun getProductFollow(fields: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val response = BaseResponse<Product>()
        response.status = 1

        return delegate.returningResponse(response).getProductFollow(fields)
    }

    override fun getBoothFollow(fields: MutableMap<String, Any>): Single<BaseResponse<List<BoothFollow>>> {
        val response = BaseResponse<BoothFollow>()
        response.status = 1

        return delegate.returningResponse(response).getBoothFollow(fields)
    }

    override fun getInfoMemberSalePoint(@Query("phone") phone: String): Single<BaseResponse<SearchSalePoint>> {
        val detail = SearchSalePoint()
        detail.id = 44
        detail.name = "test điểm bán"
        detail.city = "Hà Nội"
        detail.district = "Cầu Giấy"
        detail.address = "Dương Đình Nghệ"
        detail.phone = "0989013403"
        detail.countProduct = 0
        detail.accountId = 17288

        val response = BaseResponse<SearchSalePoint>()
        response.status = 1
        response.data = detail

        return delegate.returningResponse(response).getInfoMemberSalePoint(phone)
    }

    override fun getProductDetail(id: Long): Single<BaseResponse<ProductDetail>> {
        val detail = ProductDetail()
        detail.id = 16110
        detail.name = "Balo Nữ Thời Trang Naza"
        detail.price = 590000
        detail.ttPrice = 590000
        detail.description = "<!--?xml encoding=\\\"utf-8\\\" ?-->\\r\\n<p><strong>TH&Ocirc;NG TIN SẢN PHẨM</strong></p>\\r\\n\\r\\n<p>Balo thời trang, tặng k&egrave;m gấu b&ocirc;ng<br />\\r\\n-Chất liệu: da PU cấp bền đẹp, bề mặt t&uacute;i được dập v&acirc;n tinh xảo<br />\\r\\n-D&acirc;y đeo vai c&oacute; thể điều chỉnh độ d&agrave;i ngắn<br />\\r\\n-Ngăn t&uacute;i đủ rộng để đựng c&aacute;c đồ vật cần thiết ra ngo&agrave;i: ipad,điện tho<br />\\r\\n-Kh&oacute;a k&eacute;o chống gỉ<br />\\r\\n-Chi tiết t&uacute;i được l&agrave;m tinh xảo, đường may cẩn thận<br />\\r\\n-Sử dụng nhiều mục đ&iacute;ch: đi chơi, đi học,..<br />\\r\\n-K&iacute;ch thước: d&agrave;i 29cm, cao 31cm, rộng 12cm</p>\\r\\n"
        detail.image = "https://static.ishopgo.com/17288/657de7dc98abeddf23a80b3ac3a9bbc7contactpng.png"
        val department = Department()
        department.name = "Cafe trà sữa"
        department.id = 752
        detail.department = department
        val booth = ishopgo.com.exhibition.domain.response.Booth()
        booth.id = 17365
        booth.name = "GIAN HÀNG TRƯNG BÀY"
        booth.intro = "th&ocirc;ng tin gian h&agrave;ng 232"
        booth.info = "th&ocirc;ng tin gian h&agrave;ng23"
        booth.image = "https://static.ishopgo.com/17288/99bbfb5e747949d53d0eaeb5c4f3ee92.png"
        booth.hotline = "12345"
        booth.address = null
        booth.count = 4
        detail.booth = booth
        detail.linkAffiliate = "http://hoicho.ishopgo.com/san-pham/balo-nu-thoi-trang-naza-16110"

        val response = BaseResponse<ProductDetail>()
        response.status = 1
        response.data = detail

        return delegate.returningResponse(response).getProductDetail(id)
    }

    override fun getShopInfo(id: Long): Single<BaseResponse<ManagerShopDetail>> {
        val d = ShopDetail()
        d.id = 17354
        d.name = "Gian hàng hội chợ"
        d.introduction = "Gian hàng trưng bày các sản phẩm mỹ phẩm chất lượng cao"
        d.hotline = "0917577795"
        d.info = "Thông tin thì cùng không có gì nhiều."
        d.banner = "https://static.ishopgo.com/17288/32cc917eabe0af0d905421071f4804f7bannerjpg.jpg"
        d.address = null
        d.productCount = 8
        d.rate = 4

        val ps = mutableListOf<SearchSalePoint>()
        val detail = SearchSalePoint()
        detail.id = 44
        detail.name = "test điểm bán"
        detail.city = "Hà Nội"
        detail.district = "Cầu Giấy"
        detail.address = "Dương Đình Nghệ"
        detail.phone = "0989013403"
        detail.countProduct = 0
        detail.accountId = 17288

        for (i in 0..5)
            ps.add(detail)


        val response = BaseResponse<ManagerShopDetail>()
        response.status = 1
        response.data?.booth = d
        response.data?.salePoint = ps
        return delegate.returningResponse(response).getShopInfo(id)
    }

    override fun createTicket(): Single<BaseResponse<Ticket>> {
        val c = Ticket()
        c.id = random.nextInt(1000).toLong()
        c.ticketName = "Hội chợ iShopGo"
        c.name = "Nguyễn Huy Hoàng"
        c.phone = "0989013403"
        c.district = "Cầu Giấy"
        c.city = "Hà Nội"
        c.createdAt = "2018/04/27 15:35:25"
        c.address = "Dương Đình Nghệ"
        c.banner = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"


        val response = BaseResponse<Ticket>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).createTicket()
    }

    override fun getTicket(): Single<BaseResponse<Ticket>> {
        val c = Ticket()
        c.id = random.nextInt(1000).toLong()
        c.ticketName = "Hội chợ iShopGo"
        c.name = "Nguyễn Huy Hoàng"
        c.phone = "0989013403"
        c.district = "Cầu Giấy"
        c.city = "Hà Nội"
        c.createdAt = "2018/04/27 15:35:25"
        c.address = "Dương Đình Nghệ"
        c.banner = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"


        val response = BaseResponse<Ticket>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).getTicket()
    }

    override fun postSurvey(@Path("id") id: Long, @QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postSurvey(id, fields)
    }

    override fun getSurvey(): Single<BaseResponse<Survey>> {
        val response = BaseResponse<Survey>()
        response.status = 1
        return delegate.returningResponse(response).getSurvey()
    }

    override fun checkSurvey(): Single<BaseResponse<CheckSurvey>> {
        val response = BaseResponse<CheckSurvey>()
        response.status = 1
        response.data?.status = 1
        return delegate.returningResponse(response).checkSurvey()
    }

    override fun createRatingShop(product_id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createRatingShop(product_id, fields)
    }

    override fun createProductSalePoint(fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createProductSalePoint(fields)
    }

    override fun getProductSalePoint(product_id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<List<ProductSalePoint>>> {
        val ps = mutableListOf<ProductSalePoint>()
        for (i in 0..5)
            ps.add(generateProductSalePoint())

        val response = BaseResponse<List<ProductSalePoint>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getProductSalePoint(product_id, fields)
    }

    private fun generateProductSalePoint(): ProductSalePoint {
        val c = ProductSalePoint()
        c.id = random.nextInt(1000).toLong()
        c.name = "test điểm bán"
        c.phone = "0985771189"
        c.city = "Hà Nội"
        c.district = "Đống dá"
        c.address = "ABC"
        c.price = 2950000
        c.status = 1
        c.productName = "Vợt Cầu Lông LiNing Chính Hãng 3D BREAK FREE 80TD (Màu Đồng)"
        return c
    }

    override fun postProductPollow(fields: MutableMap<String, Any>): Single<BaseResponse<ProductFollow>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postProductPollow(fields)
    }

    override fun registerBooth(account_id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).registerBooth(account_id, body)
    }


    override fun getMemberDetail(memberId: Long): Single<BaseResponse<Member>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).getMemberDetail(memberId)
    }

    override fun deleteBooth(booth_id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).deleteBooth(booth_id)
    }

    override fun getBooth(fields: MutableMap<String, Any>): Single<BaseResponse<List<BoothManager>>> {
        val ps = mutableListOf<BoothManager>()
        for (i in 0..5)
            ps.add(generateBoothManager())

        val response = BaseResponse<List<BoothManager>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getBooth(fields)
    }

    private fun generateBoothManager(): BoothManager {
        val c = BoothManager()
        c.id = random.nextInt(1000).toLong()
        c.name = "Lê Đại Dương"
        c.phone = "0985771189"
        c.companyStore = "Ebc Viet Nam"
        c.region = "Khu vực 5"
        c.numberProduct = 0
        c.memberCnt = 0
        return c
    }

    override fun createBooth(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createBooth(body)
    }

    override fun changeStatusSalePoint(salePoint_id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).changeStatusSalePoint(salePoint_id)
    }

    override fun getSalePoint(fields: MutableMap<String, Any>): Single<BaseResponse<List<SalePoint>>> {
        val ps = mutableListOf<SalePoint>()
        for (i in 0..5)
            ps.add(generateSalePoint())

        val response = BaseResponse<List<SalePoint>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getSalePoint(fields)
    }

    private fun generateSalePoint(): SalePoint {
        val c = SalePoint()
        c.id = random.nextInt(1000).toLong()
        c.name = "test điểm bán"
        c.city = "Hà Nội"
        c.district = "Hoàn Kiếm"
        c.address = "333"
        c.price = 590000
        c.phone = "0985771140"
        c.status = 1
        c.productName = "Balo Nữ Thời Trang Naza"
        return c
    }

    override fun createSalePoint(fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createSalePoint(fields)
    }

    override fun getFavoriteProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getFavoriteProducts(params)
    }

    override fun getConfigBooth(): Single<BaseResponse<Booth>> {
        val c = Booth()
        c.name = "Nguyễn Huy Hoàng"
        c.hotline = "0989013403"
        c.introduction = "Chào các bạn"
        c.info = "Chào các bạn"
        c.address = "Dương Đình Nghệ, Cầu Giấy, Hà Nội"
        c.banner = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"


        val response = BaseResponse<Booth>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).getConfigBooth()
    }

    override fun editConfigBooth(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).editConfigBooth(body)
    }

    override fun postCommentProduct(product_id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postCommentProduct(product_id, body)
    }

    override fun postProductShare(id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).postProductShare(id)
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

    private fun generateProduct(): Product {
        val p = Product()
        p.id = random.nextInt(1000).toLong()
        p.image = "https://ishopgo.com/local/files/11793/clone-5a3c7e16148537e649a88ce4d2f28da1a1ae9ab1c48d2lotion-duong-da-toan-than-napie-skinjpg.jpg"
        p.name = "Lotion dưỡng trắng da toàn thân NAPIE SKIN WHITENING BODY LOTION"
        p.ttPrice = 50000
        p.price = 45000
        return p
    }

    private var delegate: BehaviorDelegate<ApiService.Auth> = behavior
    private val random = Random()

    override fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}