package ishopgo.com.exhibition.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
abstract class BaseSingleFragmentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        if (savedInstanceState == null) {
            replaceFragmentInActivity(createFragment(startupOptions()), R.id.fragment_container)
        }
    }

    open fun startupOptions(): Bundle = Bundle()

    abstract fun createFragment(startupOption: Bundle): Fragment
}