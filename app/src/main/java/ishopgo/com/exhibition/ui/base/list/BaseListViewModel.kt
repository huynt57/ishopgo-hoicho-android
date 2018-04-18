package ishopgo.com.exhibition.ui.base.list

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 3/28/18. HappyCoding!
 */
abstract class BaseListViewModel<T> : BaseApiViewModel() {

    var dataReturned = MutableLiveData<T>()

    abstract fun loadData(params: RequestParams)
}