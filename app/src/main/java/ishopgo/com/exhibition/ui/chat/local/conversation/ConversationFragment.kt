package ishopgo.com.exhibition.ui.chat.local.conversation

import android.annotation.TargetApi
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.pattern.PatternChooserBottomSheet
import ishopgo.com.exhibition.ui.chat.local.imageinventory.ImageInventoryActivity
import ishopgo.com.exhibition.ui.chat.local.info.ConversationInfoActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.ChatReplyActionView
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_local_chat_conversation.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuanhong on 4/11/18. HappyCoding!
 */
class ConversationFragment : BaseActionBarFragment() {
    companion object {
        private val TAG = "ConversationFragment"

        fun newInstance(data: Bundle): ConversationFragment {
            val fragment = ConversationFragment()
            fragment.arguments = data

            return fragment
        }

    }

    private lateinit var viewModel: ConversationViewModel
    private lateinit var sharedViewModel: ConversationSharedViewModel

    private val adapter = MessageAdapter()
    private var sendingPhotoUri: Uri? = null
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var conversation: LocalConversationItem
    private lateinit var conversationInfo: ConversationInfo

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_conversation
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = obtainViewModel(ConversationSharedViewModel::class.java, true)
        sharedViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        sharedViewModel.newMessage.observe(this, Observer { m ->
            m?.let {
                // we have new message
                Log.d(TAG, "received new message: $m")
                adapter.addData(0, it)

                recyclerview.post {
                    smoothScroller.targetPosition = 0
                    recyclerview.layoutManager.startSmoothScroll(smoothScroller)
                }
            }
        })

