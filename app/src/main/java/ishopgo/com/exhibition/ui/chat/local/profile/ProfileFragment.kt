package ishopgo.com.exhibition.ui.chat.local.profile

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
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
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.userData.observe(this, Observer { info ->
            info?.let {
                showDetail(it)
            }
        })

        viewModel.deleteSusscess.observe(this, Observer {
            toast("Xoá thành công")
            hideProgressDialog()
            activity?.setResult(RESULT_OK)
            activity?.finish()
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Thông tin thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentType == "Chủ hội chợ") {
            toolbar.rightButton(R.drawable.ic_delete_green_24dp)
            toolbar.setRightButtonClickListener {
                context?.let {
                    MaterialDialog.Builder(it)
                            .content("Bạn có muốn xoá thành viên này không?")
                            .positiveText("Có")
                            .onPositive { _, _ ->
                                activity?.let {
                                    val memberId = it.intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
                                    viewModel.deleteMember(memberId)
                                }
                                showProgressDialog()
                            }
                            .negativeText("Không")
                            .onNegative { dialog, _ -> dialog.dismiss() }
                            .show()
                }
            }
        }
    }
}