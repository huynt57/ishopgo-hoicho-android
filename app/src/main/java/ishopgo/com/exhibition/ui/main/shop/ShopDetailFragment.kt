package ishopgo.com.exhibition.ui.main.shop

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.ShopDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.main.product.shop.ProductsFragment
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoFragment
import ishopgo.com.exhibition.ui.main.shop.news.NewsFragment
import ishopgo.com.exhibition.ui.main.shop.rate.RateFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_shop_detail.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopDetailFragment : BaseFragment() {

    private lateinit var viewModel: ShopDetailViewModel
    private lateinit var adapter: DetailAdapter
    private var boothId = -1L
    private var dataShopDetail: ShopDetail? = null

    companion object {
        const val TAG = "ShopDetailFragment"
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
        view_pager.offscreenPageLimit = 4
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))

        view_shop_message.setOnClickListener { messageShop() }
    }

    private fun messageShop() {
        if (UserDataManager.currentUserId > 0) {
            // gui tin nhan cho shop
            boothId.let {
                val request = CreateConversationRequest()
                request.type = 1
                val members = mutableListOf<Long>()
                members.add(UserDataManager.currentUserId)
                members.add(it)
                request.member = members
                viewModel.createConversation(request)
            }
        } else {
            openLoginActivity()
        }
    }

    private fun callShop(context: Context, sdt: String) {
        val call = Uri.parse("tel:$sdt")
        val intent = Intent(Intent.ACTION_DIAL, call)
        if (intent.resolveActivity(context.packageManager) != null)
            startActivity(intent)
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
                hideProgressDialog()
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

        viewModel.qrCode.observe(this, Observer { i ->
            i?.let {
                dataShopDetail = it
            }
        })

        viewModel.productCount.observe(this, Observer { i ->
            i?.let {
                view_shop_productCount.text = "$it sản phẩm"
            }
        })

        viewModel.shopId.observe(this, Observer { i ->
            i?.let {
                boothId = it
                view_shop_follow.setOnClickListener {
                    if (UserDataManager.currentUserId > 0) {
                        if (boothId != -1L)
                            viewModel.postProductFollow(boothId)
                    } else openLoginActivity()
                }
            }
        })

        viewModel.conversation.observe(this, Observer { c ->
            c?.let {
                val conv = LocalConversationItem()
                conv.idConversions = c.id ?: ""
                conv.name = c.name ?: ""

                context?.let {
                    val intent = Intent(it, ConversationActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conv.idConversions)
                    intent.putExtra(Const.TransferKey.EXTRA_TITLE, conv.name)
                    startActivity(intent)
                }
            }
        })

        viewModel.shopSDT.observe(this, Observer { i ->
            i?.let {
                val sdt = it
                view_shop_call.setOnClickListener { callShop(it.context, sdt) }
            }
        })

        viewModel.shopFollow.observe(this, Observer { i ->
            i?.let {
                view_shop_follow.drawableCompat(0, if (it) R.drawable.ic_favorite_accent_24dp else R.drawable.ic_favorite_border_default_24dp, 0, 0)
            }
        })

        viewModel.editSusscess.observe(this, Observer {
            toast("Cập nhật thành công")
        })

        viewModel.postFollow.observe(this, Observer { p ->
            p.let {
                if (it?.status ?: 0 == 1) {
                    view_shop_follow.drawableCompat(0, R.drawable.ic_favorite_accent_24dp, 0, 0)
                    toast("Theo dõi gian hàng thành công")
                } else {
                    view_shop_follow.drawableCompat(0, R.drawable.ic_favorite_border_default_24dp, 0, 0)
                    toast("Bỏ theo dõi gian hàng thành công")
                    activity?.setResult(RESULT_OK)
                }
            }
        })

        viewModel.deleteSusscess.observe(this, Observer {
            toast("Xoá thành công")
            activity?.setResult(Activity.RESULT_OK)
            hideProgressDialog()
            activity?.finish()
        })


    }

    fun deleleBooth() {
        context?.let {
            MaterialDialog.Builder(it)
                    .content("Bạn có muốn xoá gian hàng này không?")
                    .positiveText("Có")
                    .onPositive { _, _ ->
                        viewModel.deleteBooth(boothId)
                        showProgressDialog()
                    }
                    .negativeText("Không")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .show()
        }
    }

    fun openQRCodeBooth() {
        dataShopDetail?.let {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            Navigation.findNavController(view_pager).navigate(R.id.action_shopDetailFragmentActionBar_to_qrCodeShopFragment, extra)
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
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
                2 -> {
                    NewsFragment.newInstance(arguments ?: Bundle())
                }
                3 -> {
                    RateFragment.newInstance(arguments ?: Bundle())
                }
                else -> {
                    Fragment()
                }
            }
        }

    }
}