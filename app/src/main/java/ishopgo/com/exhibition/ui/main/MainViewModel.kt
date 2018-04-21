package ishopgo.com.exhibition.ui.main

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var highlightBrand = MutableLiveData<List<HighlightBrandProvider>>()

    fun loadHighlightBrands() {
        val dummy = mutableListOf<HighlightBrandProvider>()
        for (i in 0..14)
            dummy.add(object : IdentityData(), HighlightBrandProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/11793/d2deb19a06df9d842fb60a966f1d2b9amy-pham-cao-cap-tphcm-napie-skinpng.png"
                }

            })

        highlightBrand.postValue(dummy)
    }

    var highlightProducts = MutableLiveData<List<ProductProvider>>()

    fun loadHighlightProducts() {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

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

        highlightProducts.postValue(dummy)
    }

    var viewedProducts = MutableLiveData<List<ProductProvider>>()

    fun loadViewedProducts() {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

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

    var suggestedProducts = MutableLiveData<List<ProductProvider>>()

    fun loadSuggestedProducts() {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless (Gợi ý)"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        suggestedProducts.postValue(dummy)
    }

    var favoriteProducts = MutableLiveData<List<ProductProvider>>()

    fun loadFavoriteProducts() {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

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

    var categories = MutableLiveData<List<CategoryProvider>>()

    fun loadCategories() {
        val dummy = mutableListOf<CategoryProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), CategoryProvider {

                init {
                    id = i.toLong()
                }

                override fun provideIcon(): String {
                    return "http://theme.hstatic.net/1000122548/1000237689/14/leftmenu_icon_4.png?v=127"
                }

                override fun provideName(): String {
                    return "Quần áo"
                }

                override fun provideChilds(): List<CategoryProvider> {
                    val child = mutableListOf<CategoryProvider>()
                    for (i in 0..4)
                        child.add(object : IdentityData(), CategoryProvider {
                            override fun provideIcon(): String {
                                return "http://theme.hstatic.net/1000122548/1000237689/14/leftmenu_icon_4.png?v=127"
                            }

                            override fun provideName(): String {
                                return "Trang sức"
                            }

                            override fun provideChilds(): List<CategoryProvider> {
                                return listOf()
                            }

                            override fun provideIsParent(): Boolean {
                                return false
                            }

                        })
                    return child
                }

                override fun provideIsParent(): Boolean {
                    return true
                }

            })

        categories.postValue(dummy)
    }

    var banners = MutableLiveData<List<String>>()

    fun loadBanners() {
        val dummy = mutableListOf<String>()
        dummy.add("http://1.bp.blogspot.com/-y1UTm1XVdbI/U0eBjHlNymI/AAAAAAAABFg/IMtJO_CEf0o/s1600/Revlon+Lipstick+Banner+3.jpg")
        dummy.add("http://winkandasmilemakeup.com/wp-content/uploads/parser/makeup-banner-1.png")
        dummy.add("https://s3.amazonaws.com/pikpsdnw/wp-content/uploads/2017/11/19123047/91.jpg")
        dummy.add("http://corp.sasa.com/wps/wcm/connect/c5057d3f-34fa-4460-9aff-e46559c9ec42/CYB_Rosy+Makeup+line_Lipstick_Web+banner_20161125-01.jpg?MOD=AJPERES&CACHEID=c5057d3f-34fa-4460-9aff-e46559c9ec42")

        banners.postValue(dummy)
    }

}