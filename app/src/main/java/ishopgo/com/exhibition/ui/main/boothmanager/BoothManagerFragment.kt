package ishopgo.com.exhibition.ui.main.boothmanager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.boothmanager.add_booth.BoothManagerAddActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import java.io.*


class BoothManagerFragment : BaseListFragment<List<BoothManager>, BoothManager>() {
    override fun initLoading() {
        firstLoad()
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<BoothManager>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<BoothManager> {
        val adapter = BoothManagerAdapter()
        adapter.addData(BoothManager())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<BoothManager>> {
        return obtainViewModel(BoothManagerViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    private fun saveQRcodeStorage() {
        if (adapter is ClickableAdapter<BoothManager>)
            (adapter as ClickableAdapter<BoothManager>).listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                @SuppressLint("ObsoleteSdkInt")
                override fun click(position: Int, data: BoothManager, code: Int) {
                    when (code) {
                        SAVE_QRCODE_TO_STORAGE -> {
                            context?.let {
                                if (!hasCameraPermission(it))
                                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Const.RequestCode.STORAGE_PERMISSION)
                                else
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(data.qrcode)
                                            .into(object : SimpleTarget<Bitmap>(300, 300) {
                                                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                                    resource?.let {
                                                        storeImage(it, data.name ?: "unknown")
                                                    }
                                                }
                                            })
                            }

                        }
                        CLICK_ITEM_TO_BOOTH -> {
                            val boothId = data.id
                            val intent = Intent(context, ShopDetailActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
                            startActivityForResult(intent, Const.RequestCode.BOOTH_MANAGER_DELETE)
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        saveQRcodeStorage()
    }

    private fun hasCameraPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }


    private fun storeImage(imageData: Bitmap, filename: String): Boolean {
        // get path to external storage (SD card)
        val sdIconStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString())

        // create storage directories, if they don't exist
        if (!sdIconStorageDir.exists())
            sdIconStorageDir.mkdirs()

        try {
            val filePath = sdIconStorageDir.toString() + File.separator + filename + ".png"
            val fileOutputStream = FileOutputStream(filePath)
            val bos = BufferedOutputStream(fileOutputStream)
            //Toast.makeText(m_cont, "Image Saved at----" + filePath, Toast.LENGTH_LONG).show();
            // choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
            toast("lưuLưu thành công\n$filePath")
            MediaScannerConnection.scanFile(requireContext(), arrayOf(filePath), null, null)
        } catch (e: FileNotFoundException) {
            toast("Không thành công")
            Log.w("TAG", "Error saving image file: " + e.message)
            return false
        } catch (e: IOException) {
            toast("Không thành công")
            Log.w("TAG", "Error saving image file: " + e.message)
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    fun openAddBoothManager() {
        val intent = Intent(context, BoothManagerAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.BOOTH_MANAGER_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Const.RequestCode.BOOTH_MANAGER_ADD || requestCode == Const.RequestCode.BOOTH_MANAGER_DELETE) && resultCode == RESULT_OK)
            firstLoad()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveQRcodeStorage()
        } else {
            // permission was not granted
            if (activity == null) {
                return
            }
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                context?.let {
                    MaterialDialog.Builder(it)
                            .content("Hãy cấp quyền cho ứng dụng sử dụng bộ nhớ trong")
                            .positiveText("Đi tới cài đặt")
                            .onPositive { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", it.packageName, null))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            .negativeText("Huỷ")
                            .show()
                }
        }
    }

    companion object {
        const val TAG = "BoothManagerFragment"
        fun newInstance(params: Bundle): BoothManagerFragment {
            val fragment = BoothManagerFragment()
            fragment.arguments = params

            return fragment
        }

        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_BOOTH = 1
    }
}