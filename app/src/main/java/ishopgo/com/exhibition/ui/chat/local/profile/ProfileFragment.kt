package ishopgo.com.exhibition.ui.chat.local.profile

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import kotlinx.android.synthetic.main.content_local_chat_profile.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class ProfileFragment : BaseActionBarFragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_profile
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.userData.observe(this, Observer { info ->
            info?.let {
                showDetail(it)
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

        activity?.let {
            val memberId = it.intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
            if (memberId == -1L)
                it.finish()
            else {
                viewModel.loadUserDetail(memberId)
            }
        }

    }

    private fun showDetail(info: UserInfoProvider) {
        Glide.with(view_cover.context)
//                .load(info.provideCover())
                .load(R.drawable.default_cover_background)
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                )
                .into(view_cover)
        Glide.with(view_avatar.context)
                .load(info.provideAvatar())
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        view_avatar.setBackgroundResource(R.color.md_grey_200)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        view_avatar.setBackgroundResource(0)
                        return false
                    }

                })
                .into(view_avatar)

        view_name.text = info.provideName()
        view_phone.text = info.providePhone()
        view_dob.text = info.provideDob()
        view_email.text = info.provideEmail()
        view_company.text = info.provideCompany()
        view_region.text = info.provideRegion()
        view_address.text = info.provideAddress()
        view_type.text = info.provideType()
        view_joined_date.text = info.provideJoinedDate()
        view_introduction.text = info.provideIntroduction()

        view_message.setOnClickListener {
            // start conversation
            val currentUserId = UserDataManager.currentUserId
            if (info is IdentityData && currentUserId != info.id) {
                val request = CreateConversationRequest()
                request.type = 1
                val members = mutableListOf<Long>()
                members.add(currentUserId)
                members.add(info.id)
                request.member = members
                viewModel.createConversation(request)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Thông tin thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

    }

}