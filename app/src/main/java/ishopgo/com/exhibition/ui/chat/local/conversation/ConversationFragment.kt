package ishopgo.com.exhibition.ui.chat.local.conversation

import android.annotation.TargetApi
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
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
import com.afollestad.materialdialogs.MaterialDialog
import com.pusher.client.Pusher
import com.pusher.client.channel.Channel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.MyApp
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.pattern.PatternChooserBottomSheet
import ishopgo.com.exhibition.ui.chat.local.imageinventory.ImageInventoryActivity
import ishopgo.com.exhibition.ui.chat.local.info.ConversationInfoActivity
import ishopgo.com.exhibition.ui.chat.local.service.PusherMessageReceiver
import ishopgo.com.exhibition.ui.chat.local.service.utils.PresenceChannelListener
import ishopgo.com.exhibition.ui.chat.local.service.utils.PublicChannelListener
import ishopgo.com.exhibition.ui.chat.local.service.utils.PusherUtils
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_local_chat_conversation.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuanhong on 5/31/18. HappyCoding!
 */
class ConversationFragment : BaseActionBarFragment() {

    companion object {
        private const val TAG = "ConversationFragment"
        private const val TYPING_TIMER_LENGTH = 600L

        fun newInstance(data: Bundle): ConversationFragment {
            val fragment = ConversationFragment()
            fragment.arguments = data

            return fragment
        }
    }

    private val adapter = MessageAdapter()
    private var sendingPhotoUri: Uri? = null
    private var mTyping = false
    private var isPusherConnected = false
    private lateinit var pusher: Pusher
    private lateinit var handler: Handler
    private val handlerThread = HandlerThread("message queue", Process.THREAD_PRIORITY_BACKGROUND)
    /** Keeps track of all channel we are listening to */
    private var mChannelNames = mutableListOf<String>()
    private var mSubscribedChannel = mutableListOf<Channel>()
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: ConversationViewModel
    private lateinit var shareViewModel: ShareChatViewModel

    private lateinit var conversationId: String
    private lateinit var conversationTitle: String
    private lateinit var conversationInfo: ConversationInfo

    private val onTypingTimeout = Runnable {
        if (!mTyping) return@Runnable

        mTyping = false
//        mSocket.emit("stop typing")
        Log.d(TAG, "stop typing")
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_conversation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle(conversationTitle)
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
        toolbar.rightButton(R.drawable.ic_info_green_24dp)
        toolbar.setRightButtonClickListener {
            context?.let {
                if (::conversationInfo.isInitialized) {
                    val intent = Intent(it, ConversationInfoActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(conversationInfo))
                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conversationId)
                    startActivity(intent)
                }
            }
        }

        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
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


