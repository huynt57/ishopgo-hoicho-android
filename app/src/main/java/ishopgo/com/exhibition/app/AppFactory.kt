package ishopgo.com.exhibition.app

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * @author Steve
 * @since 10/22/17
 */
class AppFactory(val application: MyApp) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val t: T = super.create(modelClass)
        if (t is AppComponent.Injectable) {
            (t as AppComponent.Injectable).inject(application.appComponent)
        }

        return t
    }
}