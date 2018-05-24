package ishopgo.com.exhibition.ui.chat.local

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ChatFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): ChatFragmentActionBar {
            val fragment = ChatFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ChatFragment.newInstance(arguments ?: Bundle()))
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tin nhắn")
    }


}