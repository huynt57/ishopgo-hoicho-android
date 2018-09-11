package ishopgo.com.exhibition.ui.main.configbooth

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.ShopRelateRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.BoothAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.BrandsAdapter
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_setting_booth.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hoangnh on 5/5/2018.
 */
class ConfigBoothFragmentActionBar : BaseActionBarFragment() {

    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()
    private lateinit var viewModel: ConfigBoothViewModel
    private lateinit var shareViewModel: ShareBoothViewModel
    private val relateBoothAdapter = BoothRelateDeleteAdapter()
    private var listBoothRelate: MutableList<BoothManager> = mutableListOf()
    private var postMediasCert = ArrayList<PostMedia>()
    private var listImageDeleteCert = ArrayList<PostMedia>()
    private var adapterImagesCert = ComposingPostMediaAdapter()
    private val adapterDonViCungUng = BoothAdapter()

    companion object {
        const val CLICK_OPEN_OPTION = 1
        const val PERMISSIONS_REQUEST_CAMERA = 100
        const val TYPE_DONVI_CAP_2: Int = 1
        const val TYPE_DONVI_CAP_3: Int = 2
        const val TYPE_DONVI_CAP_4: Int = 3
        const val TYPE_DONVI_CAP_5: Int = 4
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_setting_booth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()
        setupImageCertRecycleview()
//        related_booth.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
//
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        related_booth.layoutManager = layoutManager
//        related_booth.adapter = relateBoothAdapter
//        relateBoothAdapter.listener = object : ClickableAdapter.BaseAdapterAction<BoothRelate> {
//            override fun click(position: Int, data: BoothRelate, code: Int) {
//                when (code) {
//                    CLICK_OPEN_OPTION -> {
//                        context?.let {
//                            val dialog = MaterialDialog.Builder(it)
//                                    .customView(R.layout.dialog_option_booth_relate, false)
//                                    .negativeText("Huỷ")
//                                    .onNegative { dialog, _ -> dialog.dismiss() }
//                                    .autoDismiss(false)
//                                    .canceledOnTouchOutside(false)
//                                    .build()
//
//                            val tv_edit_booth_related = dialog.findViewById(R.id.tv_edit_booth_related) as VectorSupportTextView
//                            val tv_delete_booth_related = dialog.findViewById(R.id.tv_delete_booth_related) as VectorSupportTextView
//
//
//                            tv_edit_booth_related.setOnClickListener {
//                                dialog.dismiss()
//                                showDialogEditBoothRelate(data)
//                            }
//                            tv_delete_booth_related.setOnClickListener {
//                                for (i in listBoothRelate.indices)
//                                    if (data.id == listBoothRelate[i].id) {
//                                        listBoothRelate.removeAt(i)
//                                        relateBoothAdapter.replaceAll(listBoothRelate)
//                                        dialog.dismiss()
//                                        break
//                                    }
//                            }
//
//                            val window = dialog.window
//                            if (window != null) {
//                                window.attributes.windowAnimations = R.style.BottomDialog
//                                window.setGravity(Gravity.BOTTOM)
//                            }
//                            dialog.show()
//                        }
//                    }
//                }
//            }
//        }

        edit_product_donVi2.setOnClickListener { getBooth(edit_product_donVi2, TYPE_DONVI_CAP_2) }
        edit_product_donVi3.setOnClickListener { getBooth(edit_product_donVi3, TYPE_DONVI_CAP_3) }
        edit_product_donVi4.setOnClickListener { getBooth(edit_product_donVi4, TYPE_DONVI_CAP_4) }
        edit_product_donVi5.setOnClickListener { getBooth(edit_product_donVi5, TYPE_DONVI_CAP_5) }

        view_camera_cert.setOnClickListener {
            takePhoto()
        }

        view_add_images_cert.setOnClickListener {
            launchPickPhotoIntent(Const.RequestCode.RC_PICK_IMAGE)
        }

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
                    tv_setting_booth_type.text.toString(),
                    listBoothRelate,
                    edit_product_chucNangDV2.text.toString(),
                    edit_product_chucNangDV3.text.toString(),
                    edit_product_chucNangDV4.text.toString(),
                    edit_product_chucNangDV5.text.toString(), postMediasCert
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

//        add_relate_booth.setOnClickListener {
//            shareViewModel.openBoothRelateFragment()
//        }
    }

