package ishopgo.com.exhibition.ui.chat.local.contact

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.MainViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_chat_contact.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ContactFragment : BaseListFragment<List<ContactProvider>, ContactProvider>() {

    private lateinit var mainViewModel: MainViewModel

    override fun populateData(data: List<ContactProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_contact, container, false)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ContactProvider> {
        val contactAdapter = ContactAdapter()
        contactAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ContactProvider> {

            override fun click(position: Int, data: ContactProvider, code: Int) {
                if (viewModel is ContactViewModel) {
                    val request = CreateConversationRequest()
                    request.type = 1
                    val members = mutableListOf<Long>()
                    members.add(UserDataManager.currentUserId)
                    if (data is IdentityData) {
                        members.add(data.id)
                    }
                    request.member = members
                    (viewModel as ContactViewModel).createConversation(request)
                }
            }

        }
        return contactAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

        view_search.setOnClickListener {
            mainViewModel.enableSearchContact()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is ContactViewModel) {
            (viewModel as ContactViewModel).conversation.observe(this, Observer { c ->
                c?.let {
                    val conv = LocalConversationItem()
                    conv.idConversions = c.id ?: ""
                    conv.name = c.name ?: ""
                    mainViewModel.openCurrentConversation(conv)
                }
            })
        }

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
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