package ishopgo.com.exhibition.ui.main.administrator.add

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.model.administrator.AdministratorRole
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.administrator.AdministratorViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_administrator_add.*

class AdministratorAddFragment : BaseFragment() {

    companion object {
        const val TAG = "AdministratorAddFragment"
        fun newInstance(params: Bundle): AdministratorAddFragment {
            val fragment = AdministratorAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var data: Administrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, Administrator::class.java)
    }

    private lateinit var viewModel: AdministratorViewModel
    private lateinit var memberViewModel: FragmentAdministratorViewModel
    private var adapter = AdministratorPermissionsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_administrator_add, container, false)
    }

    private val listRole = ArrayList<AdministratorRole>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.setHasFixedSize(true)
        view_recyclerview.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        layoutManager.isAutoMeasureEnabled = true

        adapter.onClickListener = object : ClickableAdapter.BaseAdapterAction<Administrator>, AdministratorPermissionsAdapter.onClick {
            override fun clickRole(item: AdministratorRole) {
                if (listRole.isEmpty())
                    listRole.add(item)
                else for (i in listRole.indices) {
                    if (listRole[i].key == item.key) {
                        listRole.removeAt(i)
                        break
                    } else if (i == listRole.size - 1)
                        listRole.add(item)
                }
            }

            override fun click(position: Int, data: Administrator, code: Int) {

            }

        }
        view_recyclerview.adapter = adapter

        if (data == null) {
            edit_member_name.setOnClickListener { memberViewModel.showFragmentMember() }
            edit_member_phone.setOnClickListener { memberViewModel.showFragmentMember() }
            btn_add_administrator.setOnClickListener {
                if (checkRequireFields(edit_member_name.text.toString(), edit_member_phone.text.toString())) {
                    viewModel.addAdministrator(listRole, edit_member_phone.text.toString())
                }
            }
        } else {
            edit_member_name.setText(data?.name)
            edit_member_phone.setText(data?.phone)
            btn_add_administrator.text = "Cập nhật quản trị viên"
            btn_add_administrator.setOnClickListener {
                if (checkRequireFields(edit_member_name.text.toString(), edit_member_phone.text.toString())) {
                    data?.id?.let { it1 -> viewModel.editAdministrator(listRole, it1) }
                }
            }
        }
    }

    fun deleleAdministrator() {
        context?.let {
            MaterialDialog.Builder(it)
                    .content("Bạn có muốn xoá thành viên này không?")
                    .positiveText("Có")
                    .onPositive { _, _ ->
                        activity?.let {
                            data?.id?.let { it1 -> viewModel.deleteAdministrator(it1) }
                        }
                        showProgressDialog()
                    }
                    .negativeText("Không")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .show()
        }
    }

    private fun checkRequireFields(name: String, phone: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tên không được để trống")
            edit_member_name.error = getString(R.string.error_field_required)
            edit_member_name.requestFocus()
            return false
        }

        if (phone.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            edit_member_phone.error = getString(R.string.error_field_required)
            edit_member_phone.requestFocus()
            return false
        }

        if (listRole.isEmpty()) {
            toast("Bạn vui lòng chọn quyền cho thành viên này")
            return false
        }
        return true
    }

    private var listPermission = mutableListOf<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        memberViewModel = obtainViewModel(FragmentAdministratorViewModel::class.java, true)
        memberViewModel.getDataMember.observe(this, Observer { p ->
            p?.let {
                edit_member_name.setText(it.name)
                edit_member_phone.setText(it.phone)
            }
        })
        viewModel = obtainViewModel(AdministratorViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getDataPermissions.observe(this, Observer { p ->
            p?.let {
                listPermission = it
                for (i in it.indices) {
                    val administratorRole = AdministratorRole()
                    administratorRole.key = it[i]
                    listRole.add(administratorRole)
                }
            }
        })

        viewModel.dataAdministratorPermissions.observe(this, Observer { p ->
            p?.let {
                view_recyclerview.scheduleLayoutAnimation()
                if (data == null)
                    adapter.replaceAll(it)
                else {
                    for (i in it.indices) {
                        for (j in listPermission.indices) {
                            if (it[i].subMenu?.isNotEmpty() == true) {

                                val subMenu = it[i].subMenu!!

                                for (k in subMenu.indices)
                                    if (subMenu[k].role?.isNotEmpty() == true) {

                                        val role = subMenu[k].role!!

                                        for (s in role.indices)
                                            if (role[s].key == listPermission[j]) {
                                                role[s].isSelected = true
                                                subMenu[k].isSelected = true
                                                it[i].isSelected = true
                                            }
                                    }

                            } else if (it[i].role?.isNotEmpty() == true) {

                                val role = it[i].role!!

                                for (s in role.indices)
                                    if (role[s].key == listPermission[j]) {
                                        role[s].isSelected = true
                                        it[i].isSelected = true
                                    }
                            }
                        }
                    }
                    adapter.replaceAll(it)
                }
            }
        })

        viewModel.createSusscess.observe(this, Observer
        {
            toast("Thêm quản trị viên thành công")
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })

        viewModel.editSusscess.observe(this, Observer
        {
            toast("Cập nhật quản trị viên thành công")
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        })

        viewModel.deleteSusscess.observe(this, Observer
        {
            hideProgressDialog()
            toast("Xoá thành công")
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        })
        if (data != null)
            data?.id?.let { viewModel.getPermisions(it) }

        viewModel.getAdministratorPermissions()

    }
}