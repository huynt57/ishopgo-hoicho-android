package ishopgo.com.exhibition.ui.chat.local.profile

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.widget.BottomSheetOptionListener
import kotlinx.android.synthetic.main.bottom_sheet_config_member_profile.*

class MemberProfileOptions : BottomSheetDialogFragment() {

    companion object {
        private val TAG = "MemberProfileOptions"

        fun newInstance(data: Bundle): MemberProfileOptions {
            val fragment = MemberProfileOptions()
            fragment.arguments = data

            return fragment
        }

        const val OPTION_UPGRADE = 1
        const val OPTION_DELETE = 2
    }

    var optionSelectedListener: BottomSheetOptionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_config_member_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option_upgrade.setOnClickListener { dismiss(); optionSelectedListener?.click(OPTION_UPGRADE) }
        option_delete.setOnClickListener { dismiss(); optionSelectedListener?.click(OPTION_DELETE) }
        option_cancel.setOnClickListener { dismiss() }
    }
}