package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var sameShopProducts = MutableLiveData<List<ProductProvider>>()

    fun loadSameShopProducts(productId: Long) {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : ProductProvider {
                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        sameShopProducts.postValue(dummy)
    }

    var viewedProducts = MutableLiveData<List<ProductProvider>>()

    fun loadViewedProducts(productId: Long) {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : ProductProvider {
                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless (Đã xem)"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        viewedProducts.postValue(dummy)
    }

    var favoriteProducts = MutableLiveData<List<ProductProvider>>()

    fun loadFavoriteProducts(productId: Long) {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : ProductProvider {
                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotlessý (Yêu thích)"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        favoriteProducts.postValue(dummy)
    }

    var detail = MutableLiveData<ProductDetailProvider>()

    fun loadProductDetail(productId: Long) {

        detail.postValue(object : ProductDetailProvider {
            override fun provideProductImage(): String {
                return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
            }

            override fun provideProductName(): String {
                return "Kem trị thâm mụn Medi Spotless Kem trị thâm mụn Medi Spotless"
            }

            override fun provideProductPrice(): String {
                return "530.000 đ"
            }

            override fun provideProductBrand(): String {
                return "Medi White"
            }

            override fun provideProductShortDescription(): String {
                return "✎ Kem trị thâm mụn Medi White đánh bay mọi vết thâm, sạm da do sẹo mụn để lại, làm trắng da, giúp dưỡng da tươi trẻ và mịn màng không tỳ vết.\n" +
                        "\n" +
                        "✎ Xóa mờ các vết thâm sẹo, thâm do té xe, thâm da do bỏng bô, côn trùng chích, dời leo, mụn nhọt, làm đều tông màu da\n" +
                        "\n"
            }

            override fun provideShopName(): String {
                return "Mua Đồ Tốt"
            }

            override fun provideShopRegion(): String {
                return "Hồ Chí Minh"
            }

            override fun provideShopProductCount(): Int {
                return 40
            }

            override fun provideShopRateCount(): Int {
                return 10
            }

            override fun provideShopPhone(): String {
                return "0974427143"
            }

            override fun provideProductLikeCount(): Int {
                return 90
            }

            override fun provideProductCommentCount(): Int {
                return 80
            }

            override fun provideProductShareCount(): Int {
                return 50
            }

        })
    }

}