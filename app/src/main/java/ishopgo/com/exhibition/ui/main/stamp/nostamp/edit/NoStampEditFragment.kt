package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_no_stamp_edit.*

class NoStampEditFragment : BaseFragment() {
    private lateinit var adapter: TrackingAdapter
    private lateinit var item: MutableList<Tracking>
    private var stampId = 0L
    private lateinit var viewModel: NoStampViewModel

    companion object {

        fun newInstance(params: Bundle): NoStampEditFragment {
            val fragment = NoStampEditFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stampId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_edit, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_edit_no_stamp.setOnClickListener {
            //                viewModel.editNoStampDetail(stampId, edit_no_stamp_name.text.toString(), edit_no_stamp_count.text.toString())
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_tracking.layoutManager = layoutManager
        layoutManager.isAutoMeasureEnabled = true
        rv_tracking.isNestedScrollingEnabled = false
        rv_tracking.setHasFixedSize(false)
        rv_tracking.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(NoStampViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getDataNoStampDetail.observe(this, Observer { p ->
            p?.let { it ->
                edit_no_stamp_name.setText(it.code ?: "")
                edit_no_stamp_count.setText("${it.limitedAccess ?: 0}")
                item = it.trackings as MutableList<Tracking>
                val tracking = Tracking()
                item.add(item.size, tracking)

                adapter = TrackingAdapter(item, childFragmentManager)
                rv_tracking.adapter = adapter
                adapter.replaceAll(item)

            }
        })

        viewModel.editNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.getDetailNoStamp(stampId)
    }
}