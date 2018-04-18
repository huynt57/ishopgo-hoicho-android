package ishopgo.com.exhibition.app

import android.app.Application
import ishopgo.com.exhibition.ui.base.splash.SplashViewModel
import dagger.Component
import ishopgo.com.exhibition.domain.ApiService
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class))
interface AppComponent {

    fun application(): Application

    fun apiService(): ApiService

    fun inject(splashViewModel: SplashViewModel)

    interface Injectable {
        fun inject(appComponent: AppComponent)
    }

}