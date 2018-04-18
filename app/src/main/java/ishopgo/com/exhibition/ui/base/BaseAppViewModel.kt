package ishopgo.com.exhibition.ui.base

import ishopgo.com.exhibition.domain.ApiService
import javax.inject.Inject

/**
 * @author Steve
 * @since 10/22/17
 */
open class BaseApiViewModel : BaseViewModel() {
    companion object {
        private val TAG = "BaseAppViewModel"
    }

    @Inject
    lateinit var apiService: ApiService

}