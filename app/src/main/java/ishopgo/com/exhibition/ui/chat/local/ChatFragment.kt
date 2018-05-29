package ishopgo.com.exhibition.ui.chat.local

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.chat.local.contact.ContactFragment
import ishopgo.com.exhibition.ui.chat.local.inbox.InboxFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ChatFragment : BaseFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        return false
    }

    private lateinit var viewModel: ChatViewModel

    companion object {
        fun newInstance(params: Bundle): ChatFragment {
            val f = ChatFragment()
            f.arguments = params

            return f
        }
    }

    private var mSectionsPagerAdapter: CountSpecificPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ChatViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { it?.let { resolveError(it) } })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.let {
            mSectionsPagerAdapter = LocalPagerAdapter(it)
            view_pager.setPagingEnabled(true)
            view_pager.offscreenPageLimit = view_tab_layout.tabCount
            view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
            view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))

            view_pager.adapter = mSectionsPagerAdapter

        }
    }

    inner class LocalPagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 2) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> InboxFragment()
                1 -> ContactFragment()
                else -> Fragment()
            }
        }
    }

}