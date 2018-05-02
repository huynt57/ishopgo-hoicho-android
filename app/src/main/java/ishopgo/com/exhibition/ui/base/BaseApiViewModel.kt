package ishopgo.com.exhibition.ui.base

import ishopgo.com.exhibition.domain.ApiService
import javax.inject.Inject

open class BaseApiViewModel : BaseViewModel() {
    companion object {
        private val TAG = "BaseAppViewModel"
    }

    @Inject
    lateinit var authService: ApiService.Auth

    @Inject
    lateinit var noAuthService: ApiService.NoAuth

}