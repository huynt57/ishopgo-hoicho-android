package ishopgo.com.exhibition.ui.main.administrator

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.bottom_sheet_administrator.*

class AdministratorBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private val TAG = "AdministratorBottomSheet"

        fun newInstance(data: Bundle): AdministratorBottomSheet {
            val fragment = AdministratorBottomSheet()
            fragment.arguments = data

            return fragment
        }

    }

    var chooseEdit: View.OnClickListener? = null
    var chooseCancel: View.OnClickListener? = null
    var chooseDelete: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_administrator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option_edit.setOnClickListener { dismiss(); chooseEdit?.onClick(it) }
        option_cancel.setOnClickListener { dismiss(); chooseCancel?.onClick(it) }
        option_delete.setOnClickListener { dismiss(); chooseDelete?.onClick(it) }
    }

}