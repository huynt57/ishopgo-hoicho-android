package ishopgo.com.exhibition.domain

import android.util.Log
import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.Community.CommunityProduct
import ishopgo.com.exhibition.model.LoginResponse
import okhttp3.RequestBody
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockNoAuthService(behavior: BehaviorDelegate<ApiService.NoAuth>) : ApiService.NoAuth {
    override fun getCommunity(fields: MutableMap<String, Any>): Single<BaseResponse<List<Community>>> {
        val ps = mutableListOf<Community>()
        for (i in 0..5)
            ps.add(generateCommunity(i))

        val response = BaseResponse<List<Community>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getCommunity(fields)
    }

    override fun login(phone: String, password: String, domain: String, token: String): Single<BaseResponse<LoginResponse>> {
        val c = LoginResponse()
        c.id = 18398
        c.name = "Nnguyễn Huy Hoàng"
        c.image = "https://static.ishopgo.com/17288/1e07cc8716cbe857c3cea1cb3cf772e8avatar-1525273657277jpg.jpg"
        c.type = "Chủ gian hàng"
        c.token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjE4Mzk4LCJpc3MiOiJodHRwOi8vaXNob3Bnby5jb20vYXBpL3YxL2V4cG8vbG9naW4iLCJpYXQiOjE1MjU0MDAxMjUsImV4cCI6MTUyNTQwNzMyNSwibmJmIjoxNTI1NDAwMTI1LCJqdGkiOiJsdmdNd2d0OU1NQkxiM3oyIn0.WNXo3TGwPUxLahefEK15bevWNKsYt_6xMPsh4124bog"

        val response = BaseResponse<LoginResponse>()
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

    override fun getShopRatings(id: Long, params: MutableMap<String, Any>): Single<BaseResponse<List<ProductComment>>> {
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

    override fun searchShops(params: MutableMap<String, Any>): Single<BaseResponse<List<Shop>>> {
        val ps = mutableListOf<Shop>()
        for (i in 0..5)
            ps.add(generateShop())

        val response = BaseResponse<List<Shop>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).searchShops(params)
    }

    override fun searchProducts(params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).searchProducts(params)
    }

    private var delegate: BehaviorDelegate<ApiService.NoAuth> = behavior
    private val random = Random()

    override fun getCategories(): Single<BaseResponse<List<Category>>> {
        Log.d(TAG, "getCategories: ")
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

    override fun getHighlightBrands(params: MutableMap<String, Any>): Single<BaseResponse<List<Brand>>> {
        val ps = mutableListOf<Brand>()
        for (i in 0..5)
            ps.add(generateBrand())

        val response = BaseResponse<List<Brand>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getHighlightBrands(params)
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
        p.image = "https://static.ishopgo.com/17288/clone-5ac88a12d51431523091986."
        p.name = "Thảo dược giảm cân Sen Quỳnh"
        p.ttPrice = 50000
        p.price = 45000
        return p
    }

    private fun generateBrand(): Brand {
        val b = Brand()
        b.id = random.nextInt(1000).toLong()
        b.logo = "https://static.ishopgo.com/17288/clone-5ac88a12d51431523091986."
        b.name = "Thảo dược giảm cân Sen Quỳnh"
        return b
    }

    private fun generateCommunity(i: Int): Community {
        val b = Community()
        b.id = random.nextInt(1000).toLong()
        b.createdAt = "2018/04/28 12:42:08"
        b.shopId = 17288
        b.content = "Chào các bạn"
        b.accountId = 18396
        b.accountName = "Nguyễn Nam Phong"
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


    override fun getBrandProducts(brandId: Long, params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getBrandProducts(brandId, params)
    }

    companion object {
        private val images = mutableListOf(
                "http://1.bp.blogspot.com/-y1UTm1XVdbI/U0eBjHlNymI/AAAAAAAABFg/IMtJO_CEf0o/s1600/Revlon+Lipstick+Banner+3.jpg",
                "http://winkandasmilemakeup.com/wp-content/uploads/parser/makeup-banner-1.png",
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