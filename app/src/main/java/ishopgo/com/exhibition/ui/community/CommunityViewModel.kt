package ishopgo.com.exhibition.ui.community

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityViewModel : BaseListViewModel<List<CommunityProvider>>(), AppComponent.Injectable {
    override fun loadData(params: RequestParams) {
        val dummy = mutableListOf<CommunityProvider>()
        for (i in 0..10)
            dummy.add(object : IdentityData(), CommunityProvider {
                init {
                    id = i.toLong()
                }

                override fun userName(): String {
                    return "Nguyễn Huy Hoàng"
                }

                override fun userAvatar(): String {
                    return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlNQ2yyZICBI_pcqc_KxT55FYvlpE8TtOKTlVuNuVyjbr1BSwLhw"
                }

                override fun currentUserAvatar(): String {
                    return "http://www.urhobosocialclublagos.com/wp-content/uploads/2017/07/default-avatar-ginger-guy.png"
                }

                override fun communityContent(): String {
                    return "Xin chào"
                }

                override fun communityTime(): String {
                    return "23/04/2018 4:02:50"
                }

                override fun communityLike(): Int {
                    return 10
                }

                override fun communityComment(): Int {
                    return 5
                }

                override fun communityShare(): Int {
                    return 15
                }

                override fun communityProductName(): String {
                    return "Mỹ phẩm làm đẹp da Hồng Thảo"
                }

                override fun communityProductCode(): String {
                    return "0123456789"
                }

                override fun communityProductPrice(): String {
                    return "1.000.000đ"
                }

                override fun communityProductImage(): String {
                    return "http://admin.bigmua.com/public/media/media/pic112014/kem-tri-seo-ro-medi-white-medi-scar-4(2).jpg"
                }

                override fun communityProductListImage(): MutableList<String>? {
                    return null
                }
            })

        dataReturned.postValue(dummy)
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}