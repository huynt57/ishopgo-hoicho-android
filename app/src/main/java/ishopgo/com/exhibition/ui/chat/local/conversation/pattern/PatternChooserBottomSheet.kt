package ishopgo.com.exhibition.ui.chat.local.conversation.pattern

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppFactory
import ishopgo.com.exhibition.app.MyApp
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.bottom_sheet_pattern_chooser.*

/**
 * Created by xuanhong on 4/14/18. HappyCoding!
 */
class PatternChooserBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(data: Bundle): PatternChooserBottomSheet {
            val fragment = PatternChooserBottomSheet()
            fragment.arguments = data

            return fragment
        }

    }

    private lateinit var viewModel: PatternChooserViewModel
    private var patternAdapter = TextPatternAdapter()
    var patternChooserListener: PatternChooserListener? = null
    private var reloadData = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_pattern_chooser, container, false)
    }

    fun <T : ViewModel> obtainViewModel(viewModelClass: Class<T>, activityObserver: Boolean = true) =
            if (activityObserver && activity != null) ViewModelProviders.of(activity!!, AppFactory(activity!!.application as MyApp)).get(viewModelClass)
            else ViewModelProviders.of(this, AppFactory(activity?.application as MyApp)).get(viewModelClass)

    fun resolveError(error: BaseErrorSignal) {
        activity?.let {
            when (error.errorCode) {
                BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                    Toast.makeText(it, error.errorMessage, Toast.LENGTH_SHORT).show()
                }
                BaseErrorSignal.ERROR_401 -> {
                    Toast.makeText(it, "Xin vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()

                    it.finish()
                    it.finishAffinity()
                }
                BaseErrorSignal.ERROR_NETWORK -> {
                    val snackBar = Snackbar.make(it.findViewById(android.R.id.content), R.string.error_no_connection, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.WHITE)
                            .setDuration(10 * 1000)
                    val snackBarText = snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                    snackBarText.setTextColor(Color.WHITE)
                    snackBar.show()
                }
                BaseErrorSignal.ERROR_SERVER -> {
                    MaterialDialog.Builder(it)
                            .content("Hệ thống đang được cập nhật. Vui lòng khởi động lại hoặc quản lại sau.")
                            .positiveText("OK")
                            .show()
                }
                BaseErrorSignal.ERROR_UNKNOWN -> {
                    MaterialDialog.Builder(it)
                            .content("Có lỗi xảy ra. Liên lạc với nhà quản trị để được hỗ trợ.")
                            .positiveText("OK")
                            .show()
                }
                else -> {
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(PatternChooserViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.patterns.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    patternAdapter.replaceAll(it)
                } else {
                    patternAdapter.addAll(it)
                }

                view_empty_notice.visibility = if (patternAdapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        })

        reloadData = true
        viewModel.loadPatterns(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = patternAdapter
        recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                reloadData = false
                viewModel.loadPatterns(totalItemsCount)
            }
        })

        view_add_pattern.setOnClickListener {
            dismiss()
            patternChooserListener?.addPattern()
        }

        patternAdapter.listener = object : ClickableAdapter.BaseAdapterAction<TextPattern> {
            override fun click(position: Int, data: TextPattern, code: Int) {
                when (code) {
                    TextPatternAdapter.CODE_CLICK -> {
                        dismiss()
                        patternChooserListener?.submitPattern(data)
                    }
                    TextPatternAdapter.CODE_EDIT -> {
                        dismiss()
                        patternChooserListener?.editPattern(data)
                    }
                    TextPatternAdapter.CODE_REMOVE -> {
                        dismiss()
                        patternChooserListener?.removePattern(data)
                    }
                    else -> {
                    }
                }
            }

        }
    }

    interface PatternChooserListener {
        fun submitPattern(pattern: TextPattern)
        fun addPattern()
        fun editPattern(pattern: TextPattern)
        fun removePattern(pattern: TextPattern)
    }

}