package ishopgo.com.exhibition.ui.main.map.config

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.bottom_sheet_config_expo.*

/**
 * Created by xuanhong on 6/20/18. HappyCoding!
 */
class ExpoConfigBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private val TAG = "ExpoConfigBottomSheet"

        fun newInstance(data: Bundle): ExpoConfigBottomSheet {
            val fragment = ExpoConfigBottomSheet()
            fragment.arguments = data

            return fragment
        }

    }

    var chooseEdit: View.OnClickListener? = null
    var chooseSetting: View.OnClickListener? = null
    var chooseDelete: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_config_expo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option_edit.setOnClickListener { dismiss(); chooseEdit?.onClick(it) }
        option_setting.setOnClickListener { dismiss(); chooseSetting?.onClick(it) }
        option_delete.setOnClickListener { dismiss(); chooseDelete?.onClick(it) }
    }

}