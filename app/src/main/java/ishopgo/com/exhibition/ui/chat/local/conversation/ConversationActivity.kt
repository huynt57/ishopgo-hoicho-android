package ishopgo.com.exhibition.ui.chat.local.conversation

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.pusher.client.Pusher
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.MyApp
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.pattern.PatternChooserBottomSheet
import ishopgo.com.exhibition.ui.chat.local.imageinventory.ImageInventoryActivity
import ishopgo.com.exhibition.ui.chat.local.info.ConversationInfoActivity
import ishopgo.com.exhibition.ui.chat.local.service.PushNotificationMessageReceiver
import ishopgo.com.exhibition.ui.chat.local.service.utils.PusherUtils
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asColor
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_local_chat_conversation.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuanhong on 5/31/18. HappyCoding!
 */
class ConversationActivity : BaseActivity() {

    companion object {
        private const val TAG = "ConversationActivity"
        private const val TYPING_TIMER_LENGTH = 600L
    }

    private lateinit var pusher: Pusher
    private val adapter = MessageAdapter()
    private var sendingPhotoUri: Uri? = null
    private var mTyping = false
    private var reloadData = false
    private var enableToChat = false
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: ConversationViewModel
    private lateinit var sharedViewModel: ShareChatViewModel

    private lateinit var conversationId: String
    private lateinit var conversationTitle: String
    private lateinit var conversationInfo: ConversationInfo

    private var isPusherConnected = false
    private val handler = Handler()

    private val onTypingTimeout = Runnable {
        if (!mTyping) return@Runnable

        mTyping = false
//        mSocket.emit("stop typing")
        Log.d(TAG, "stop typing")
    }

    private val connectionListener = object : ConnectionEventListener {
        @SuppressLint("SetTextI18n")
        override fun onConnectionStateChange(connectionStateChange: ConnectionStateChange) {
            val currentState = connectionStateChange.currentState
            when (currentState) {
                ConnectionState.CONNECTING -> {
                    Log.d(TAG, "onConnectionStateChange: connecting")
                    isPusherConnected = false
                    sharedViewModel.updateConnectionState(ShareChatViewModel.STATE_CONNECTING)
                }
                ConnectionState.CONNECTED -> {
                    Log.d(TAG, "onConnectionStateChange: connected")
                    isPusherConnected = true
                    sharedViewModel.updateConnectionState(ShareChatViewModel.STATE_CONNECTED)
                }
                else -> {
                    Log.d(TAG, "onConnectionStateChange: no connection $currentState")
                    isPusherConnected = false
                    sharedViewModel.updateConnectionState(ShareChatViewModel.STATE_DISCONNECTED)
                }
            }

        }

        override fun onError(s: String?, s1: String?, e: Exception?) {
            Log.d(TAG, "onError() called with: s = [$s], s1 = [$s1], e = [$e]")
        }

    }

    private val channelListener = object : PrivateChannelEventListener {
        override fun onEvent(channelName: String?, eventName: String?, data: String?) {
            Log.d(TAG, "onEvent: channelName = [${channelName}], eventName = [${eventName}], data = [${data}]")
            handler.post {
                val msg = try {
                    Toolbox.gson.fromJson(data, PusherChatMessage::class.java)
                } catch (e: Exception) {
                    null
                }

                msg?.let {
                    if (it.idConversation == conversationId) {
                        sharedViewModel.resolveMessage(it)
                    } else {
                        val intent = Intent(this@ConversationActivity, PushNotificationMessageReceiver::class.java)
                        intent.putExtra("type", "chat")
                        intent.putExtra("idConversion", conversationId)
                        intent.putExtra("content", it.apiContent)
                        intent.putExtra("title", it.name)
                        startService(intent)
                    }
                }
            }

        }

        override fun onAuthenticationFailure(messange: String?, exception: Exception?) {
            Log.d(TAG, "onAuthenticationFailure: messange = [$messange], exception = [$exception]")
            isPusherConnected = false
            viewModel.channelSubscribed(false)
        }

        override fun onSubscriptionSucceeded(channelName: String?) {
            Log.d(TAG, "onSubscriptionSucceeded: channelName = [$channelName]")
            viewModel.channelSubscribed(true)
        }

    }

