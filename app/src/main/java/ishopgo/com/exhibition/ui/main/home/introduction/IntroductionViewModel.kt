package ishopgo.com.exhibition.ui.main.home.introduction

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 6/23/18. HappyCoding!
 */
class IntroductionViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var introduction = MutableLiveData<String>()

    fun getIntroduction() {
        val fixedIntro = "" +
                "<strong>Để tạo điều kiện cho c&aacute;c doanh nghiệp (DN) đạt Nh&atilde;n hiệu chứng nhận H&agrave;ng Việt Nam chất lượng cao 2014, sử dụng logo H&agrave;ng Việt Nam chất lượng cao (HVNCLC) theo đ&uacute;ng quy định hiện h&agrave;nh, Hội đ&atilde; gởi hợp đồng đến c&aacute;c DN đ&atilde; đăng k&yacute; sử dụng logo HVNCLC 2014.</strong>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "\n" +
                "<div style=\"text-align: center;\"><a href=\"http://congthongtinhvnclc.vn/uploadwb/hinhanh/19573201331915_Logo%20HVNCLC.jpg\"><img alt=\"\" src=\"http://congthongtinhvnclc.vn/uploadwb/hinhanh/19573201331915_Logo%20HVNCLC.jpg\" style=\"height: 450px; width: 600px;\" /></a></div>\n" +
                "\n" +
                "<div>&nbsp;</div>\n" +
                "\n" +
                "<div>T&iacute;nh đến ng&agrave;y 31/03/2014, Hội đ&atilde; ch&iacute;nh thức k&yacute; 292 hợp đồng do doanh nghiệp đ&atilde; k&yacute; v&agrave; gửi lại.</div>\n" +
                "\n" +
                "<div>&nbsp;</div>\n" +
                "\n" +
                "<div>DN chưa k&yacute; v&agrave; gửi lại hợp đồng cho Hội, xin vui l&ograve;ng khẩn trương ho&agrave;n tất thủ tục trước ng&agrave;y 15/04/2014. Sau thời hạn kể tr&ecirc;n, c&aacute;c DN kh&ocirc;ng k&yacute;, gửi lại hợp đồng sử dụng logo cho hội kể như l&agrave; chưa ho&agrave;n tất thủ tục sử dụng logo HVNCLC.</div>\n" +
                "\n" +
                "<div>&nbsp;</div>\n" +
                "\n" +
                "<div>Mọi chi tiết xin vui l&ograve;ng li&ecirc;n hệ:</div>\n" +
                "\n" +
                "<div><strong>Ban H&agrave;ng Việt_ HỘI DOANH NGHIỆP HVNCLC</strong></div>\n" +
                "\n" +
                "<div><em>Địa chỉ: 72/5F Trần Quốc Toản, phường 8, Quận 3, TP. Hồ Ch&iacute; Minh.</em></div>\n" +
                "\n" +
                "<div><em>Điện thoại: (08) 38202797&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Fax: (08) 38207978.</em></div>\n" +
                "\n" +
                "<div><em>Người li&ecirc;n hệ: anh Phượng &nbsp;Điện thoại: 0909 543 643 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</em></div>\n" +
                "\n" +
                "<div><em>Email:&nbsp;<a href=\"mailto:hoso.hvnclc@bsa.org.vn\">hoso.hvnclc@bsa.org.vn</a></em></div>\n" +
                "\n" +
                "<div>&nbsp;</div>\n" +
                "\n" +
                "<div>Tr&acirc;n trọng th&ocirc;ng b&aacute;o đến Qu&yacute; Doanh nghiệp.</div>\n" +
                "\n" +
                "<div>&nbsp;</div>\n" +
                "\n" +
                "<div align=\"right\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Đại diện Hội doanh nghiệp HVNCLC</strong></div>\n" +
                "\n" +
                "<div align=\"right\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ban H&agrave;ng Việt</strong></div>\n" +
                "\n" +
                "<p>&nbsp;</p>\n"


        introduction.postValue(fixedIntro)
    }
}