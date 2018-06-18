package ishopgo.com.exhibition.ui.main.map.config


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
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
                val intent = Intent(requireContext(), ExpoMapActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivity(intent)
            }

        }
        return expoAdapter
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

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Danh sách hội chợ")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
        if (UserDataManager.currentType == "Chủ hội chợ") {
            toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
            toolbar.setRightButtonClickListener {
                openAddExpoActivity()
            }
        }

    }

    private fun openAddExpoActivity() {
        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment)
                .navigate(R.id.action_expoMapConfigFragment_to_expoAddFragmentActionBar)
    }

    companion object {
        @JvmStatic
        fun newInstance(arg: Bundle) =
                ExpoMapConfigFragment().apply {
                    arguments = arg
                }

    }
}
