package ishopgo.com.exhibition.ui.main.map.config.add

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoAddFragmentActionBar : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    companion object {

        fun newInstance(params: Bundle): ExpoAddFragmentActionBar {
            val fragment = ExpoAddFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm hội chợ")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            hideKeyboard()
            Navigation.findNavController(it).navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ExpoAddFragment())
                .commit()
    }


}