package ishopgo.com.exhibition.ui.chat.local.config.notification

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.bottom_sheet_config_notification_options.*

/**
 * Created by xuanhong on 4/7/18. HappyCoding!
 */
class NotificationConfigFragment : BottomSheetDialogFragment() {

    companion object {
        private val TAG = "NotificationConfigFragment"

        fun newInstance(data: Bundle): NotificationConfigFragment {
            val fragment = NotificationConfigFragment()
            fragment.arguments = data

            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_config_notification_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option_turn_on.setOnClickListener { dismiss(); toast("Chưa khả dụng") }
        option_turn_off_for15.setOnClickListener { dismiss(); toast("Chưa khả dụng") }
        option_turn_off_for30.setOnClickListener { dismiss(); toast("Chưa khả dụng") }
        option_turn_off_for60.setOnClickListener { dismiss(); toast("Chưa khả dụng") }
        option_turn_off_for8x60.setOnClickListener { dismiss(); toast("Chưa khả dụng") }
        option_turn_off_for_next_turnon.setOnClickListener { dismiss(); toast("Chưa khả dụng") }
    }

    private fun toast(msg: String) {
        context?.let {
            Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
        }

    }

}