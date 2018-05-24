package ishopgo.com.exhibition.ui.chat.local.contact

import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ContactFragment : BaseListFragment<List<ContactProvider>, ContactProvider>() {
    override fun populateData(data: List<ContactProvider>) {
        if (reloadData)
            adapter.replaceAll(data)
        else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ContactProvider> {
        return ContactAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<ContactProvider>> {
        return obtainViewModel(ContactViewModel::class.java)
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = LoadMoreRequest()
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = LoadMoreRequest()
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

}