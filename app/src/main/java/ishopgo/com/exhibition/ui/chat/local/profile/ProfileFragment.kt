package ishopgo.com.exhibition.ui.chat.local.profile

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Member
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.content_local_chat_profile.*
import kotlinx.android.synthetic.main.dialog_input_date.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.layout_container_date_care.*
import kotlinx.android.synthetic.main.layout_container_note.*

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class ProfileFragment : BaseActionBarFragment() {

    private lateinit var viewModel: ProfileViewModel
    private var noteAdapter = UserNoteAdapter()

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_profile
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.dateCare.observe(this, Observer { date ->
            date?.let {
                member_lich_chamsoc.text = if (it.isBlank()) "Chưa đặt" else it
            }
        })
        viewModel.notes.observe(this, Observer { notes ->
            notes?.let {
                noteAdapter.replaceAll(it)
            }
        })
        viewModel.userData.observe(this, Observer { info ->
            info?.let {
                showDetail(it)
            }
        })


        activity?.let {
            var memberId = it.intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
            if (memberId == -1L)
                it.finish()
            else {
                viewModel.loadNotes(memberId)
                viewModel.loadDateCare(memberId)
                viewModel.loadUserDetail(memberId)
            }
        }

    }

    private fun showDetail(user: Member) {
        Glide.with(view_member_avatar.context)
                .load(user.image)
                .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.avatar_placeholder))
                .into(view_member_avatar)

        member_detail_name.text = "Họ tên: <b>${user.name} </b>".asHtml()
        member_detail_phone.text = "SĐT: <b>${user.phone} </b>".asHtml()
        member_detail_email.text = "Email: <b>${user.email} </b>".asHtml()
        member_detail_dob.text = "Ngày sinh: <b>${user.birthday?.asDate()} </b>".asHtml()
        member_detail_ngay_thamgia.text = "Ngày tham gia: <b>${user.createdAt?.asDate()} </b>".asHtml()
        member_detail_chucdanh.text = "Chức danh: <b>${user.titleText} </b>".asHtml()
        member_detail_khuvuc.text = "Khu vực: <b>${user.region} </b>".asHtml()
        member_detail_company.text = "Công ty: <b>${user.company} </b>".asHtml()
        member_detail_tax_code.text = "Mã số thuế: <b>${user.taxCode} </b>".asHtml()
        member_detail_address.text = "Địa chỉ: <b>${user.address} </b>".asHtml()
        member_detail_name_phutrach.text = "Người phụ trách: <b>${user.managerName} </b>".asHtml()
        member_detail_phone_phutrach.text = "SĐT người phụ trách: <b>${user.managerPhone} </b>".asHtml()

        member_lich_chamsoc.text = "${user.dateCare}"
        member_lich_chamsoc.setOnClickListener {
            MaterialDialog.Builder(member_lich_chamsoc.context)
                    .title("Sửa ngày chăm sóc")
                    .customView(R.layout.dialog_input_date, false)
                    .positiveText("OK")
                    .onPositive { dialog, _ ->
                        if (dialog.input_date.isError || dialog.input_date.isEmpty) {
                            toast("Ngày chưa hợp lệ")
                        } else {
                            viewModel.updateDateCare(user.id, dialog.input_date.text.toString())
                        }
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Thông tin thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        activity?.let {
            val canCreateGroup = it.intent.getBooleanExtra(Const.TransferKey.EXTRA_ENABLE_CREATE_GROUP, true)
            if (canCreateGroup) {
                toolbar.rightButton(R.drawable.ic_group_add_green_24dp)
                toolbar.setRightButtonClickListener { toast("Tạo nhóm chat") }
            }
        }

        view_notes.adapter = noteAdapter
        val layoutManager = LinearLayoutManager(view_notes.context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_notes.layoutManager = layoutManager
        view_notes.isNestedScrollingEnabled = false
    }

}