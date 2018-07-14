package ishopgo.com.exhibition.ui.main.shop.news

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.BoothPostRequest
import ishopgo.com.exhibition.domain.request.PostCategoryRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.post.post.SpCategoryAdapter
import ishopgo.com.exhibition.ui.main.postmanager.detail.PostManagerDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_list_post_question.*

class NewsFragment : BaseListFragment<List<PostObject>, PostObject>() {
    override fun initLoading() {
        firstLoad()
    }

    companion object {
        fun newInstance(params: Bundle): NewsFragment {
            val fragment = NewsFragment()
            fragment.arguments = params
            return fragment
        }

        const val NEWS_MANAGER = 1
    }

    private var shopId = -1L
    private var categoryId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<PostObject>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<PostObject> {
        return NewsAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = BoothPostRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        loadMore.booth_id = shopId
        loadMore.category_id = categoryId
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = BoothPostRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.booth_id = shopId
        loadMore.category_id = categoryId
        viewModel.loadData(loadMore)
    }

    fun firstLoadCategory() {
        val firstLoad = PostCategoryRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.type = NEWS_MANAGER

        (viewModel as NewsViewModel).loadCategory(firstLoad)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as NewsViewModel).getCategorySusscess.observe(this, Observer { p ->
            p.let {
                val category = PostCategory()
                category.id = 0
                category.name = "Tất cả danh mục"

                val listCategory = mutableListOf<PostCategory>()
                listCategory.add(0, category)
                it?.let { it1 -> listCategory.addAll(it1) }
                val adapter = SpCategoryAdapter(listCategory)
                sp_category.adapter = adapter
                sp_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (it != null && it.isNotEmpty()) {
                            categoryId = if (position == 0) {
                                0
                            } else
                                it[position - 1].id
                            firstLoad()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
        })

        firstLoadCategory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<PostObject>) {
            (adapter as ClickableAdapter<PostObject>).listener = object : ClickableAdapter.BaseAdapterAction<PostObject> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: PostObject, code: Int) {
                    val i = Intent(context, PostManagerDetailActivity::class.java)
                    i.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                    startActivity(i)
                }
            }
        }
    }

    override fun obtainViewModel(): BaseListViewModel<List<PostObject>> {
        return obtainViewModel(NewsViewModel::class.java, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_post_question, container, false)
    }
}