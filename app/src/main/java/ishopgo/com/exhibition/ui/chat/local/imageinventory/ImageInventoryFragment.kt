package ishopgo.com.exhibition.ui.chat.local.imageinventory

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 12/30/17. HappyCoding!
 */
class ImageInventoryFragment : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    companion object {
        private val TAG = "ImageInventoryFragment"
    }

    private lateinit var viewModel: ImageInventoryViewModel
    private val inventoryAdapter = ImageInventoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        swipe.setOnRefreshListener {
            swipe.isRefreshing = true
            reloadData = true
            viewModel.getImagesInventory(0)
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Kho hình ảnh")
        toolbar.setLeftButtonClickListener { activity?.finish() }
        toolbar.rightButton(R.drawable.ic_send_default_24dp)
        toolbar.setRightButtonClickListener {
            val intent = Intent()
            val selectedUrls = mutableListOf<String>()
            inventoryAdapter.selectedImages.mapTo(selectedUrls, { i -> i.link ?: "" })
            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, selectedUrls.toTypedArray())
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ImageInventoryViewModel::class.java, false)
        viewModel.images.observe(this, Observer { images ->
            run {
                images?.let {
                    if (reloadData) {
                        if (it.isEmpty()) {
                            view_empty_result_notice.visibility = View.VISIBLE
                            view_empty_result_notice.text = "Nội dung trống"
                        } else view_empty_result_notice.visibility = View.GONE
                        inventoryAdapter.replaceAll(it)
                    } else
                        inventoryAdapter.addAll(it)

                    reloadData = false
                    swipe.isRefreshing = false
                }
            }
        })

        reloadData = true
        viewModel.getImagesInventory(0)
    }

    private fun setupRecyclerView() {
        context?.let {
            val layoutManager = GridLayoutManager(it, 2, LinearLayoutManager.VERTICAL, false)
            view_recyclerview.setHasFixedSize(true)
            view_recyclerview.layoutManager = layoutManager
            view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.d(TAG, "onLoadMore")
                    reloadData = false
                    viewModel.getImagesInventory(totalItemsCount)
                }

            })
            view_recyclerview.adapter = inventoryAdapter

        }

    }

}