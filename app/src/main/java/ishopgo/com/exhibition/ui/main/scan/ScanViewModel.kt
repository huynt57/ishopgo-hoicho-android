package ishopgo.com.exhibition.ui.main.scan

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ScanViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}