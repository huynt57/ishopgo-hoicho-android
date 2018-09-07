package ishopgo.com.exhibition.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.crashlytics.android.core.CrashlyticsCore
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ishopgo.com.exhibition.BuildConfig
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.model.UserDataManager

/**
 * @author Steve
 * @since 10/22/17
 */
open class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    val loadingStatus: MutableLiveData<Boolean> = MutableLiveData()
    var errorSignal: MutableLiveData<BaseErrorSignal> = MutableLiveData()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun resolveError(statusCode: Int, message: String) {
        when (statusCode) {
            BaseErrorSignal.ERROR_HAS_MESSAGE -> { // has message
                Log.e("ISG_Error", message)
                errorSignal.postValue(BaseErrorSignal(BaseErrorSignal.ERROR_HAS_MESSAGE, message))
            }
            BaseErrorSignal.ERROR_401 -> {
                Log.e("ISG_Error", "401")
                reportBug(Exception("Hết phiên đăng nhập. Chưa refresh token"))
                errorSignal.postValue(BaseErrorSignal(BaseErrorSignal.ERROR_401, "Hãy đăng nhập lại."))
            }
            BaseErrorSignal.ERROR_SERVER -> {
                Log.e("ISG_Error", "server error $message")
                reportBug(Exception(message))
                errorSignal.postValue(BaseErrorSignal(BaseErrorSignal.ERROR_SERVER, "Hệ thống đang được cập nhật. Vui lòng khởi động lại hoặc quản lại sau."))
            }
            BaseErrorSignal.ERROR_NETWORK -> {
                Log.e("ISG_Error", "no network")
                errorSignal.postValue(BaseErrorSignal(BaseErrorSignal.ERROR_NETWORK, "Lỗi kết nối mạng. Kiểm tra lại đường truyền của bạn."))
            }
            BaseErrorSignal.ERROR_UNKNOWN -> {
                Log.e("ISG_Error", message)
                reportBug(Exception(message))
                errorSignal.postValue(BaseErrorSignal(BaseErrorSignal.ERROR_UNKNOWN, "Có lỗi xảy ra."))
            }
            else -> {
            }
        }
    }

    private fun reportBug(e: Exception) {
        if (!BuildConfig.DEBUG) {
            val crashlytic = CrashlyticsCore.getInstance()
            crashlytic.setUserIdentifier(UserDataManager.currentUserId.toString())
            crashlytic.setUserName(UserDataManager.currentUserPhone)

            crashlytic.logException(e)
        }
    }
}