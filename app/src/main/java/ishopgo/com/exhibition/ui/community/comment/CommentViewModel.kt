package ishopgo.com.exhibition.ui.community.comment

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class CommentViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var showCommentChild = MutableLiveData<CommunityComment>()

    fun showCommentChild(community: CommunityComment) {
        showCommentChild.postValue(community)
    }
}