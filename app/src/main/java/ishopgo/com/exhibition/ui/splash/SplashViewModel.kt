package ishopgo.com.exhibition.ui.splash

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 1/2/18. HappyCoding!
 */
class SplashViewModel : BaseApiViewModel(), AppComponent.Injectable {
    companion object {
        private val TAG = "SplashViewModel"
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}