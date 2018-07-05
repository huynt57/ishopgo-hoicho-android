package ishopgo.com.exhibition.ui.main.map.config


import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.map.ExpoMapActivity
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ExpoMapConfigFragment : BaseListActionBarFragment<List<ExpoConfig>, ExpoConfig>() {

    override fun populateData(data: List<ExpoConfig>) {
        if (reloadData) {
            adapter.replaceAll(data)
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
                        val intent = Intent(requireContext(), ExpoMapActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
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
        val request = LoadMoreRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = LoadMoreRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        viewModel.loadData(request)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view = [${view}], savedInstanceState = [${savedInstanceState}]")

        setupToolbar()
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
}
