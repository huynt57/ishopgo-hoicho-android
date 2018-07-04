package ishopgo.com.exhibition.ui.main.administrator.add

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.model.administrator.AdministratorRole
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
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

    private lateinit var viewModel: AdministratorViewModel
    private lateinit var memberViewModel: FragmentAdministratorViewModel
    private var adapter = AdministratorPermissionsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_administrator_add, container, false)
    }

    private val listRole = ArrayList<AdministratorRole>()

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

        edit_member_name.setOnClickListener { memberViewModel.showFragmentMember() }
        edit_member_phone.setOnClickListener { memberViewModel.showFragmentMember() }

        btn_add_administrator.setOnClickListener {
            if (checkRequireFields(edit_member_name.text.toString(), edit_member_phone.text.toString())) {
                viewModel.addAdministrator(listRole, edit_member_phone.text.toString())
            }
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
        viewModel.dataAdministratorPermissions.observe(this, Observer {
            view_recyclerview.scheduleLayoutAnimation()
            it?.let { it1 -> adapter.replaceAll(it1) }
        })

        viewModel.createSusscess.observe(this, Observer {
            toast("Thêm quản trị viên thành công")
            activity?.setResult(RESULT_OK)
            activity?.onBackPressed()
        })
        viewModel.getAdministratorPermissions()
    }
}