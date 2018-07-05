package ishopgo.com.exhibition.ui.main.map.config


import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ExposRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.map.ExpoDetailActivity
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.content_expo_calendar.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ExpoMapConfigFragment : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.content_expo_calendar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_pager.offscreenPageLimit = 3
        view_pager.adapter = ExpoMapConfigAdapter(childFragmentManager)
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Lịch hội chợ")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
        if (UserDataManager.currentType == "Chủ hội chợ") {
            toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
            toolbar.setRightButtonClickListener {
                openAddExpoActivity()
            }
        } else if (UserDataManager.currentType == "Quản trị viên") {
            val listPermission = Toolbox.gson.fromJson<ArrayList<String>>(UserDataManager.listPermission, object : TypeToken<ArrayList<String>>() {}.type)

            if (listPermission.isNotEmpty())
                for (i in listPermission.indices)
                    if (Const.Permission.EXPO_FAIR_ADD == listPermission[i]) {
                        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
                        toolbar.setRightButtonClickListener {
                            openAddExpoActivity()
                        }
                        break
                    }
        }

    }

    private fun openAddExpoActivity() {
        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment)
                .navigate(R.id.action_expoMapConfigFragment_to_expoAddFragmentActionBar)
    }

    companion object {
        private val TAG = "ExpoMapConfigFragment"

        @JvmStatic
        fun newInstance(arg: Bundle) =
                ExpoMapConfigFragment().apply {
                    arguments = arg
                }
    }

    inner class ExpoMapConfigAdapter(f: FragmentManager) : CountSpecificPager(f, 3) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ExposFragment.newInstance(ExposRequest.TYPE_COMPLETED)
                }
                1 -> {
                    ExposFragment.newInstance(ExposRequest.TYPE_CURRENT)
                }
                2 -> {
                    ExposFragment.newInstance(ExposRequest.TYPE_GOING)
                }
                else -> {
                    Fragment()
                }
            }
        }

    }

    class ExposFragment : BaseListFragment<List<ExpoConfig>, ExpoConfig>() {

        companion object {
            fun newInstance(type: Int): ExposFragment {
                val f = ExposFragment()
                val extra = Bundle()
                extra.putInt("type", type)

                f.arguments = extra
                return f
            }
        }

        private var currentType = ExposRequest.TYPE_COMPLETED

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            currentType = arguments?.getInt("type", ExposRequest.TYPE_CURRENT) ?: ExposRequest.TYPE_CURRENT
        }

        override fun populateData(data: List<ExpoConfig>) {
            if (reloadData) {
                adapter.replaceAll(data)

                if (data.isEmpty()) {
                    view_empty_result_notice.visibility = View.VISIBLE
                    view_empty_result_notice.text = "Không có hội chợ nào"
                } else
                    view_empty_result_notice.visibility = View.GONE
            } else
                adapter.addAll(data)
        }

        override fun itemAdapter(): BaseRecyclerViewAdapter<ExpoConfig> {
            val expoAdapter = ExpoConfigAdapter()
            expoAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ExpoConfig> {
                override fun click(position: Int, data: ExpoConfig, code: Int) {
                    when (code) {
                        ExpoConfigAdapter.CLICK_SETTING -> {
                            showSettings(data)
                        }
                        ExpoConfigAdapter.CLICK_DETAIL -> {
                            val intent = Intent(requireContext(), ExpoDetailActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                            startActivity(intent)
                        }
                    }

                }

            }
            return expoAdapter
        }

        private fun showSettings(config: ExpoConfig) {
            val fragment = ExpoConfigBottomSheet.newInstance(Bundle())
            fragment.chooseEdit = View.OnClickListener { editExpo(config) }
            fragment.chooseSetting = View.OnClickListener { settingExpo(config) }
            fragment.chooseDelete = View.OnClickListener { deleteExpo(config) }
            fragment.show(childFragmentManager, "ExpoConfigBottomSheet")
        }

        private fun deleteExpo(config: ExpoConfig) {
            MaterialDialog.Builder(requireContext())
                    .title("Xác nhận")
                    .content("Xoá hội chợ ${config.name} ?")
                    .positiveText("OK")
                    .onPositive { _, _ -> removeExpo(config.id) }
                    .negativeText("Huỷ")
                    .show()
        }

        private fun removeExpo(id: Long?) {
            if (viewModel is ExpoConfigViewModel) {
                id?.let {
                    (viewModel as ExpoConfigViewModel).removeExpo(it)
                }
            }
        }

        private val removeSuccessObserver = Observer<Boolean> {
            toast("Xoá thành công")
            firstLoad()
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            Log.d(TAG, "onActivityCreated: savedInstanceState = [${savedInstanceState}]")

            if (viewModel is ExpoConfigViewModel) {
                (viewModel as ExpoConfigViewModel).removeSuccess.observe(this, removeSuccessObserver)
            }
        }

        private fun settingExpo(config: ExpoConfig) {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(config))
            Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment)
                    .navigate(R.id.action_expoMapConfigFragment_to_expoSettingFragmentActionBar, extra)
        }

        private fun editExpo(config: ExpoConfig) {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(config))
            Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment)
                    .navigate(R.id.action_expoMapConfigFragment_to_expoEditFragmentActionBar, extra)
        }

        override fun obtainViewModel(): BaseListViewModel<List<ExpoConfig>> {
            return obtainViewModel(ExpoConfigViewModel::class.java, false)
        }

        override fun firstLoad() {
            super.firstLoad()
            val request = ExposRequest()
            request.limit = Const.PAGE_LIMIT
            request.offset = 0
            request.time = currentType
            viewModel.loadData(request)
        }

        override fun loadMore(currentCount: Int) {
            super.loadMore(currentCount)
            val request = ExposRequest()
            request.limit = Const.PAGE_LIMIT
            request.offset = currentCount
            request.time = currentType
            viewModel.loadData(request)
        }
    }
}
