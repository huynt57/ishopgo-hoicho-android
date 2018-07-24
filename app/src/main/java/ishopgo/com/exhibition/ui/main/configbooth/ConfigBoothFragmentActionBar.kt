package ishopgo.com.exhibition.ui.main.configbooth

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
import ishopgo.com.exhibition.model.BoothConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_setting_booth.*

/**
 * Created by hoangnh on 5/5/2018.
 */
class ConfigBoothFragmentActionBar : BaseActionBarFragment() {

    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()
    private lateinit var viewModel: ConfigBoothViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_setting_booth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        btn_create_booth.setOnClickListener {
            val required = listOf(
                    tv_setting_booth_name,
                    tv_setting_booth_hotline,
                    tv_setting_booth_introduction
            )
            required.map {
                if (!isRequiredFieldsValid(it))
                    return@setOnClickListener
            }

            showProgressDialog()
            viewModel.editConfigBooth(
                    tv_setting_booth_name.text.toString(),
                    tv_setting_booth_hotline.text.toString(),
                    tv_setting_booth_introduction.text.toString(),
                    image,
                    logo,
                    tv_setting_booth_city.text.toString(),
                    tv_setting_booth_district.text.toString(),
                    tv_setting_booth_type.text.toString()
            )
        }

        img_setting_booth.setOnClickListener {
            launchPickPhotoIntent(Const.RequestCode.PICK_BANNER)
        }

        img_setting_logo.setOnClickListener {
            launchPickPhotoIntent(Const.RequestCode.PICK_LOGO)
        }

        tv_setting_booth_city.setOnClickListener {
            getRegion(tv_setting_booth_city)
        }

        tv_setting_booth_district.setOnClickListener {
            getDistrict(tv_setting_booth_district)
        }

        add_relate_booth.setOnClickListener {
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Cấu hình gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }
    }

    private fun launchPickPhotoIntent(code: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, code)
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
                        tv_setting_booth_city.visibility = View.VISIBLE
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ConfigBoothViewModel::class.java, false)
        viewModel.errorSignal.observe(viewLifeCycleOwner!!, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.editSusscess.observe(viewLifeCycleOwner!!, Observer {
            it?.let {
                hideProgressDialog()
                toast("Cập nhật thành công")
            }
        })

        viewModel.loadRegion.observe(viewLifeCycleOwner!!, Observer {
            it?.let {
                adapterRegion.replaceAll(it)
            }
        })

        viewModel.loadDistrict.observe(viewLifeCycleOwner!!, Observer {
            it?.let {
                adapterDistrict.replaceAll(it)
            }
        })

        viewModel.boothTypes.observe(viewLifeCycleOwner!!, Observer {
            it?.let { types ->
                view_choose_type.setOnClickListener {
                    if (currentConfig != null) {
                        val mutableListOf = mutableListOf<String>()
                        currentConfig!!.title?.let {
                            mutableListOf.add(it)
                        }
                        mutableListOf.addAll(types)
                        MaterialDialog.Builder(requireContext())
                                .items(types)
                                .itemsCallback { _, _, _, text -> tv_setting_booth_type.setText(text) }
                                .title("Chọn loại gian hàng")
                                .positiveText("OK")
                                .show()
                    }
                }
            }
        })

        viewModel.getConfigBooth.observe(viewLifeCycleOwner!!, Observer {
            it?.let {
                currentConfig = it
                val converted = ConfigConverter().convert(currentConfig!!)
                tv_setting_booth_name.setText(converted.provideName())
                tv_setting_booth_hotline.setText(converted.provideHotline())
                tv_setting_booth_introduction.setText(converted.provideIntroduction())
                tv_setting_booth_city.setText(converted.provideCity())
                tv_setting_booth_district.setText(converted.provideDistrict())
                tv_setting_booth_address.setText(converted.provideAddress())
                tv_setting_booth_type.setText(converted.provideTitle())
                Glide.with(context)
                        .load(converted.provideBanner())
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(img_setting_booth)

                Glide.with(context)
                        .load(converted.provideBanner())
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(img_setting_logo)
            }
        })

        viewModel.getConfigBooth()
        viewModel.loadRegion()
    }

    class ConfigConverter : Converter<BoothConfig, BoothProvider> {
        override fun convert(from: BoothConfig): BoothProvider {
            return object : BoothProvider {
                override fun provideLogo(): CharSequence {
                    return from.logo ?: ""
                }

                override fun provideDistrict(): CharSequence {
                    return from.district ?: ""
                }

                override fun provideName(): CharSequence {
                    return from.name ?: ""
                }

                override fun provideIntroduction(): CharSequence {
                    return from.introduction?.asHtml() ?: ""
                }

                override fun provideHotline(): CharSequence {
                    return from.hotline ?: ""
                }

                override fun provideCity(): CharSequence {
                    return from.city ?: ""
                }

                override fun provideBanner(): CharSequence {
                    return from.banner ?: ""
                }

                override fun provideAddress(): CharSequence {
                    return from.address ?: ""
                }

                override fun provideTitle(): CharSequence {
                    return from.title ?: ""
                }

            }
        }

    }

    interface BoothProvider {
        fun provideName(): CharSequence
        fun provideIntroduction(): CharSequence
        fun provideHotline(): CharSequence
        fun provideCity(): CharSequence
        fun provideDistrict(): CharSequence
        fun provideBanner(): CharSequence
        fun provideLogo(): CharSequence
        fun provideAddress(): CharSequence
        fun provideTitle(): CharSequence
    }

    private var image: Uri? = null
    private var logo: Uri? = null
    private var currentConfig: BoothConfig? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                return
            }

            if (requestCode == Const.RequestCode.PICK_BANNER) {
                image = data.data

                Glide.with(context)
                        .load(image)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(img_setting_booth)
            } else if (requestCode == Const.RequestCode.PICK_LOGO) {
                logo = data.data

                Glide.with(context)
                        .load(logo)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(img_setting_logo)

            }
        }
    }

    private fun isRequiredFieldsValid(name: TextView): Boolean {
        if (name.text.toString().trim().isEmpty()) {
            toast("Điền hết trường còn trống")
            name.error = "Trường này còn trống"
            return false
        }
        return true
    }
}