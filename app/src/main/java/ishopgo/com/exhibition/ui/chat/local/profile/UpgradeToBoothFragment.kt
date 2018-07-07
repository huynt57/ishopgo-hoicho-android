package ishopgo.com.exhibition.ui.chat.local.profile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_upgrade_to_booth.*
import kotlinx.android.synthetic.main.item_image_only.*

class UpgradeToBoothFragment : BaseActionBarFragment() {

    private var currentMemberId = -1L
    private lateinit var viewModel: MemberProfileViewModel
    private var bannerUri: Uri? = null
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_upgrade_to_booth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(MemberProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { e -> e?.let { resolveError(it) } })
        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                adapterRegion.replaceAll(it)
            }
        })
        viewModel.loadDistrict.observe(this, Observer { p ->
            p?.let {
                adapterDistrict.replaceAll(it)
            }
        })

        currentMemberId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadRegion()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        avatar.setOnClickListener { getImageFromGallery() }
        booth_city.editText?.setOnClickListener {
            booth_city.editText?.let {
                getRegion(it)
            }
        }
        booth_district.editText?.setOnClickListener {
            booth_district.editText?.let {
                getDistrict(it)
            }
        }
        upgrade.setOnClickListener {
            if (isInputValid()) {
                val name = booth_name.editText?.text.toString() ?: ""
                val introduction = booth_introduce.editText?.text.toString() ?: ""
                val hotline = booth_hotline.editText?.text.toString() ?: ""
                val info = ""
                val city = booth_city.editText?.text.toString() ?: ""
                val district = booth_district.editText?.text.toString() ?: ""
                viewModel.upgradeMemberToBooth(currentMemberId, name, introduction, hotline, info, bannerUri!!, city, district)
            }
        }
    }

    private fun getDistrict(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn quận huyện")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterDistrict
            adapterDistrict.listener = object : ClickableAdapter.BaseAdapterAction<District> {
                override fun click(position: Int, data: District, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn khu vực")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        data.provinceid?.let { it1 -> viewModel.loadDistrict(it1) }
                        booth_district.visibility = View.VISIBLE
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }


    private fun getImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.RequestCode.RC_PICK_IMAGE -> {
                    bannerUri = data?.data
                    Glide.with(requireContext())
                            .load(bannerUri)
                            .apply(RequestOptions().centerInside())
                            .into(avatar)
                }
                else -> {
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val name = booth_name.editText?.text.toString() ?: ""
        val introduction = booth_introduce.editText?.text.toString() ?: ""
        val hotline = booth_hotline.editText?.text.toString() ?: ""
        val info = ""
        val city = booth_city.editText?.text.toString() ?: ""
        val district = booth_district.editText?.text.toString() ?: ""

        var isValid = true
        var msg = ""
        when {
            name.isBlank() -> {
                isValid = false; msg = "Chưa điền tên gian hàng"
            }
            introduction.isBlank() -> {
                isValid = false; msg = "Chưa điền tên lời giới thiệu"
            }
            hotline.isBlank() -> {
                isValid = false; msg = "Chưa điền hotline"
            }
            city.isBlank() -> {
                isValid = false; msg = "Chưa chọn tỉnh/thành phố"
            }
            district.isBlank() -> {
                isValid = false; msg = "Chưa chọn quận/huyện"
            }
            bannerUri == null -> {
                isValid = false; msg = "Chưa chọn banner"
            }
        }

        if (!isValid) {
            toast(msg)
            return false
        }

        return true
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Nâng cấp lên gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            hideKeyboard()
            activity?.onBackPressed()
        }
    }

}