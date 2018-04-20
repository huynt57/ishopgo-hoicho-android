package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Component
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
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

}