    private fun firstLoadProvider() {
        reloadData = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBooth(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadData = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBooth(loadMore)
    }

    private fun setupImageCertRecycleview() {
        context?.let {
            rv_product_cert.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            rv_product_cert.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_product_cert.adapter = adapterImagesCert
            adapterImagesCert.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    postMediasCert.remove(data)
                    if (data.uri.toString().subSequence(0, 4).contains("http"))
                        listImageDeleteCert.add(data)
                    if (postMediasCert.isEmpty()) rv_product_cert.visibility = View.GONE
                    adapterImagesCert.replaceAll(postMediasCert)
                }
            }
        }
    }

//    private fun showDialogEditBoothRelate(data: BoothRelate) {
//        context?.let {
//            val dialog = MaterialDialog.Builder(it)
//                    .customView(R.layout.dialog_edit_function_booth_relate, false)
//                    .title("Sửa chức năng của đơn vị")
//                    .positiveText("Đồng ý")
//                    .onPositive { dialog, _ ->
//                        val edit_content_relate = dialog.findViewById(R.id.edit_content_relate) as TextInputEditText
//                        for (i in listBoothRelate.indices)
//                            if (data.id == listBoothRelate[i].id) {
//                                listBoothRelate[i].content = edit_content_relate.text.toString()
//                                relateBoothAdapter.replaceAll(listBoothRelate)
//                                dialog.dismiss()
//                                break
//                            }
//                    }
//                    .negativeText("Huỷ")
//                    .onNegative { dialog, _ -> dialog.dismiss() }
//                    .show()
//
//            val edit_content_relate = dialog.findViewById(R.id.edit_content_relate) as TextInputEditText
//            for (i in listBoothRelate.indices)
//                if (data.id == listBoothRelate[i].id) {
//                    edit_content_relate.setText(listBoothRelate[i].content)
//                    break
//                }
//
//        }
//    }

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
                        viewModel.loadDistrict(data.name)
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
        shareViewModel = obtainViewModel(ShareBoothViewModel::class.java, true)

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
                listImageDeleteCert.clear()
                postMediasCert.clear()

                viewModel.loadShopRelates(UserDataManager.currentUserId)
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
                tv_setting_booth_type.setOnClickListener {
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
                viewModel.loadDistrict(converted.provideCity().toString())
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

                if (converted.providerCertImages().isNotEmpty()) {
                    for (i in converted.providerCertImages().indices) {
                        val postMedia = PostMedia()
                        postMedia.uri = Uri.parse(converted.providerCertImages()[i].image)
                        postMediasCert.add(postMedia)
                    }
                    adapterImagesCert.replaceAll(postMediasCert)
                }
            }
        })

        viewModel.shopRelates.observe(this, Observer { r ->
            r?.let {
                listBoothRelate = it as MutableList<BoothManager>
                val donVi = listBoothRelate
                for (i in listBoothRelate.indices) {
                    if (i == 0) {
                        btn_add_donVi3.visibility = View.VISIBLE
                        linear_dovi2.visibility = View.VISIBLE

                        edit_product_donVi2.setText(donVi[i].name)
                        if (donVi[i].content == "null")
                            edit_product_chucNangDV2.setText("")
                        else
                            edit_product_chucNangDV2.setText(donVi[i].content)
                    }
                    if (i == 1) {
                        btn_add_donVi3.visibility = View.GONE
                        btn_add_donVi4.visibility = View.VISIBLE
                        linear_dovi3.visibility = View.VISIBLE

                        edit_product_donVi3.setText(donVi[i].name)
                        if (donVi[i].content == "null")
                            edit_product_chucNangDV3.setText("")
                        else
                            edit_product_chucNangDV3.setText(donVi[i].content)
                    }
                    if (i == 2) {
                        btn_add_donVi4.visibility = View.GONE
                        btn_add_donVi5.visibility = View.VISIBLE
                        linear_dovi4.visibility = View.VISIBLE

                        edit_product_donVi4.setText(donVi[i].name)
                        if (donVi[i].content == "null")
                            edit_product_chucNangDV4.setText("")
                        else
                            edit_product_chucNangDV4.setText(donVi[i].content)
                    }
                    if (i == 3) {
                        btn_add_donVi5.visibility = View.GONE
                        linear_dovi5.visibility = View.VISIBLE

                        edit_product_donVi5.setText(donVi[i].name)
                        if (donVi[i].content == "null")
                            edit_product_chucNangDV5.setText("")
                        else
                            edit_product_chucNangDV5.setText(donVi[i].content)
                    }
                }
//                relateBoothAdapter.replaceAll(it)
            }
        })

        viewModel.dataBooth.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    adapterDonViCungUng.replaceAll(it)
                } else {
                    adapterDonViCungUng.addAll(it)
                }
            }
        })

