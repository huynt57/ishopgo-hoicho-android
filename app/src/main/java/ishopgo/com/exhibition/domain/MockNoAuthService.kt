package ishopgo.com.exhibition.domain

import android.util.Log
import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.User
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.model.community.CommunityProduct
import ishopgo.com.exhibition.model.community.ManagerCommunity
import ishopgo.com.exhibition.model.post.BoothPostManager
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostContent
import ishopgo.com.exhibition.model.post.PostsManager
import ishopgo.com.exhibition.model.postmenu.PostMenuManager
import ishopgo.com.exhibition.model.question.QuestionCategory
import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.model.question.QuestionManager
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.model.search_sale_point.ManagerSearchSalePoint
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import okhttp3.RequestBody
import retrofit2.http.QueryMap
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockNoAuthService(behavior: BehaviorDelegate<ApiService.NoAuth>) : ApiService.NoAuth {
    override fun getBoothPost(fields: MutableMap<String, Any>): Single<BaseResponse<BoothPostManager>> {
        val response = BaseResponse<BoothPostManager>()
        response.status = 1

        return delegate.returningResponse(response).getBoothPost(fields)
    }

    override fun getIntroduction(): Single<BaseResponse<Introduction>> {
        val response = BaseResponse<QuestionManager>()
        response.status = 1

        return delegate.returningResponse(response).getIntroduction()
    }

    override fun getExpoShopLocations(expoId: Long, fields: MutableMap<String, Any>): Single<BaseResponse<List<ExpoShop>>> {
        val response = BaseResponse<QuestionManager>()
        response.status = 1

        return delegate.returningResponse(response).getExpoShopLocations(expoId, fields)
    }

    override fun getExpos(fields: MutableMap<String, Any>): Single<BaseResponse<List<ExpoConfig>>> {
        val response = BaseResponse<QuestionManager>()
        response.status = 1

        return delegate.returningResponse(response).getExpos(fields)
    }

    override fun getQuestion(fields: MutableMap<String, Any>): Single<BaseResponse<QuestionManager>> {
        val response = BaseResponse<QuestionManager>()
        response.status = 1

        return delegate.returningResponse(response).getQuestion(fields)
    }

    override fun getQuestionDetail(question_id: Long): Single<BaseResponse<QuestionDetail>> {
        val response = BaseResponse<QuestionDetail>()
        response.status = 1

        return delegate.returningResponse(response).getQuestionDetail(question_id)
    }

    override fun getQuestionCategory(fields: MutableMap<String, Any>): Single<BaseResponse<List<QuestionCategory>>> {
        val response = BaseResponse<QuestionCategory>()
        response.status = 1

        return delegate.returningResponse(response).getQuestionCategory(fields)
    }

    override fun getPostCategory(fields: MutableMap<String, Any>): Single<BaseResponse<List<PostCategory>>> {
        val response = BaseResponse<PostCategory>()
        response.status = 1

        return delegate.returningResponse(response).getPostCategory(fields)
    }

    override fun getPostDetail(post_id: Long): Single<BaseResponse<PostContent>> {
        val response = BaseResponse<PostContent>()
        response.status = 1

        return delegate.returningResponse(response).getPostDetail(post_id)
    }

    override fun getPost(fields: MutableMap<String, Any>): Single<BaseResponse<PostMenuManager>> {
        val response = BaseResponse<PostsManager>()
        response.status = 1

        return delegate.returningResponse(response).getPost(fields)
    }

    override fun getSalePointDetail(@QueryMap fields: MutableMap<String, Any>): Single<BaseResponse<ManagerSalePointDetail>> {
        val response = BaseResponse<ManagerSearchSalePoint>()
        response.status = 1
        return delegate.returningResponse(response).getSalePointDetail(fields)
    }

    override fun searchSalePoint(fields: MutableMap<String, Any>): Single<BaseResponse<ManagerSearchSalePoint>> {
        val response = BaseResponse<ManagerSearchSalePoint>()
        response.status = 1
        return delegate.returningResponse(response).searchSalePoint(fields)
    }

    override fun getSubCategories(@QueryMap params: MutableMap<String, Any>): Single<BaseResponse<List<Category>>> {
        val dummy = mutableListOf<Category>()
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))

        val response = BaseResponse<List<Category>>()
        response.status = 1
        response.data = dummy

        return delegate.returningResponse(response).getSubCategories(params)
    }

    override fun getCategoriedProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getCategoriedProducts(params)
    }

    override fun getBoothCategories(boothId: Long): Single<BaseResponse<List<Category>>> {
        val dummy = mutableListOf<Category>()
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))

        val response = BaseResponse<List<Category>>()
        response.status = 1
        response.data = dummy

        return delegate.returningResponse(response).getBoothCategories(boothId)
    }

    override fun getSuggestedProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getSuggestedProducts(params)
    }

    override fun getViewedProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getViewedProducts(params)
    }

    override fun getBoothProducts(boothId: Long, params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getBoothProducts(boothId, params)
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

    override fun getCommentCommunity(post_id: Long, fields: MutableMap<String, Any>): Single<BaseResponse<MutableList<CommunityComment>>> {
        val ps = mutableListOf<CommunityComment>()
        for (i in 0..5)
            ps.add(generateCommunityComment(i))

        val response = BaseResponse<CommunityComment>()
        response.status = 1
        return delegate.returningResponse(response).getCommentCommunity(post_id, fields)
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

    override fun getCommunity(fields: MutableMap<String, Any>): Single<BaseResponse<ManagerCommunity>> {
        val ps = mutableListOf<Community>()
        for (i in 0..5)
            ps.add(generateCommunity(i))

        val response = BaseResponse<List<Community>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getCommunity(fields)
    }

    override fun login(phone: String, password: String, domain: String, token: String): Single<BaseResponse<User>> {
        val c = User()
        c.id = 18398
        c.name = "Nnguyễn Huy Hoàng"
        c.image = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"
        c.type = "Chủ gian hàng"
        c.token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjE4Mzk4LCJpc3MiOiJodHRwOi8vaXNob3Bnby5jb20vYXBpL3YxL2V4cG8vbG9naW4iLCJpYXQiOjE1MjU0MDAxMjUsImV4cCI6MTUyNTQwNzMyNSwibmJmIjoxNTI1NDAwMTI1LCJqdGkiOiJsdmdNd2d0OU1NQkxiM3oyIn0.WNXo3TGwPUxLahefEK15bevWNKsYt_6xMPsh4124bog"

        val response = BaseResponse<User>()
        response.status = 1
        response.data = c

        return delegate.returningResponse(response).login(phone, password, domain, token)
    }

    override fun register(body: RequestBody): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).register(body)
    }

    override fun getOTP(fields: MutableMap<String, Any>): Single<BaseResponse<Any>> {
        val response = BaseResponse<Any>()
        response.status = 1
        return delegate.returningResponse(response).getOTP(fields)
    }

    override fun getShopRatings(id: Long, params: MutableMap<String, Any>): Single<BaseResponse<List<ShopRate>>> {
        val c = ShopRate()
        c.id = 462
        c.content = "Sản phẩm dùng rất tốt, có hiệu quả ngay lần sử dụng thứ 2. Sẽ còn quay lại shop."
        val user = User()
        user.name = "Vương Xuân Hồng"
        user.image = "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
        c.account = user
        c.ratePoint = 4

        val result = mutableListOf<ShopRate>()
        for (i in 0..3)
            result.add(c)

        val response = BaseResponse<List<ShopRate>>()
        response.status = 1
        response.data = result

        return delegate.returningResponse(response).getShopRatings(id, params)
    }

    override fun getProductComments(id: Long, params: MutableMap<String, Any>): Single<BaseResponse<List<ProductComment>>> {
        val c = ProductComment()
        c.id = 462
        c.content = "Sản phẩm dùng rất tốt, có hiệu quả ngay lần sử dụng thứ 2. Sẽ còn quay lại shop."
        c.accountName = "Vương Xuân Hồng"
        c.accountImage = "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
        c.status = 2
        c.updatedAt = "2018/04/09 14:18:35"
        c.commentCount = 0
        c.lastComment = null

        val result = mutableListOf<ProductComment>()
        for (i in 0..3)
            result.add(c)

        val response = BaseResponse<List<ProductComment>>()
        response.status = 1
        response.data = result

        return delegate.returningResponse(response).getProductComments(id, params)
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
        val booth = Booth()
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

    override fun getRelateProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getRelateProducts(params)
    }

    override fun searchShops(params: MutableMap<String, Any>): Single<BaseResponse<ManagerShop>> {
        val ps = mutableListOf<Shop>()
        for (i in 0..5)
            ps.add(generateShop())

        val response = BaseResponse<ManagerShop>()
        response.status = 1
        response.data?.total = ps.size
        response.data?.booth = ps

        return delegate.returningResponse(response).searchShops(params)
    }

    override fun searchProducts(params: MutableMap<String, Any>): Single<BaseResponse<ManagerProduct>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<ManagerProduct>()
        response.data?.total = ps.size
        response.data?.product = ps

        return delegate.returningResponse(response).searchProducts(params)
    }

    private var delegate: BehaviorDelegate<ApiService.NoAuth> = behavior
    private val random = Random()

    override fun getCategories(): Single<BaseResponse<List<Category>>> {
        val dummy = mutableListOf<Category>()
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))
        dummy.add(generateCategory(random.nextInt(5)))

        val response = BaseResponse<List<Category>>()
        response.status = 1
        response.data = dummy

        return delegate.returningResponse(response).getCategories()
    }

    private fun generateCategory(subCount: Int): Category {
        val cat = Category()
        cat.id = random.nextInt(1000).toLong()
        cat.name = "Danh mục ${cat.id}"

        if (subCount <= 0) {
            cat.subs = mutableListOf()
        } else {
            val subs = mutableListOf<Category>()
            for (i in 0..subCount)
                subs.add(generateCategory(0))
            cat.subs = subs
        }

        return cat

    }

    override fun getBanners(): Single<BaseResponse<List<Banner>>> {
        Log.d(TAG, "getBanners: ")
        val dummy = mutableListOf<Banner>()
        dummy.add(generateBanner())
        dummy.add(generateBanner())
        dummy.add(generateBanner())
        dummy.add(generateBanner())
        dummy.add(generateBanner())

        val response = BaseResponse<List<Banner>>()
        response.status = 1
        response.data = dummy
        return delegate.returningResponse(response).getBanners()
    }

    private fun generateBanner(): Banner {
        val b = Banner()
        b.image = images[random.nextInt(images.size)]
        b.url = urls[random.nextInt(urls.size)]
        return b
    }

    override fun getHighlightBrands(): Single<BaseResponse<List<Brand>>> {
        val ps = mutableListOf<Brand>()
        for (i in 0..5)
            ps.add(generateBrand())

        val response = BaseResponse<List<Brand>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getHighlightBrands()
    }

    override fun getHighlightProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getHighlightProducts(params)
    }

    private fun generateShop(): Shop {
        val s = Shop()
        s.id = random.nextInt(1000).toLong()
        s.banner = "https://static.ishopgo.com/17288/clone-5ac88a12d51431523091986."
        s.name = "Thảo dược giảm cân Sen Quỳnh"
        s.createdAt = "2018/04/27 17:55:13"
        s.hotline = "0985771133"
        s.numberProduct = 450
        return s
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

    private fun generateBrand(): Brand {
        val b = Brand()
        b.id = random.nextInt(1000).toLong()
        b.logo = "https://s3-ap-southeast-1.amazonaws.com/ishopgo/11793/d2deb19a06df9d842fb60a966f1d2b9amy-pham-cao-cap-tphcm-napie-skinpng.png"
        b.name = "Mỹ phẩm cao cấp Napie Skin"
        return b
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


    override fun getBrandProducts(brandId: Long, params: MutableMap<String, Any>): Single<BaseResponse<SearchProducts>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val result = SearchProducts()
        result.total = 6
        result.data = ps

        val response = BaseResponse<SearchProducts>()
        response.status = 1
        response.data = result

        return delegate.returningResponse(response).getBrandProducts(brandId, params)
    }

    companion object {
        private val images = mutableListOf(
                "https://s3-ap-southeast-1.amazonaws.com/ishopgo/11793/f769e1fef23eaafe759aec2279a08162np7jpg.jpg",
                "https://s3-ap-southeast-1.amazonaws.com/ishopgo/11793/25ceed8b8b8b1462d3b205b671379771np4jpg.jpg",
                "https://s3.amazonaws.com/pikpsdnw/wp-content/uploads/2017/11/19123047/91.jpg",
                "http://corp.sasa.com/wps/wcm/connect/c5057d3f-34fa-4460-9aff-e46559c9ec42/CYB_Rosy+Makeup+line_Lipstick_Web+banner_20161125-01.jpg?MOD=AJPERES&CACHEID=c5057d3f-34fa-4460-9aff-e46559c9ec42"
        )
        private val urls = mutableListOf(
                "https://www.youtube.com/watch?v=faciBqUjZtA",
                "https://www.youtube.com/watch?v=ApwxP72Jnkk",
                "https://www.youtube.com/watch?v=ApwxP72Jnkk",
                "https://www.youtube.com/watch?v=ApwxP72Jnkk"
        )

        private val TAG = "MockNoAuthService"

    }

}