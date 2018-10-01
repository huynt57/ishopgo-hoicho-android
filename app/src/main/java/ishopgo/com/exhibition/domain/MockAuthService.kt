package ishopgo.com.exhibition.domain

import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.model.administrator.AdministratorPermissions
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.model.community.CommunityProduct
import ishopgo.com.exhibition.model.community.ManagerCommunity
import ishopgo.com.exhibition.model.member.ManageMember
import ishopgo.com.exhibition.model.member.MemberManager
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
    override fun createExchangeDiaryProduct(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createExchangeDiaryProduct(body)
    }

    override fun getListBGBN(fields: MutableMap<String, Any>): Single<BaseResponse<List<ListBGBN>>> {
        val response = BaseResponse<List<ListBGBN>>()
        response.status = 1
        return delegate.returningResponse(response).getListBGBN(fields)
    }

    override fun createNoStampNew(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<String>()
        response.status = 1
        return delegate.returningResponse(response).createNoStampNew(body)
    }

    override fun generateStamp(): Single<BaseResponse<String>> {
        val response = BaseResponse<String>()
        response.status = 1
        return delegate.returningResponse(response).generateStamp()
    }

    override fun getNoStampNew(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampNoListNew>>> {
        val response = BaseResponse<List<StampNoListNew>>()
        response.status = 1
        return delegate.returningResponse(response).getNoStampNew(fields)
    }

    override fun downloadNoStamp(id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).downloadNoStamp(id, body)
    }

    override fun deleteBoothMap(id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).deleteBoothMap(id)
    }

    override fun createdStampOrder(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createdStampOrder(body)
    }

    override fun updateStampOrder(id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).updateStampOrder(id, body)
    }

    override fun getImagesForConfig(fields: MutableMap<String, Any>): Single<BaseResponse<List<CertImages>>> {
        val response = BaseResponse<List<CertImages>>()
        response.status = 1
        return delegate.returningResponse(response).getImagesForConfig(fields)
    }

    override fun getBoothRelated(id: Long): Single<BaseResponse<List<BoothManager>>> {
        val response = BaseResponse<List<BoothManager>>()
        response.status = 1
        return delegate.returningResponse(response).getBoothRelated(id)
    }

    override fun getStampOrderStatistical(): Single<BaseResponse<StampOrdersStatistical>> {
        val response = BaseResponse<StampOrdersStatistical>()
        response.status = 1
        return delegate.returningResponse(response).getStampOrderStatistical()
    }

    override fun getStampOrders(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampListBuy>>> {
        val response = BaseResponse<List<StampListBuy>>()
        response.status = 1
        return delegate.returningResponse(response).getStampOrders(fields)
    }

    override fun evictionStampWarning(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).evictionStampWarning(body)
    }

    override fun restoreStampWarning(code: String, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).restoreStampWarning(code, body)
    }

    override fun loadStampWarning(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampListWarning>>> {
        val response = BaseResponse<List<StampListWarning>>()
        response.status = 1
        return delegate.returningResponse(response).loadStampWarning(fields)
    }

    override fun loadListScanStamp(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampUserListScan>>> {
        val response = BaseResponse<List<StampUserListScan>>()
        response.status = 1
        return delegate.returningResponse(response).loadListScanStamp(fields)
    }

    override fun saveStampAssign(id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).saveStampAssign(id, body)
    }

    override fun loadProductAssignNoStamp(id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<StampSearchProduct>> {
        val response = BaseResponse<StampSearchProduct>()
        response.status = 1
        return delegate.returningResponse(response).loadProductAssignNoStamp(id, fields)
    }

    override fun getStampDistribution(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampDistribution>>> {
        val response = BaseResponse<List<StampDistribution>>()
        response.status = 1
        return delegate.returningResponse(response).getStampDistribution(fields)
    }

    override fun getNoStampCreated(): Single<BaseResponse<StampNoDetail>> {
        val response = BaseResponse<StampNoDetail>()
        response.status = 1
        return delegate.returningResponse(response).getNoStampCreated()
    }

    override fun createNoStamp(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createNoStamp(body)
    }

    override fun editNoStampDetail(id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).editNoStampDetail(id, body)
    }

    override fun getNoStampDetail(id: Long): Single<BaseResponse<StampNoDetail>> {
        val response = BaseResponse<StampNoDetail>()
        response.status = 1
        return delegate.returningResponse(response).getNoStampDetail(id)
    }

    override fun loadNoStamp(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampNoList>>> {
        val response = BaseResponse<List<StampNoList>>()
        response.status = 1
        return delegate.returningResponse(response).loadNoStamp(fields)
    }

    override fun loadStampManager(fields: MutableMap<String, Any>): Single<BaseResponse<List<StampManager>>> {
        val response = BaseResponse<List<StampManager>>()
        response.status = 1
        return delegate.returningResponse(response).loadStampManager(fields)
    }

    override fun deleteProductInSalePoint(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).deleteProductInSalePoint(body)
    }

    override fun getMemberPermissions(fields: MutableMap<String, Any>): Single<BaseResponse<ManageMember>> {
        val response = BaseResponse<List<MemberManager>>()
        response.status = 1
        return delegate.returningResponse(response).getMemberPermissions(fields)
    }

    override fun getBoothPermissions(): Single<BaseResponse<MutableList<AdministratorPermissions>>> {
        val response = BaseResponse<List<AdministratorPermissions>>()
        response.status = 1
        return delegate.returningResponse(response).getBoothPermissions()
    }

    override fun deleteProductDiary(id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).deleteProductDiary(id, fields)
    }

    override fun createProductDiary(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).createProductDiary(body)
    }

    override fun getFriends(fields: MutableMap<String, Any>): Single<BaseResponse<List<ContactItem>>> {
        val response = BaseResponse<MutableList<String>>()
        response.status = 1
        return delegate.returningResponse(response).getFriends(fields)
    }

    override fun pushTop(params: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<MutableList<String>>()
        response.status = 1
        return delegate.returningResponse(response).pushTop(params)
    }

    override fun getAccountPermissions(fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<String>>> {
        val response = BaseResponse<MutableList<String>>()
        response.status = 1
        return delegate.returningResponse(response).getAccountPermissions(fields)
    }

    override fun editAdministrator(id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1

        return delegate.returningResponse(response).editAdministrator(id, body)
    }

    override fun addAdministrator(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1

        return delegate.returningResponse(response).addAdministrator(body)
    }

    override fun getAdministratorPermissions(): Single<BaseResponse<MutableList<AdministratorPermissions>>> {
        val response = BaseResponse<List<AdministratorPermissions>>()
        response.status = 1

        return delegate.returningResponse(response).getAdministratorPermissions()
    }

    override fun getAdministrator(fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<Administrator>>> {
        val response = BaseResponse<List<Administrator>>()
        response.status = 1

        return delegate.returningResponse(response).getAdministrator(fields)
    }

    override fun deleteAdministrator(id: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1

        return delegate.returningResponse(response).deleteAdministrator(id)
    }

    override fun getProductManagerDetail(id: Long): Single<BaseResponse<ProductDetail>> {
        val response = BaseResponse<List<ProductDetail>>()
        response.status = 1

        return delegate.returningResponse(response).getProductManagerDetail(id)
    }

    override fun assignBooth(position: Long, fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1

        return delegate.returningResponse(response).assignBooth(position, fields)
    }

    override fun getCommunity(fields: MutableMap<String, Any>): Single<BaseResponse<ManagerCommunity>> {
        val ps = mutableListOf<Community>()
        for (i in 0..5)
            ps.add(generateCommunity(i))

        val response = BaseResponse<List<Community>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getCommunity(fields)
    }

    private fun generateCommunity(i: Int): Community {
        val b = Community()
        b.id = random.nextInt(1000).toLong()
        b.createdAt = "2018/04/28 12:42:08"
        b.shopId = 17288
        b.content = "Chào các bạn"
        b.accountId = 18396
        b.accountName = "Vương Xuân Hồng"
        b.accountImage = "http://uptheme.ishopgo.com/files//tmp/phpzwxAWr"

        if (i % 3 == 0) {
            val c = CommunityProduct()
            c.name = "Trĩ Medi Medi Happy"
            c.price = 390000
            c.image = "https://static.ishopgo.com/17288/clone-5ac88a1b532731523091995.jpg"
            c.id = 16352
            b.product = c
        } else b.product = null

        if (i % 2 == 0) {
            val c = mutableListOf<String>()
            c.add("https://static.ishopgo.com/17288/fe0eb451a22ca727e6b2bd91baf8f3b4ozed-4e29d62a55f4dae4fc69899975657ab5wd-melasma-projpgjpg.jpg")
            b.images = c
        }

        if (i % 5 == 0) {
            val c = mutableListOf<String>()
            for (i in 0..1) {
                c.add("https://static.ishopgo.com/17288/fe0eb451a22ca727e6b2bd91baf8f3b4ozed-4e29d62a55f4dae4fc69899975657ab5wd-melasma-projpgjpg.jpg")
            }
            b.images = c
        }

        b.likeCount = 2
        b.commentCount = 0
        b.shareCount = 10
        return b
    }

    override fun editExpo(id: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).editExpo(id, body)
    }

    override fun settingExpo(expoId: Long, body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).settingExpo(expoId, body)
    }

    override fun deleteExpo(expoId: Long): Single<BaseResponse<Any>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).deleteExpo(expoId)
    }

    override fun addExpo(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).addExpo(body)
    }

    override fun getVisitors(fields: MutableMap<String, Any>): Single<BaseResponse<ManageVisitor>> {
        val response = BaseResponse<ManageMember>()
        response.status = 1

        return delegate.returningResponse(response).getVisitors(fields)
    }

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

    override fun getListTicket(fields: MutableMap<String, Any>): Single<BaseResponse<ManagerTicket>> {
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

    override fun getProductDetail(id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<ProductDetail>> {
        val detail = ProductDetail()
        detail.id = 16110
        detail.name = "Balo Nữ Thời Trang Naza"
        detail.price = 590000
        detail.description = "<!--?xml encoding=\\\"utf-8\\\" ?-->\\r\\n<p><strong>TH&Ocirc;NG TIN SẢN PHẨM</strong></p>\\r\\n\\r\\n<p>Balo thời trang, tặng k&egrave;m gấu b&ocirc;ng<br />\\r\\n-Chất liệu: da PU cấp bền đẹp, bề mặt t&uacute;i được dập v&acirc;n tinh xảo<br />\\r\\n-D&acirc;y đeo vai c&oacute; thể điều chỉnh độ d&agrave;i ngắn<br />\\r\\n-Ngăn t&uacute;i đủ rộng để đựng c&aacute;c đồ vật cần thiết ra ngo&agrave;i: ipad,điện tho<br />\\r\\n-Kh&oacute;a k&eacute;o chống gỉ<br />\\r\\n-Chi tiết t&uacute;i được l&agrave;m tinh xảo, đường may cẩn thận<br />\\r\\n-Sử dụng nhiều mục đ&iacute;ch: đi chơi, đi học,..<br />\\r\\n-K&iacute;ch thước: d&agrave;i 29cm, cao 31cm, rộng 12cm</p>\\r\\n"
        val department = Department()
        department.name = "Cafe trà sữa"
        department.id = 752
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

        val response = BaseResponse<ProductDetail>()
        response.status = 1
        response.data = detail

        return delegate.returningResponse(response).getProductDetail(id, fields)
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

    override fun createTicket(fields: MutableMap<String, Any>): Single<BaseResponse<Ticket>> {
        val c = Ticket()
        c.id = random.nextInt(1000).toLong()
        c.name = "Nguyễn Huy Hoàng"
        c.phone = "0989013403"
        c.district = "Cầu Giấy"
        c.city = "Hà Nội"
        c.createdAt = "2018/04/27 15:35:25"
        c.address = "Dương Đình Nghệ"
        c.avatar = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"


        val response = BaseResponse<Ticket>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).createTicket(fields)
    }

    override fun getMyTicket(): Single<BaseResponse<List<Ticket>>> {
        val c = Ticket()
        c.id = random.nextInt(1000).toLong()
        c.name = "Nguyễn Huy Hoàng"
        c.phone = "0989013403"
        c.district = "Cầu Giấy"
        c.city = "Hà Nội"
        c.createdAt = "2018/04/27 15:35:25"
        c.address = "Dương Đình Nghệ"
        c.avatar = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"


        val response = BaseResponse<List<Ticket>>()
        response.status = 1
        response.data = mutableListOf(c)

        return delegate.returningResponse(response).getMyTicket()
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

    override fun getBooth(fields: MutableMap<String, Any>): Single<BaseResponse<ManagerBooth>> {
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

    override fun getConfigBooth(): Single<BaseResponse<BoothConfig>> {
        val c = BoothConfig()
        c.name = "Nguyễn Huy Hoàng"
        c.hotline = "0989013403"
        c.introduction = "Chào các bạn"
        c.info = "Chào các bạn"
        c.address = "Dương Đình Nghệ, Cầu Giấy, Hà Nội"
        c.banner = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"


        val response = BaseResponse<BoothConfig>()
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
        p.promotionPrice = 50000
        p.price = 45000
        return p
    }

    private var delegate: BehaviorDelegate<ApiService.Auth> = behavior
    private val random = Random()

    override fun refreshToken(): Single<BaseResponse<RefreshTokenResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}