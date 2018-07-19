package ishopgo.com.exhibition.ui.main.references

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 7/18/18. HappyCoding!
 */
class ReferencesFragmentActionBar : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ReferencesFragment())
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thành viên đã giới thiệu")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }
    }
}