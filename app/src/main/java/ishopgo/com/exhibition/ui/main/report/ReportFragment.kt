package ishopgo.com.exhibition.ui.main.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.fragment_report.*

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class ReportFragment : BaseFragment() {

    companion object {

        fun newInstance(params: Bundle): ReportFragment {
            val fragment = ReportFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notice = "Hãy gửi cho chúng tôi mọi lỗi kỹ thuật & mọi bất tiện mà bạn gặp phải trong quá trình sử dụng hệ thống bằng cách tham gia vào <a href=\"https://www.facebook.com/groups/ishopgovn/\">nhóm ishopgo quản trị và khởi nghiệp trên facebook</a>, sau đó chụp hình mọi lỗi hoặc mọi bất tiện mà bạn gặp phải và miêu tả rõ cho chúng tôi biết yêu cầu hoặc mong muốn của bạn. \n" +
                "Đội ngũ kỹ thuật, đội ngũ tư vấn, và cộng đồng sử dụng ishopgo sẽ hỗ trợ cho bạn.\n" +
                "Khi tham gia vào cộng đồng bạn còn được trao đổi hợp tác tìm đôi tác làm ăn và khởi nghiệp, được chia sẻ và được nhận những chia sẻ bổ ích từ các chu doanh nghiệp và những người đã điều hành tốt doanh nghiệp của mình."
        view_notice.text = notice.asHtml()
        view_notice.movementMethod = LinkMovementMethod.getInstance()

        view_join_fp.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/ishopgovn/"))

            context?.let {
                if (i.resolveActivity(it.packageManager) != null) {
                    it.startActivity(i)
                }
            }
        }
    }

}