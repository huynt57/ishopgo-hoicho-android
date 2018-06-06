package ishopgo.com.exhibition.ui.main.questmanager

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.QuestionRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.question.QuestionCategory
import ishopgo.com.exhibition.model.question.QuestionObject
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.postmanager.detail.QuestionManagerDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class QuestionManagerProcessedFragment : BaseListFragment<List<QuestProvider>, QuestProvider>() {
    private val adapterCategory = QuestionManagerCategoryAdapter()
    private var reloadCategory = false
    private var key_search = ""
    private var categoryId: Long = 0
    private var categoryName = ""
    private lateinit var viewModelSearch: QuestionSearchViewModel


    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<QuestProvider>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<QuestProvider> {
        val adapter = QuestionManagerAdapter()
        adapter.addData(QuestionObject())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<QuestProvider>> {
        return obtainViewModel(QuestionViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = QuestionRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.category_id = categoryId
        firstLoad.key_search = key_search
        firstLoad.status = QUESTION_PROCESSED

        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = QuestionRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.category_id = categoryId
        loadMore.key_search = key_search
        loadMore.status = QUESTION_PROCESSED


        viewModel.loadData(loadMore)
    }

    fun firstLoadCategory() {
        reloadCategory = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        (viewModel as QuestionViewModel).loadCategory(firstLoad)
    }

    fun loadMoreCategory(currentCount: Int) {
        reloadCategory = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount

        (viewModel as QuestionViewModel).loadCategory(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<QuestProvider>) {
            (adapter as ClickableAdapter<QuestProvider>).listener = object : ClickableAdapter.BaseAdapterAction<QuestProvider> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: QuestProvider, code: Int) {
                    if (data is QuestionObject) {
                        val i = Intent(context, QuestionManagerDetailActivity::class.java)
                        i.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivity(i)
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelSearch = obtainViewModel(QuestionSearchViewModel::class.java, true)
        viewModelSearch.errorSignal.observe(this, Observer {
            it?.let { resolveError(it) }
        })

        (viewModel as QuestionViewModel).dataReturned.observe(this, Observer { p ->
            p.let {
                hideProgressDialog()
                if (reloadData) it?.let { it1 -> adapter.replaceAll(it1) }
                else it?.let { it1 -> adapter.addAll(it1) }

            }
        })


        (viewModel as QuestionViewModel).getCategorySusscess.observe(this, Observer { p ->
            p.let {
                if (reloadCategory) it?.let { it1 ->
                    adapterCategory.replaceAll(it1)
                    val category = QuestionCategory()
                    category.id = 0
                    category.name = "Tất cả danh mục"
                    adapterCategory.addData(0, category)
                }
                else it?.let { it1 -> adapterCategory.addAll(it1) }
            }
        })

        viewModelSearch.searchKey.observe(this, Observer {
            if (it == TAG)
                performSearching()
        })

        reloadCategory = true
        firstLoadCategory()
    }

    @SuppressLint("SetTextI18n")
    fun performSearching() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Tìm kiếm")
                    .customView(R.layout.dialog_search_question, false)
                    .positiveText("Lọc")
                    .onPositive { dialog, _ ->
                        val edit_post_name = dialog.findViewById(R.id.edit_post_name) as TextInputEditText
                        val edit_post_category = dialog.findViewById(R.id.edit_post_category) as TextInputEditText

                        key_search = edit_post_name.text.toString().trim { it <= ' ' }
                        categoryName = edit_post_category.text.toString().trim { it <= ' ' }

                        dialog.dismiss()

                        showProgressDialog()
                        firstLoad()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_post_name = dialog.findViewById(R.id.edit_post_name) as TextInputEditText
            val edit_post_category = dialog.findViewById(R.id.edit_post_category) as TextInputEditText
            edit_post_category.setOnClickListener { loadCategory(edit_post_category) }
            edit_post_name.setText(key_search)

            if (categoryName.isNotEmpty())
                edit_post_category.setText(categoryName)
            else edit_post_category.setText("Tất cả danh mục")

            dialog.show()
        }
    }

    private fun loadCategory(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn danh mục")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterCategory
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreCategory(totalItemsCount)
                }
            })
            adapterCategory.listener = object : ClickableAdapter.BaseAdapterAction<QuestionCategory> {
                override fun click(position: Int, data: QuestionCategory, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        categoryId = data.id
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    companion object {
        const val TAG = "QuestionManagerProcessedFragment"
        fun newInstance(params: Bundle): QuestionManagerProcessedFragment {
            val fragment = QuestionManagerProcessedFragment()
            fragment.arguments = params

            return fragment
        }

        const val QUESTION_PROCESSED = 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firstLoad()
    }
}