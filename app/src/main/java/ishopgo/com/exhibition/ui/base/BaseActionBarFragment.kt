package ishopgo.com.exhibition.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.fragment_base_actionbar.view.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
abstract class BaseActionBarFragment : BaseFragment(), ContentDescription {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base_actionbar, container, false)

        val contentLayoutRes = contentLayoutRes()
        if (contentLayoutRes != 0) {
            inflater.inflate(contentLayoutRes, view.content, true)
        }

        return view
    }

}