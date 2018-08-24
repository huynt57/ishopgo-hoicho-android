package ishopgo.com.exhibition.ui.main.registerbooth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
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

        view_caption.setPhone(("Chức năng này chỉ dành cho chủ gian hàng sỉ là những người đủ dù một trong các điều kiện sau:<br>" +
                "- Đơn vị muốn làm truy xuất nguồn gốc hàng hoá<br>" +
                "- Đơn vị phân phối độc quyền sản phẩm<br>" +
                "- Đơn vị nhập khẩu độc quyền<br>" +
                "- Đơn vị sản xuất hàng hoá<br>" +
                "- Đơn vị chế biến sản phẩm<br>" +
                "- Đơn vị nuôi trồng nông nghiệp nông - lâm - ngư nghiệp như hợp tác xã, trang trại, hộ nông dân...<br>" +
                "- Đơn vị đăng ký gian hàng hội chợ triển lãm<br>" +
                "<br>" +
                "Nếu bạn có một trong các điều kiện trên hãng chuẩn bị giấy tờ liên quan để chứng minh và liên hệ 0985771133 để được nâng cấp thành gian hàng").asHtml(), "0985771133")
    }
}