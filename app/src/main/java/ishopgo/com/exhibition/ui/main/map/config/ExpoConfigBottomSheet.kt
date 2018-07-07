package ishopgo.com.exhibition.ui.main.map.config

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.extensions.Toolbox
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

        if (UserDataManager.currentType == "Quản trị viên") {
            val listPermission = Const.listPermission

            if (listPermission.isNotEmpty()) {
                for (i in listPermission.indices)
                    if (Const.Permission.EXPO_FAIR_EDIT == listPermission[i]) {
                        option_edit.visibility = View.VISIBLE
                        break
                    } else option_edit.visibility = View.GONE

                for (i in listPermission.indices)
                    if (Const.Permission.EXPO_FAIR_SETUP == listPermission[i]) {
                        option_setting.visibility = View.VISIBLE
                        break
                    } else option_setting.visibility = View.GONE

                for (i in listPermission.indices)
                    if (Const.Permission.EXPO_FAIR_DELETE == listPermission[i]) {
                        option_delete.visibility = View.VISIBLE
                        break
                    } else option_delete.visibility = View.GONE
            }
        }

        option_edit.setOnClickListener { dismiss(); chooseEdit?.onClick(it) }
        option_setting.setOnClickListener { dismiss(); chooseSetting?.onClick(it) }
        option_delete.setOnClickListener { dismiss(); chooseDelete?.onClick(it) }
    }

}