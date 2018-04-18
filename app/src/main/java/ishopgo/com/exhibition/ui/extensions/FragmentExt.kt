package ishopgo.com.exhibition.ui.extensions

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */

/**
 * Runs a FragmentTransaction, then calls commit().
 */
fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}
