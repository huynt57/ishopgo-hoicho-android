package ishopgo.com.exhibition.ui.main.ticket

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class MyTicketFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): MyTicketFragmentActionBar {
            val fragment = MyTicketFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Vé tham quan của tôi")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
//                .replace(R.id.view_main_content, TicketFragment.newInstance(Bundle()))
                .replace(R.id.view_main_content, MyTicketsFragment())
                .commit()
    }

    class MyTicketsFragment : BaseListFragment<List<Ticket>, Ticket>() {
        override fun populateData(data: List<Ticket>) {
            if (reloadData)
                adapter.replaceAll(data)
            else
                adapter.addAll(data)
        }

        override fun itemAdapter(): BaseRecyclerViewAdapter<Ticket> {
            val adapter = TicketAdapter()
            adapter.listener = object : ClickableAdapter.BaseAdapterAction<Ticket> {
                override fun click(position: Int, data: Ticket, code: Int) {
                    val extra = Bundle()
                    extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                    Navigation
                            .findNavController(requireActivity(), R.id.nav_map_host_fragment)
                            .navigate(R.id.action_ticketFragmentActionBar_to_ticketDetailFragmentActionBar, extra)
                }


            }
            return adapter
        }

        override fun firstLoad() {
            super.firstLoad()

            viewModel.loadData(Request())
        }

        override fun loadMore(currentCount: Int) {
            // do not support load more
        }

        override fun obtainViewModel(): BaseListViewModel<List<Ticket>> {
            return obtainViewModel(TicketViewModel::class.java, false)
        }


    }
}