        smoothScroller = object : LinearSmoothScroller(view.context) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        edt_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isPusherConnected) return

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
            val text = edt_comment.text.toString()
            if (text.isNotBlank())
                viewModel.sendTextMessage(conversationId, text)

            edt_comment.setText("")
        }

        attach_extra.setOnClickListener {
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

    private fun showPatternChooser() {
        context?.let {
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
                    context?.let {
                        MaterialDialog.Builder(it)
                                .title("Thêm mẫu tin nhắn")
                                .input("Nội dung tin nhắn mẫu", null, false) { _, input ->
                                    viewModel.addPattern(conversationId, input.toString())
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
                                        viewModel.updatePattern(conversationId, newPattern)
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
                                .onPositive { _, _ -> viewModel.removePattern(conversationId, pattern.id) }
                                .negativeText("Huỷ")
                                .show()
                    }
                }

            }
            fragment.show(childFragmentManager, "PatternChooserBottomSheet")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shareViewModel = obtainViewModel(ShareChatViewModel::class.java, true)
        shareViewModel.newMessage.observe(this, Observer { m ->
            m?.let {
                // we have new message
                if (m.idConversation == conversationId) {
                    adapter.addData(0, it)

                    scrollToBottom()
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
                    scrollToBottom()

                    view_recyclerview.post {
                        view_recyclerview.addOnScrollListener(scrollListener)
                        reloadData = false
                    }
                }
            }
        })
        viewModel.patternCreated.observe(this, Observer { p ->
            p?.let {
                toast("Tạo thành công")
                edt_comment.setText(it.content ?: "")
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
                updateInfo(conversationInfo)
            }
        })

        viewModel.getConversationInfo(conversationId)
        // start loading message
        reloadData = true
        viewModel.getMessages(conversationId, -1L)
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handlerThread.start()
        handler = Handler(handlerThread.looper)

        val arguments = arguments
        conversationTitle = arguments?.getString(Const.TransferKey.EXTRA_TITLE) ?: ""
        conversationId = arguments?.getString(Const.TransferKey.EXTRA_CONVERSATION_ID) ?: ""

        connectPusher()
    }

    private fun connectPusher() {
        val application = activity?.application as? MyApp
        application?.pusher?.let {
            pusher = it

            pusher.connect(object : ConnectionEventListener {
                override fun onConnectionStateChange(connectionStateChange: ConnectionStateChange) {
                    val currentState = connectionStateChange.currentState
                    when (currentState) {
                        ConnectionState.CONNECTING -> {
                            Log.d(TAG, "onConnectionStateChange: connecting")
                            isPusherConnected = false
                            activity?.runOnUiThread {
                                view_notice.visibility = View.VISIBLE
                                view_notice.text = "Đang kết nối..."
                                view_notice.setBackgroundColor(resources.getColor(R.color.md_orange_A400))
                            }
                        }
                        ConnectionState.CONNECTED -> {
                            Log.d(TAG, "onConnectionStateChange: connected")
                            isPusherConnected = true
                            registerChannel("private-new-channel-${UserDataManager.currentUserId}")
                            activity?.runOnUiThread {
                                view_notice.visibility = View.VISIBLE
                                view_notice.text = "Đã kết nối"
                                view_notice.setBackgroundColor(resources.getColor(R.color.md_light_blue_400))
                                view_notice.postDelayed({ view_notice.visibility = View.GONE }, 1500)
                            }
                        }
                        else -> {
                            Log.d(TAG, "onConnectionStateChange: no connection")
                            isPusherConnected = false
                            activity?.runOnUiThread {
                                if (activity?.isFinishing == false) {
                                    view_notice.visibility = View.VISIBLE
                                    view_notice.text = "Mất kết nối"
                                    view_notice.setBackgroundColor(resources.getColor(R.color.md_deep_orange_A400))
                                }
                            }
                        }
                    }

                }

                override fun onError(s: String?, s1: String?, e: java.lang.Exception?) {
                    Log.d(TAG, "onError() called with: s = [$s], s1 = [$s1], e = [$e]")
                }
            }, ConnectionState.ALL)
        }

    }

    private fun leave() {
        // disconnect pusher
        if (::pusher.isInitialized) {
            pusher.disconnect()

            // should destroy connection here
            mSubscribedChannel.filter { it.isSubscribed }.map { channel -> channel.unbind("new-chat", { _, _, _ -> }) }
            mSubscribedChannel.clear()

            // unsubscribe from channels
            mChannelNames.map { pusher.unsubscribe(it); Log.d(TAG, "unsubcribe channel: $it") }
        }

        handlerThread.quit()
    }


    override fun onDestroy() {
        leave()
        super.onDestroy()
    }

    private fun scrollToBottom() {
        view_recyclerview.post {
            smoothScroller.targetPosition = 0
            view_recyclerview.layoutManager.startSmoothScroll(smoothScroller)
        }
    }

    // register a channel to start listening to
    fun registerChannel(channel: String) {
        when {
            PusherUtils.isPrivateChannel(channel) -> subscribePrivate(channel)
            PusherUtils.isPublicChannel(channel) -> subscribePublic(channel)
            PusherUtils.isPresenceChannel(channel) -> subscribePresence(channel)
            else -> {
                // ignored
            }
        }

    }

    private fun subscribePublic(channel: String) {
        if (pusher.getChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed. Subscribing it")
            if (isPusherConnected)
                handler.post { internalSubscribePublic(channel) }
            else {
                // try again after 1 second
                Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                handler.postDelayed({ subscribePublic(channel) }, 1000)
            }
        }
    }

    private fun internalSubscribePublic(channel: String) {
        val channelListener = PublicChannelListener()
        val subscription = pusher.subscribe(channel, channelListener, "new-chat")
        mSubscribedChannel.add(subscription)
        mChannelNames.add(channel)
    }

    private fun subscribePrivate(channel: String) {
        if (pusher.getPrivateChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed")
            if (isPusherConnected)
                handler.post { internalSubscribePrivate(channel) }
            else {
                // try again after 1 second
                Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                handler.postDelayed({ subscribePrivate(channel) }, 1000)
            }
        }
    }

    private fun internalSubscribePrivate(channel: String) {
        Log.d(TAG, "Subscribing channel: $channel")
        val channelListener = object : PrivateChannelEventListener {
            override fun onEvent(channelName: String?, eventName: String?, data: String?) {
                Log.d(TAG, "onEvent $data")
                context?.let {
                    val intent = Intent(it, PusherMessageReceiver::class.java)
                    intent.putExtra(Const.Chat.EXTRA_MESSAGE, data)
                    intent.action = Const.Chat.PUSHER_MESSAGE
                    it.startService(intent)
                }
            }

            override fun onAuthenticationFailure(messange: String?, exception: Exception?) {
                Log.d(TAG, "onAuthenticationFailure: messange = [${messange}], exception = [${exception}]")
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                Log.d(TAG, "onSubscriptionSucceeded: channelName = [${channelName}]")
            }

        }
        val subscription = pusher.subscribePrivate(channel, channelListener, "new-chat")
        mSubscribedChannel.add(subscription)
        mChannelNames.add(channel)
    }

    private fun subscribePresence(channel: String) {
        if (pusher.getPresenceChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed. Subscribing it")

            if (isPusherConnected)
                handler.post { internalSubscribePresence(channel) }
            else {
                // try again after 1 second
                Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                handler.postDelayed({ subscribePresence(channel) }, 1000)
            }
        }
    }

    private fun internalSubscribePresence(channel: String) {
        val channelListener = PresenceChannelListener()
        val subscription = pusher.subscribePresence(channel, channelListener, "new-chat")
        mSubscribedChannel.add(subscription)
        mChannelNames.add(channel)
    }

}