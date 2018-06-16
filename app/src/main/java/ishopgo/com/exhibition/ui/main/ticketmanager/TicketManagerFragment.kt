package ishopgo.com.exhibition.ui.main.ticketmanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import net.glxn.qrgen.android.QRCode
import java.io.*
import java.io.File.separator


class TicketManagerFragment : BaseListFragment<List<TicketManagerProvider>, TicketManagerProvider>() {

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<TicketManagerProvider>) {
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
        hideProgressDialog()
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<TicketManagerProvider> {
        return TicketManagerAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<TicketManagerProvider>> {
        return obtainViewModel(TicketManagerViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    private fun saveTicketStorage() {
        if (adapter is ClickableAdapter<TicketManagerProvider>)
            (adapter as ClickableAdapter<TicketManagerProvider>).listener = object : ClickableAdapter.BaseAdapterAction<TicketManagerProvider> {
                @SuppressLint("ObsoleteSdkInt")
                override fun click(position: Int, data: TicketManagerProvider, code: Int) {
                    when (code) {
                        SAVE_QRCODE_TO_STORAGE -> {
                            context?.let {
                                if (!hasCameraPermission(it))
                                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Const.RequestCode.STORAGE_PERMISSION)
                                else
                                    storeImage(QRCode.from(data.provideTicketCode()).withSize(300, 300).bitmap(), data.provideBoothName())
                            }
                        }
                        CLICK_ITEM_TO_PROFILE -> {
                            if (data is Ticket) {
                                val intent = Intent(context, MemberProfileActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        saveTicketStorage()
    }

    private fun hasCameraPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        fun newInstance(params: Bundle): TicketManagerFragment {
            val fragment = TicketManagerFragment()
            fragment.arguments = params

            return fragment
        }

        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_PROFILE = 1
    }

    private fun storeImage(imageData: Bitmap, filename: String): Boolean {
        // get path to external storage (SD card)
        val sdIconStorageDir = File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).toString())

        // create storage directories, if they don't exist
        if (!sdIconStorageDir.exists())
            sdIconStorageDir.mkdirs()

        try {
            val filePath = sdIconStorageDir.toString() + separator + filename + ".png"
            val fileOutputStream = FileOutputStream(filePath)
            val bos = BufferedOutputStream(fileOutputStream)
            //Toast.makeText(m_cont, "Image Saved at----" + filePath, Toast.LENGTH_LONG).show();
            // choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
            toast("Lưu thành công\n$filePath")

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Const.RequestCode.STORAGE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveTicketStorage()
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
    }
}