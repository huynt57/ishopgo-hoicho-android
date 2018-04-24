package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Component
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.account.AccountViewModel
import ishopgo.com.exhibition.ui.main.brand.popular.PopularBrandsViewModel
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentViewModel
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsViewModel
import ishopgo.com.exhibition.ui.main.product.popular.PopularProductsViewModel
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsViewModel
import ishopgo.com.exhibition.ui.main.profile.ProfileViewModel
import ishopgo.com.exhibition.ui.main.shop.ShopDetailViewModel
import ishopgo.com.exhibition.ui.main.shop.category.CategoryViewMode
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoViewModel
import ishopgo.com.exhibition.ui.splash.SplashViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class))
interface AppComponent {

    fun application(): Application

    fun apiService(): ApiService

    interface Injectable {
        fun inject(appComponent: AppComponent)
    }

    fun inject(splashViewModel: SplashViewModel)
    fun inject(main: MainViewModel)
    fun inject(productDetailViewModel: ProductDetailViewModel)
    fun inject(favoriteProductsViewModel: FavoriteProductsViewModel)
    fun inject(viewedProductsViewModel: ViewedProductsViewModel)
    fun inject(popularBrandsViewModel: PopularBrandsViewModel)
    fun inject(popularProductsViewModel: PopularProductsViewModel)
    fun inject(brandedProductsViewModel: ProductsOfShopViewModel)
    fun inject(productCommentViewModel: ProductCommentViewModel)
    fun inject(shopDetailViewModel: ShopDetailViewModel)
    fun inject(shopInfoViewModel: ShopInfoViewModel)
    fun inject(categoryViewMode: CategoryViewMode)
    fun inject(accountViewModel: AccountViewModel)
    fun inject(profileViewModel: ProfileViewModel)
}