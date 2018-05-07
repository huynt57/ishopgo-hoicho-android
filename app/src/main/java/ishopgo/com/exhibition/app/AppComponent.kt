package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Component
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.login.LoginViewModel
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.account.AccountViewModel
import ishopgo.com.exhibition.ui.main.brand.popular.PopularBrandsViewModel
import ishopgo.com.exhibition.ui.main.home.HomeViewModel
import ishopgo.com.exhibition.ui.main.home.category.product.ProductsByCategoryViewModel
import ishopgo.com.exhibition.ui.main.home.search.SearchViewModel
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductViewModel
import ishopgo.com.exhibition.ui.main.home.search.shop.SearchShopsViewModel
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentViewModel
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsViewModel
import ishopgo.com.exhibition.ui.main.product.popular.PopularProductsViewModel
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopViewModel
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsViewModel
import ishopgo.com.exhibition.ui.main.profile.ProfileViewModel
import ishopgo.com.exhibition.ui.main.scan.ScanViewModel
import ishopgo.com.exhibition.ui.main.configbooth.ConfigBoothViewModel
import ishopgo.com.exhibition.ui.main.notification.NotificationViewModel
import ishopgo.com.exhibition.ui.main.shop.ShopDetailViewModel
import ishopgo.com.exhibition.ui.main.shop.category.CategoryViewModel
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoViewModel
import ishopgo.com.exhibition.ui.main.shop.rate.RateViewModel
import ishopgo.com.exhibition.ui.splash.SplashViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class))
interface AppComponent {

    fun application(): Application

    fun authService(): ApiService.Auth

    fun noAuthService(): ApiService.NoAuth

    fun isgService(): ApiService.ISGApi

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
    fun inject(shopProductsViewModel: ProductsOfShopViewModel)
    fun inject(productCommentViewModel: ProductCommentViewModel)
    fun inject(shopDetailViewModel: ShopDetailViewModel)
    fun inject(shopInfoViewModel: ShopInfoViewModel)
    fun inject(categoryViewMode: CategoryViewModel)
    fun inject(accountViewModel: AccountViewModel)
    fun inject(profileViewModel: ProfileViewModel)
    fun inject(communityViewModel: CommunityViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(scanViewModel: ScanViewModel)
    fun inject(productsOfBrandViewModel: ProductsOfBrandViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(searchViewModel: SearchViewModel)
    fun inject(searchProductViewModel: SearchProductViewModel)
    fun inject(searchShopHasProductsViewModel: SearchShopsViewModel)
    fun inject(productsByCategoryViewModel: ProductsByCategoryViewModel)
    fun inject(rateViewModel: RateViewModel)
    fun inject(configBoothViewModel: ConfigBoothViewModel)
    fun inject(notificationViewModel: NotificationViewModel)
}