package ishopgo.com.exhibition.ui.community

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.scan.ScanFragmentActionBar
import kotlinx.android.synthetic.main.fragment_share_community.*
import android.widget.AdapterView
import android.widget.Toast
import ishopgo.com.exhibition.ui.main.MainActivity
import android.widget.ArrayAdapter


/**
 * Created by hoangnh on 4/26/2018.
 */
class CommunityShareFragment : BaseFragment() {
    private lateinit var viewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_share_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_back_share.setOnClickListener {
            activity?.finish()
        }

        img_select_camera.setOnClickListener {
            toast("Bạn phải quét sản phẩm trước khi sử dụng tính năng này")
        }

        img_select_picture.setOnClickListener {
            toast("Bạn phải quét sản phẩm trước khi sử dụng tính năng này")
        }

        img_select_scan_qr.setOnClickListener {
            toast("Mở Scan")
        }

        btn_share.setOnClickListener {
            if (checkRequireFields(edit_share.text.toString())) {
                showProgressDialog()
                viewModel.sentShareCommunity(edit_share.text.toString())
            }
        }

        Glide.with(this).load("http://www.urhobosocialclublagos.com/wp-content/uploads/2017/07/default-avatar-ginger-guy.png")
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_share_avatar)

        loadTypeShare()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(CommunityViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.sentShareSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Đăng thành công")
        })
    }

    private fun loadTypeShare() {
        val listTypeShare = mutableListOf<String>()
        listTypeShare.add("Chia sẻ với cộng đồng")
        listTypeShare.add("Trên tường nhà")
        listTypeShare.add("Trong nhóm")
        listTypeShare.add("Trên trang quản lý")

        val adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_item, listTypeShare)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        sp_share_community.adapter = adapter
        sp_share_community.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                Toast.makeText(context, sp_share_community.selectedItem.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    private fun checkRequireFields(share: String): Boolean {
        if (share.trim().isEmpty()) {
            toast("Nội dung quá ngắn hoặc chưa đầy đủ")
            return false
        }
        return true
    }
}