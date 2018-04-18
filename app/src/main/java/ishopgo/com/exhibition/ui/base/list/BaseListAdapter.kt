package ishopgo.com.exhibition.ui.base.list

import ishopgo.com.exhibition.ui.base.BaseRecyclerViewAdapter

/**
 * Created by xuanhong on 3/16/18. HappyCoding!
 */
abstract class BaseListAdapter<T> : BaseRecyclerViewAdapter<T>() {

    var listener: BaseAdapterAction<T>? = null

    interface BaseAdapterAction<T> {
        /*
            click event to an item, can distinguish event by code
         */
        fun click(position: Int, data: T, code: Int = -1)
    }

}