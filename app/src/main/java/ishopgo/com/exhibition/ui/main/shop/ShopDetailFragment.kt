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
    private var shopId: Long = 0

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

        if (UserDataManager.currentUserId == shopId) {
            view_image.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
            }

            view_shop_name.setOnClickListener {
                view_shop_name.isFocusable = true
                view_shop_name.isFocusableInTouchMode = true
            }

            view_shop_name.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_NULL) {

                    val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view_shop_name.windowToken, 0)

                    if (checkRequireFields(view_shop_name.text.toString()))
                        viewModel.editConfigBooth(view_shop_name.text.toString(), "")

                    return@OnEditorActionListener true
                }

                false
            })
        }
    }

    private fun checkRequireFields(name: String): Boolean {

        if (name.trim().isEmpty()) {
            toast("Tên gian hàng không được để trống")
            view_shop_name.error = "Trường này còn trống"
            return false
        }
        return true
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


            viewModel.editConfigBooth("", data.data.toString())
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

        viewModel.shopName.observe(this, Observer { i ->
            i?.let {
                view_shop_name.setText(it)
            }
        })
        viewModel.shopId.observe(this, Observer { i ->
            i?.let {
                shopId = it
            }
        })

        viewModel.editSusscess.observe(this, Observer {
            view_shop_name.isFocusable = false
            view_shop_name.isFocusableInTouchMode = false
            toast("Cập nhật thành công")
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