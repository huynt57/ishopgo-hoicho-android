package ishopgo.com.exhibition.ui.main.registerbooth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asPhone
import kotlinx.android.synthetic.main.fragment_contact_to_register_booth.*

/**
 * Created by xuanhong on 6/19/18. HappyCoding!
 */
class ContactToRegisterBoothFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact_to_register_booth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_caption.setText("Để đăng ký gian hàng hội chợ, vui lòng liên hệ hotline <font color=\"#009624\">${"0985771133".asPhone()}</font>".asHtml())
    }
}