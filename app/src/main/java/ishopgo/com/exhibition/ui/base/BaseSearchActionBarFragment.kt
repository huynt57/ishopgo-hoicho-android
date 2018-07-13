package ishopgo.com.exhibition.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.extensions.observable
import ishopgo.com.exhibition.ui.extensions.showSoftKeyboard
import ishopgo.com.exhibition.ui.widget.VectorSupportEditText
import kotlinx.android.synthetic.main.base_fragment_search.*
import kotlinx.android.synthetic.main.base_fragment_search.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
abstract class BaseSearchActionBarFragment : BaseFragment(), ContentDescription, SearchHandler {

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.base_fragment_search, container, false)

        val contentLayoutRes = contentLayoutRes()
        if (contentLayoutRes != 0) {
            inflater.inflate(contentLayoutRes, view.view_content, true)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(view_search_field.observable()
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { triggerSearch(it) })

        view_back.setOnClickListener {
            view_search_field.hideKeyboard()
            dismissSearch()
        }
        view_cancel.setOnClickListener {
            view_search_field.hideKeyboard()

            val currentKeyword = view_search_field.text.toString()
            if (currentKeyword.isEmpty())
                dismissSearch()
            else {
                view_search_field.setText("")
                searchReset()
            }
        }

        view_search_field.showSoftKeyboard()
    }

    override fun onDetach() {
        super.onDetach()
        disposables.dispose()
    }

    open fun getSearchField(): VectorSupportEditText {
        return view_search_field
    }
}