//        shareViewModel.getDataBoothRelate.observe(this, Observer { p ->
//            p?.let {
//                listBoothRelate.add(it)
//                relateBoothAdapter.replaceAll(listBoothRelate)
//            }
//        })

        viewModel.loadShopRelates(UserDataManager.currentUserId)
        firstLoadProvider()
        viewModel.getConfigBooth()
        viewModel.loadRegion()
    }

    private fun getBooth(view: TextView, type: Int) {
        context?.let {

            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn đơn vị trong chuỗi cung ứng")
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

            rv_search.adapter = adapterDonViCungUng

            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })

            adapterDonViCungUng.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        if (type == TYPE_DONVI_CAP_2) {
                            if (listBoothRelate.size >= 1) {
                                listBoothRelate.removeAt(0)
                                listBoothRelate.add(data)
                            } else listBoothRelate.add(data)
                        }
                        if (type == TYPE_DONVI_CAP_3) {
                            if (listBoothRelate.size >= 2) {
                                listBoothRelate.removeAt(1)
                                listBoothRelate.add(1, data)
                            } else listBoothRelate.add(data)
                        }
                        if (type == TYPE_DONVI_CAP_4) {
                            if (listBoothRelate.size >= 3) {
                                listBoothRelate.removeAt(2)
                                listBoothRelate.add(2, data)
                            } else listBoothRelate.add(data)
                        }
                        if (type == TYPE_DONVI_CAP_4) {
                            if (listBoothRelate.size >= 4) {
                                listBoothRelate.removeAt(3)
                                listBoothRelate.add(3, data)
                            } else listBoothRelate.add(data)
                        }

                        view.text = data.boothName ?: ""
                        view.error = null
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
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

                override fun providerCertImages(): List<ProductDetail.ListCert> {
                    return from.certImages ?: mutableListOf()
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
        fun providerCertImages(): List<ProductDetail.ListCert>
    }

    private var image: Uri? = null
    private var logo: Uri? = null
    private var currentConfig: BoothConfig? = null

    private var sendingPhotoUri: Uri? = null
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )
    }

    private fun takePhoto() {
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {

            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA) }

        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(context?.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.e("Hong", "khong the tao file", ex)
                }
                photoFile?.let {
                    val photoURI = FileProvider.getUriForFile(context!!, getString(R.string.file_provider_authority), it)
                    sendingPhotoUri = photoURI

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, Const.RequestCode.TAKE_PICTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Const.RequestCode.PICK_BANNER || requestCode == Const.RequestCode.PICK_LOGO) && resultCode == Activity.RESULT_OK && null != data) {
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

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMediasCert.add(postMedia)
            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMediasCert.add(postMedia)
                }
            }
            adapterImagesCert.replaceAll(postMediasCert)
            rv_product_cert.visibility = View.VISIBLE
        }

        if (requestCode == Const.RequestCode.TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            sendingPhotoUri?.let {
                val postMedia = PostMedia()

                postMedia.uri = it
                postMediasCert.add(postMedia)
                adapterImagesCert.replaceAll(postMediasCert)
                rv_product_cert.visibility = View.VISIBLE
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