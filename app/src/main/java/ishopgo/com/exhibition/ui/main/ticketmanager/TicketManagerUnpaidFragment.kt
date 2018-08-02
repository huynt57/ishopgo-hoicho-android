package ishopgo.com.exhibition.ui.main.ticketmanager

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.TicketRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import net.glxn.qrgen.android.QRCode
import java.io.*
import java.io.File.separator


class TicketManagerUnpaidFragment : BaseListFragment<List<Ticket>, Ticket>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_search_swipable_recyclerview, container, false)
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = TicketRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.payment_status = UN_PAID
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = TicketRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.payment_status = UN_PAID
        viewModel.loadData(loadMore)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<Ticket>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<Ticket> {
        return TicketManagerAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<Ticket>> {
        return obtainViewModel(TicketManagerViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (viewModel as TicketManagerViewModel).total.observe(this, Observer { p ->
            p?.let {
                if (it > 0) search_total.visibility = View.VISIBLE else search_total.visibility = View.GONE
                search_total.text = "Có $it vé tham quan"
            }
        })
    }

    private fun saveTicketStorage() {
        if (adapter is ClickableAdapter<Ticket>)
            (adapter as ClickableAdapter<Ticket>).listener = object : ClickableAdapter.BaseAdapterAction<Ticket> {
                @SuppressLint("ObsoleteSdkInt")
                override fun click(position: Int, data: Ticket, code: Int) {
                    when (code) {
                        SAVE_QRCODE_TO_STORAGE -> {
                            context?.let {
                                if (!hasCameraPermission(it))
                                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Const.RequestCode.STORAGE_PERMISSION)
                                else
                                    storeImage(QRCode.from(data.code).withSize(300, 300).bitmap(), data.name
                                            ?: "unknown")
                            }
                        }
                        CLICK_ITEM_TO_PROFILE -> {
                            val intent = Intent(context, MemberProfileActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                            startActivity(intent)
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
        fun newInstance(params: Bundle): TicketManagerUnpaidFragment {
            val fragment = TicketManagerUnpaidFragment()
            fragment.arguments = params

            return fragment
        }

        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_PROFILE = 1
        const val UN_PAID: Long = 1
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