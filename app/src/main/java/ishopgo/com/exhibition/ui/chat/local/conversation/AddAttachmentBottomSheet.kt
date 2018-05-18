package ishopgo.com.exhibition.ui.chat.local.conversation

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.bottom_sheet_add_attachment_options.*

/**
 * Created by xuanhong on 4/13/18. HappyCoding!
 */
class AddAttachmentBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private val TAG = "AddAttachmentBottomSheet"

        fun newInstance(data: Bundle): AddAttachmentBottomSheet {
            val fragment = AddAttachmentBottomSheet()
            fragment.arguments = data

            return fragment
        }

    }

    var chooseCamera: View.OnClickListener? = null
    var chooseGallery: View.OnClickListener? = null
    var chooseInventory: View.OnClickListener? = null
    var choosePattern: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_attachment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option_camera.setOnClickListener { chooseCamera?.onClick(it); dismiss() }
        option_gallery.setOnClickListener { chooseGallery?.onClick(it); dismiss() }
        option_inventory.setOnClickListener { chooseInventory?.onClick(it); dismiss() }
        option_pattern.setOnClickListener { choosePattern?.onClick(it); dismiss() }
    }

}