package ishopgo.com.exhibition.ui.main.administrator.add

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.administrator.AdministratorViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_administrator_add.*

class AdministratorAddFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        viewModel.getAdministratorPermissions()
    }

    companion object {
        const val TAG = "AdministratorAddFragment"
        fun newInstance(params: Bundle): AdministratorAddFragment {
            val fragment = AdministratorAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: AdministratorViewModel
    private var adapter = AdministratorPermissionsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_administrator_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager

        edit_member_name.setOnClickListener { }
        edit_member_phone.setOnClickListener { }
        swipe.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            swipe.isRefreshing = false
        })
        swipe.isRefreshing = true
        viewModel.getAdministratorPermissions()
    }
}