        viewModel = obtainViewModel(ConversationViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.messages.observe(this, Observer { m ->
            m?.let {
                if (reloadData) {
                    adapter.replaceAll(it)
                } else {
                    adapter.addAll(it)
                }

                // because we just want to load latest message, because of reverse layout so add scroll listener for first time here
                if (reloadData) {
                    recyclerview.post {
                        smoothScroller.targetPosition = 0
                        recyclerview.layoutManager.startSmoothScroll(smoothScroller)

                        recyclerview.addOnScrollListener(scrollListener)
                        reloadData = false
                    }
                }
            }
        })
        viewModel.patternCreated.observe(this, Observer { p ->
            p?.let {
                toast("Tạo thành công")
                view_reply.setText(it.content ?: "")
            }
        })
        viewModel.patternUpdated.observe(this, Observer { ok ->
            if (ok == true) toast("Sửa thành công")
        })
        viewModel.patternRemoved.observe(this, Observer { ok ->
            if (ok == true) toast("Xoá thành công")
        })
        viewModel.conversationInfo.observe(this, Observer { info ->
            info?.let {
                conversationInfo = it
            }
        })

        viewModel.getConversationInfo(conversation.idConversions)
        // start loading message
        reloadData = true
        viewModel.getMessages(conversation.idConversions, -1L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = arguments
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        try {
            conversation = Toolbox.gson.fromJson(json, LocalConversationItem::class.java)
        } catch (e: Exception) {
            throw RuntimeException("can not parse LocalConversationItem")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        smoothScroller = object : LinearSmoothScroller(view.context) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        toolbar.setCustomTitle(conversation.name ?: "Chưa đặt tên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
        toolbar.rightButton(R.drawable.ic_info_green_24dp)
        toolbar.setRightButtonClickListener {
            context?.let {
                if (::conversationInfo.isInitialized) {
                    val intent = Intent(it, ConversationInfoActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(conversationInfo))
                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conversation.idConversions)
                    startActivity(intent)
                }
            }
        }

        setupStream()
        view_reply.listener = object : ChatReplyActionView.ChatReplyListener {

            override fun addAttachments(actionView: ChatReplyActionView) {
                val fragment = AddAttachmentBottomSheet.newInstance(Bundle())
                fragment.chooseCamera = View.OnClickListener {
                    getImageFromCamera(it.context)
                }
                fragment.chooseGallery = View.OnClickListener {
                    getImageFromGallery()
                }
                fragment.chooseInventory = View.OnClickListener {
                    selectImagesFromInventory()
                }
                fragment.choosePattern = View.OnClickListener {
                    selectTextPattern()
                }
                fragment.show(childFragmentManager, "AddAttachmentBottomSheet")
            }

            override fun sendTextMessage(actionView: ChatReplyActionView, text: String) {
                viewModel.sendTextMessage(conversation.idConversions, text)
            }
        }
    }

    private fun selectTextPattern() {
        showPatternChooser()
    }

    private fun selectImagesFromInventory() {
        context?.let {
            startActivityForResult(Intent(it, ImageInventoryActivity::class.java), Const.RequestCode.RC_PICK_IMAGES)
        }
    }

    private fun getImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private fun getImageFromCamera(context: Context) {
        if (!hasCameraPermission()) {
            requestCameraPermission()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            val photoFile: File
            try {
                photoFile = createImageFile(context)
            } catch (ex: IOException) {
                toast("Không thể tạo file.")
                return
            }
            photoFile.let {
                val photoURI = FileProvider.getUriForFile(context, "ishopgo.com.exhibition.fileprovider", it)
                sendingPhotoUri = photoURI
                Log.d(TAG, "create file temp: $photoURI")

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, Const.RequestCode.RC_CAPTURE_IMAGE)
            }

        }
    }

    private fun showPatternChooser() {
        context?.let {
            val fragment = PatternChooserBottomSheet()
            fragment.patternChooserListener = object : PatternChooserBottomSheet.PatternChooserListener {
                override fun addPattern() {
                    showAddPatternDialog()
                }

                override fun submitPattern(pattern: TextPattern) {
                    view_reply.setText(pattern.content ?: "")
                }

                override fun editPattern(pattern: TextPattern) {
                    showEditPatternDialog(pattern)
                }

                override fun removePattern(pattern: TextPattern) {
                    showConfirmRemovePatternDialog(pattern)
                }

                private fun showAddPatternDialog() {
                    context?.let {
                        MaterialDialog.Builder(it)
                                .title("Thêm mẫu tin nhắn")
                                .input("Nội dung tin nhắn mẫu", null, false) { _, input ->
                                    viewModel.addPattern(conversation.idConversions, input.toString())
                                }
                                .positiveText("OK")
                                .negativeText("Huỷ")
                                .show()
                    }
                }

                private fun showEditPatternDialog(pattern: TextPattern) {
                    context?.let {
                        MaterialDialog.Builder(it)
                                .title("Sửa tin nhắn mẫu")
                                .input(pattern.content, null, false) { _, input ->
                                    run {
                                        val newPattern = TextPattern()
                                        newPattern.id = pattern.id
                                        newPattern.content = input.toString()
                                        viewModel.updatePattern(conversation.idConversions, newPattern)
                                    }
                                }
                                .positiveText("OK")
                                .negativeText("Huỷ")
                                .show()
                    }
                }

                private fun showConfirmRemovePatternDialog(pattern: TextPattern) {
                    context?.let {
                        MaterialDialog.Builder(it)
                                .title("Xoá tin nhắn mẫu ?")
                                .content(pattern.content ?: "")
                                .positiveText("OK")
                                .onPositive { _, _ -> viewModel.removePattern(conversation.idConversions, pattern.id) }
                                .negativeText("Huỷ")
                                .show()
                    }
                }

            }
            fragment.show(childFragmentManager, "PatternChooserBottomSheet")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.RequestCode.RC_PICK_IMAGE -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        viewModel.sendImageMessage(conversation.idConversions, it)
                    }
                }
                Const.RequestCode.RC_PICK_IMAGES -> {
                    val urls = data?.getStringArrayExtra(Const.TransferKey.EXTRA_STRING_LIST)
                    val uris = mutableListOf<Uri>()
                    urls?.mapTo(uris, { s -> Uri.parse(s) })
                    Log.d(TAG, "picked images = $uris")
                    if (uris.isNotEmpty()) {
                        viewModel.sendImagesFromInventory(conversation.idConversions, uris)
                    }
                }
                Const.RequestCode.RC_CAPTURE_IMAGE -> {
                    sendingPhotoUri?.let {
                        viewModel.sendImageMessage(conversation.idConversions, it)

                        sendingPhotoUri = null
                    }
                }
                else -> {
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestCameraPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), Const.RequestCode.CAMERA_PERMISSION)
    }

    private fun hasCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED }
                    ?: false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Const.RequestCode.CAMERA_PERMISSION) {
            val hasCameraPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (hasCameraPermission) {
                context?.let { getImageFromCamera(it) }
            }
        }
    }

    private fun setupStream() {
        context?.let {
            val layoutManager = LinearLayoutManager(it)
            layoutManager.reverseLayout = true
            layoutManager.stackFromEnd = true
            recyclerview.setHasFixedSize(true)
            recyclerview.layoutManager = layoutManager
            scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    reloadData = false
                    viewModel.getMessages(conversation.idConversions, adapter.getItem(adapter.itemCount - 1).getMessageId())
                }

            }
        }


        adapter.listener = object : ClickableAdapter.BaseAdapterAction<IChatMessage> {
            override fun click(position: Int, data: IChatMessage, code: Int) {
                when (code) {
                    MessageAdapter.CODE_FAIL -> {
                        //                adapter.remove(data)
//
//                viewModel.resendMessage(data)
//                adapter.addAt(0, data)
//
//                smoothScroller.targetPosition = 0
//                recyclerview.layoutManager.startSmoothScroll(smoothScroller)
                    }
                    MessageAdapter.CODE_NORMAL -> {
                        // ignored
                    }
                    else -> {
                    }
                }
            }
        }
        recyclerview.adapter = adapter

    }

}