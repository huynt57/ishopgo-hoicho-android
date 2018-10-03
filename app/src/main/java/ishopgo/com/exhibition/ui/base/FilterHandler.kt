package ishopgo.com.exhibition.ui.base

import android.view.View

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
interface FilterHandler {
    fun triggerSearch(key: String)
    fun searchReset()
    fun dismissSearch()
    fun openFilter()
}