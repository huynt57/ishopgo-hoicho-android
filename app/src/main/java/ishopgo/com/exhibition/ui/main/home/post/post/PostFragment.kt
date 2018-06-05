package ishopgo.com.exhibition.ui.main.home.post.post

import android.annotation.SuppressLint
import android.app.Activity
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
import ishopgo.com.exhibition.domain.request.PostCategoryRequest
import ishopgo.com.exhibition.domain.request.PostRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.postmanager.PostManagerAdapter
import ishopgo.com.exhibition.ui.main.home.post.post.detail.PostMenuDetailActivity
import ishopgo.com.exhibition.ui.main.postmanager.PostManagerCategoryAdapter
import ishopgo.com.exhibition.ui.main.postmanager.PostProvider
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class PostFragment : BaseListFragment<List<PostProvider>, PostProvider>() {
    private var typeManager = 0
    private val adapterCategory = PostManagerCategoryAdapter()
    private var reloadCategory = false
    private var name = ""
    private var categoryId: Long = 0
    private var categoryName = ""
    override fun populateData(data: List<PostProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<PostProvider> {
        val adapter = PostManagerAdapter()
        adapter.addData(PostObject())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<PostProvider>> {
        return obtainViewModel(PostMenuViewModel::class.java, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeManager = arguments?.getInt(Const.TransferKey.EXTRA_REQUIRE, 0) ?: 0
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = PostRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        if (typeManager == Const.AccountAction.ACTION_NEWS_MANAGER)
            firstLoad.type = NEWS_MANAGER
        if (typeManager == Const.AccountAction.ACTION_GENEREL_MANAGER)
            firstLoad.type = GENERAL_MANAGER

        firstLoad.category_id = categoryId
        firstLoad.name = name
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = PostRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        if (typeManager == Const.AccountAction.ACTION_NEWS_MANAGER)
            loadMore.type = NEWS_MANAGER
        if (typeManager == Const.AccountAction.ACTION_GENEREL_MANAGER)
            loadMore.type = GENERAL_MANAGER

        loadMore.category_id = categoryId
        loadMore.name = name
        viewModel.loadData(loadMore)
    }

    fun firstLoadCategory() {
        reloadCategory = true
        val firstLoad = PostCategoryRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        if (typeManager == Const.AccountAction.ACTION_NEWS_MANAGER)
            firstLoad.type = NEWS_MANAGER
        if (typeManager == Const.AccountAction.ACTION_GENEREL_MANAGER)
            firstLoad.type = GENERAL_MANAGER

        (viewModel as PostMenuViewModel).loadCategory(firstLoad)
    }

    fun loadMoreCategory(currentCount: Int) {
        reloadCategory = false
        val loadMore = PostCategoryRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        if (typeManager == Const.AccountAction.ACTION_NEWS_MANAGER)
            loadMore.type = NEWS_MANAGER
        if (typeManager == Const.AccountAction.ACTION_GENEREL_MANAGER)
            loadMore.type = GENERAL_MANAGER

        (viewModel as PostMenuViewModel).loadCategory(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<PostProvider>) {
            (adapter as ClickableAdapter<PostProvider>).listener = object : ClickableAdapter.BaseAdapterAction<PostProvider> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: PostProvider, code: Int) {
                    if (data is PostObject) {
                        val i = Intent(context, PostMenuDetailActivity::class.java)
                        i.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                        startActivity(i)
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as PostMenuViewModel).dataReturned.observe(this, Observer { p ->
            p.let {
                hideProgressDialog()
                if (reloadData) it?.let { it1 -> adapter.replaceAll(it1) }
                else it?.let { it1 -> adapter.addAll(it1) }

            }
        })


        (viewModel as PostMenuViewModel).getCategorySusscess.observe(this, Observer { p ->
            p.let {
                if (reloadCategory) it?.let { it1 ->
                    adapterCategory.replaceAll(it1)
                    val category = PostCategory()
                    category.id = 0
                    category.name = "Tất cả danh mục"
                    adapterCategory.addData(0, category)
                }
                else it?.let { it1 -> adapterCategory.addAll(it1) }
            }
        })

        reloadCategory = true
        firstLoadCategory()
    }

    @SuppressLint("SetTextI18n")
    fun performSearching() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Tìm kiếm")
                    .customView(R.layout.dialog_search_post, false)
                    .positiveText("Lọc")
                    .onPositive { dialog, _ ->
                        val edit_post_name = dialog.findViewById(R.id.edit_post_name) as TextInputEditText
                        val edit_post_category = dialog.findViewById(R.id.edit_post_category) as TextInputEditText

                        name = edit_post_name.text.toString().trim { it <= ' ' }
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
            edit_post_name.setText(name)

            if (categoryName.isNotEmpty())
                edit_post_category.setText(categoryName)
            else edit_post_category.setText("Tất cả danh mục")

            dialog.show()
        }
    }

    private fun loadCategory(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn nhà cung cấp")
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
            adapterCategory.listener = object : ClickableAdapter.BaseAdapterAction<PostCategory> {
                override fun click(position: Int, data: PostCategory, code: Int) {
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
        const val TAG = "PostFragment"
        fun newInstance(params: Bundle): PostFragment {
            val fragment = PostFragment()
            fragment.arguments = params

            return fragment
        }

        const val NEWS_MANAGER = 1
        const val GENERAL_MANAGER = 7
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.NEWS_MANAGER_ADD && resultCode == Activity.RESULT_OK)
            firstLoad()
        if (requestCode == Const.RequestCode.NEWS_MANAGER_EDIT && resultCode == Activity.RESULT_OK)
            firstLoad()
    }
}