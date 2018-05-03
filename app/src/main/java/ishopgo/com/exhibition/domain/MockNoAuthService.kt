package ishopgo.com.exhibition.domain

import android.util.Log
import io.reactivex.Single
import ishopgo.com.exhibition.domain.response.*
import retrofit2.mock.BehaviorDelegate
import java.util.*

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class MockNoAuthService(behavior: BehaviorDelegate<ApiService.NoAuth>) : ApiService.NoAuth {

    override fun getProductDetail(id: Long, params: MutableMap<String, Any>): Single<BaseResponse<ProductDetail>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRelateProducts(id: Long, params: MutableMap<String, Any>): Single<BaseResponse<List<Product>>> {
        val ps = mutableListOf<Product>()
        for (i in 0..5)
            ps.add(generateProduct())

        val response = BaseResponse<List<Product>>()
        response.status = 1
        response.data = ps

        return delegate.returningResponse(response).getRelateProducts(id, params)
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