    private var messageReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: context = [${context}], intent = [${intent}]")
            intent?.let {
                val idConversion = it.getStringExtra("idConversion")

                idConversion?.let {
                    resultCode = if (it == conversationId) {
                        // mark this message was processed and do not create notification
                        Log.d(TAG, "chat message was processed: ")
                        Activity.RESULT_CANCELED
                    } else {
                        // this message is not belong to this conversation, show notification
                        Log.d(TAG, "chat message was not processed: ")
                        Activity.RESULT_OK
                    }
                }
            }

        }

    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_chat_conversation)

        val application = application as? MyApp
        application?.pusher?.let {
            pusher = it
        }

        sharedViewModel = obtainViewModel(ShareChatViewModel::class.java)
        sharedViewModel.newMessage.observe(this, Observer { m ->
            m?.let {
                // we have new message
                if (m.idConversation == conversationId) {
                    scrollListener.resetState()

                    adapter.addData(0, it)

                    scrollToBottom()
                }
            }
        })
        sharedViewModel.connectionState.observe(this, Observer { m ->
            m?.let {
                when (it) {
                    ShareChatViewModel.STATE_CONNECTED -> {
                        view_notice.visibility = View.VISIBLE
                        view_notice.text = "Đã kết nối"
                        view_notice.setBackgroundColor(R.color.md_light_blue_400.asColor(view_notice.context))
                        view_notice.postDelayed({ view_notice.visibility = View.GONE }, 1500)
                    }
                    ShareChatViewModel.STATE_CONNECTING -> {
                        view_notice.visibility = View.VISIBLE
                        view_notice.text = "Đang kết nối..."
                        view_notice.setBackgroundColor(R.color.md_orange_A400.asColor(view_notice.context))
                    }
                    ShareChatViewModel.STATE_DISCONNECTED -> {
                        view_notice.visibility = View.VISIBLE
                        view_notice.text = "Mất kết nối"
                        view_notice.setBackgroundColor(R.color.md_deep_orange_A400.asColor(view_notice.context))
                    }
                    else -> {
                    }
                }
            }
        })


        viewModel = obtainViewModel(ConversationViewModel::class.java)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.channelSubscribed.observe(this, Observer { s ->
            s?.let {
                edt_comment.isEnabled = it
                enableToChat = it

            }
        })
        viewModel.messages.observe(this, Observer { m ->
            m?.let {
                if (reloadData) {
                    adapter.replaceAll(it)
                } else {
                    adapter.addAll(it)
                }

                // because we just want to load latest message, because of reverse layout so add scroll listener for first time here
                if (reloadData) {
                    scrollToBottom()

                    view_recyclerview.post {
                        view_recyclerview.clearOnScrollListeners()
                        view_recyclerview.addOnScrollListener(scrollListener)
                        reloadData = false
                    }
                }
            }
        })
        viewModel.patternCreated.observe(this, Observer { p ->
            p?.let {
                Toast.makeText(this, "Tạo thành công", Toast.LENGTH_SHORT).show()
                edt_comment.setText(it.content ?: "")
            }
        })
        viewModel.patternUpdated.observe(this, Observer { ok ->
            if (ok == true) Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show()
        })
        viewModel.patternRemoved.observe(this, Observer { ok ->
            if (ok == true) Toast.makeText(this, "Xoá thành công", Toast.LENGTH_SHORT).show()
        })
        viewModel.conversationInfo.observe(this, Observer { info ->
            info?.let {
                conversationInfo = it
                updateInfo(conversationInfo)
            }
        })

        if (intent?.hasExtra(Const.TransferKey.EXTRA_CONVERSATION_ID) == true) {
            conversationId = intent?.getStringExtra(Const.TransferKey.EXTRA_CONVERSATION_ID) ?: ""
        } else if (intent?.hasExtra("idConversion") == true) {
            conversationId = intent?.getStringExtra("idConversion") ?: ""
        }
        conversationTitle = intent?.getStringExtra(Const.TransferKey.EXTRA_TITLE) ?: ""

        connectPusher()

        toolbar.setCustomTitle(conversationTitle)
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            onBackPressed()
        }
        toolbar.rightButton(R.drawable.ic_info_highlight_24dp)
        toolbar.setRightButtonClickListener {
            if (::conversationInfo.isInitialized) {
                val intent = Intent(this, ConversationInfoActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(conversationInfo))
                intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conversationId)
                startActivityForResult(intent, Const.RequestCode.RC_SHOW_DETAIL)
            }
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        view_recyclerview.setHasFixedSize(true)
        view_recyclerview.layoutManager = layoutManager
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                reloadData = false
                viewModel.getMessages(conversationId, adapter.getItem(totalItemsCount - 1).getMessageId())
            }

        }
        view_recyclerview.adapter = adapter
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


        smoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        edt_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mTyping) {
                    mTyping = true
//                    mSocket.emit("Typing")
                    Log.d(TAG, "typing: ")
                }

                edt_comment.handler.removeCallbacks(onTypingTimeout)
                edt_comment.handler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH)
            }

        })

        btn_send.setOnClickListener {
            if (!enableToChat) return@setOnClickListener

            val text = edt_comment.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendTextMessage(conversationId, text)
                edt_comment.setText("")
            }
        }

        attach_extra.setOnClickListener {
            if (!enableToChat) return@setOnClickListener

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
            fragment.show(supportFragmentManager, "AddAttachmentBottomSheet")
        }

        viewModel.getConversationInfo(conversationId)
        // start loading message
        reloadData = true
        viewModel.getMessages(conversationId, -1L)
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter(Const.Chat.BROADCAST_NOTIFICATION)
        registerReceiver(messageReceiver, filter)
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(messageReceiver)
    }

    private fun selectTextPattern() {
        showPatternChooser()
    }

    private fun selectImagesFromInventory() {
        startActivityForResult(Intent(this, ImageInventoryActivity::class.java), Const.RequestCode.RC_PICK_IMAGES)
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
                return
            }
            photoFile.let {
                val photoURI = FileProvider.getUriForFile(context, getString(R.string.file_provider_authority), it)
                sendingPhotoUri = photoURI
                Log.d(TAG, "create file temp: $photoURI")

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, Const.RequestCode.RC_CAPTURE_IMAGE)
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
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Const.RequestCode.CAMERA_PERMISSION) {
            val hasCameraPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (hasCameraPermission) {
                getImageFromCamera(this)
            }
        }
    }

    private fun showPatternChooser() {
        val fragment = PatternChooserBottomSheet()
        fragment.patternChooserListener = object : PatternChooserBottomSheet.PatternChooserListener {
            override fun addPattern() {
                showAddPatternDialog()
            }

            override fun submitPattern(pattern: TextPattern) {
                edt_comment.setText(pattern.content ?: "")
            }

            override fun editPattern(pattern: TextPattern) {
                showEditPatternDialog(pattern)
            }

            override fun removePattern(pattern: TextPattern) {
                showConfirmRemovePatternDialog(pattern)
            }

            private fun showAddPatternDialog() {
                MaterialDialog.Builder(this@ConversationActivity)
                        .title("Thêm mẫu tin nhắn")
                        .input("Nội dung tin nhắn mẫu", null, false) { _, input ->
                            viewModel.addPattern(conversationId, input.toString())
                        }
                        .positiveText("OK")
                        .negativeText("Huỷ")
                        .show()
            }

            private fun showEditPatternDialog(pattern: TextPattern) {
                MaterialDialog.Builder(this@ConversationActivity)
                        .title("Sửa tin nhắn mẫu")
                        .input(pattern.content, null, false) { _, input ->
                            run {
                                val newPattern = TextPattern()
                                newPattern.id = pattern.id
                                newPattern.content = input.toString()
                                viewModel.updatePattern(conversationId, newPattern)
                            }
                        }
                        .positiveText("OK")
                        .negativeText("Huỷ")
                        .show()
            }

            private fun showConfirmRemovePatternDialog(pattern: TextPattern) {
                MaterialDialog.Builder(this@ConversationActivity)
                        .title("Xoá tin nhắn mẫu ?")
                        .content(pattern.content ?: "")
                        .positiveText("OK")
                        .onPositive { _, _ -> viewModel.removePattern(conversationId, pattern.id) }
                        .negativeText("Huỷ")
                        .show()
            }

        }
        fragment.show(supportFragmentManager, "PatternChooserBottomSheet")
    }

    private fun leave() {
        Log.d(TAG, "leave: ")
        // disconnect pusher
        if (::pusher.isInitialized) {
            pusher.unsubscribe("private-new-channel-${UserDataManager.currentUserId}")
            pusher.connection.unbind(ConnectionState.ALL, connectionListener)
            pusher.disconnect()
        }

    }

    override fun onDestroy() {
        leave()
        super.onDestroy()
    }

    private fun connectPusher() {
        pusher.connect(connectionListener, ConnectionState.ALL)
        registerChannel("private-new-channel-${UserDataManager.currentUserId}")
    }

    // register a channel to start listening to
    private fun registerChannel(channel: String) {
        when {
            PusherUtils.isPrivateChannel(channel) -> subscribePrivate(channel)
            else -> {
                // ignored
            }
        }

    }

    private fun subscribePrivate(channel: String) {
        if (pusher.getPrivateChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed")
            if (isPusherConnected)
                internalSubscribePrivate(channel)
            else {
                // try again after 1 second
                Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                handler.postDelayed({ subscribePrivate(channel) }, 1000)
            }
        }
    }

    private fun internalSubscribePrivate(channel: String) {
        Log.d(TAG, "Subscribing channel: $channel")
        pusher.subscribePrivate(channel, channelListener, "new-chat")
    }

    private fun updateInfo(info: ConversationInfo) {
        toolbar.setCustomTitle(info.title ?: "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.RequestCode.RC_PICK_IMAGE -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        viewModel.sendImageMessage(conversationId, it)
                    }
                }
                Const.RequestCode.RC_PICK_IMAGES -> {
                    val urls = data?.getStringArrayExtra(Const.TransferKey.EXTRA_STRING_LIST)
                    val uris = mutableListOf<Uri>()
                    urls?.mapTo(uris, { s -> Uri.parse(s) })
                    Log.d(TAG, "picked images = $uris")
                    if (uris.isNotEmpty()) {
                        viewModel.sendImagesFromInventory(conversationId, uris)
                    }
                }
                Const.RequestCode.RC_CAPTURE_IMAGE -> {
                    sendingPhotoUri?.let {
                        viewModel.sendImageMessage(conversationId, it)

                        sendingPhotoUri = null
                    }
                }
                else -> {
                }
            }
        }

        viewModel.getConversationInfo(conversationId)
    }

    private fun scrollToBottom() {
        view_recyclerview.post {
            smoothScroller.targetPosition = 0
            view_recyclerview.layoutManager.startSmoothScroll(smoothScroller)
        }
    }
}