package ishopgo.com.exhibition.ui.main.shop

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.shop.ProductsFragment
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoFragment
import ishopgo.com.exhibition.ui.main.shop.rate.RateFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_shop_detail.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopDetailFragment : BaseFragment() {

    private lateinit var viewModel: ShopDetailViewModel
    private lateinit var adapter: PagerAdapter

    companion object {
        fun newInstance(params: Bundle): ShopDetailFragment {
            val fragment = ShopDetailFragment()
            fragment.arguments = params
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetailAdapter(childFragmentManager)
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 3
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            Glide.with(context)
                    .load(data.data)
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(view_image)


            viewModel.editConfigBooth("", "", data.data.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ShopDetailViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
            }
        })
        viewModel.shopImage.observe(this, Observer { i ->
            i?.let {
                Glide.with(view_image.context)
                        .load(it)
                        .apply(RequestOptions().centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_image)
            }
        })

        viewModel.shopId.observe(this, Observer { i ->
            i?.let {
                val boothId = it
                if (UserDataManager.currentUserId == boothId) {
                    tv_edit_image.visibility = View.VISIBLE
                    view_favorite.visibility = View.GONE
                    view_image.setOnClickListener {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
                    }
                } else {
                    tv_edit_image.visibility = View.GONE
                    view_favorite.visibility = View.VISIBLE
                }
                view_favorite.setOnClickListener {
                    if (UserDataManager.currentUserId > 0) {
                        if (boothId != -1L)
                            viewModel.postProductFollow(boothId)
                    } else toast("Bạn vui lòng đăng nhập để sử dụng chức năng này")
                }
            }
        })

        viewModel.shopFollow.observe(this, Observer { i ->
            i?.let {
                Glide.with(context)
                        .load(if (it) R.drawable.ic_added_to_favorite_24dp else R.drawable.ic_add_to_favorite_24dp)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_favorite)
            }
        })

        viewModel.editSusscess.observe(this, Observer {
            toast("Cập nhật thành công")
        })

        viewModel.postFollow.observe(this, Observer { p ->
            p.let {
                if (it?.status ?: 0 == 1) {
                    Glide.with(context)
                            .load(R.drawable.ic_added_to_favorite_24dp)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder))
                            .into(view_favorite)
                    toast("Theo dõi gian hàng thành công")
                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_add_to_favorite_24dp)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder))
                            .into(view_favorite)
                    toast("Bỏ theo dõi gian hàng thành công")
                }
            }
        })
    }

    inner class DetailAdapter(fm: FragmentManager) : CountSpecificPager(fm, 4) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ShopInfoFragment.newInstance(arguments ?: Bundle())
                }
                1 -> {
                    ProductsFragment.newInstance(arguments ?: Bundle())
                }
//                2 -> {
//                    CategoryFragment.newInstance(arguments ?: Bundle())
//                }
                2 -> {
                    RateFragment.newInstance(arguments ?: Bundle())
                }
                else -> {
                    Fragment()
                }
            }
        }

    }
}