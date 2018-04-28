package ishopgo.com.exhibition.ui.main.product.detail.comment

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ProductCommentViewModel : BaseListViewModel<List<ProductCommentProvider>>(), AppComponent.Injectable {

    override fun loadData(params: RequestParams) {
        val dummy = mutableListOf<ProductCommentProvider>()
        for (i in 0..19)
            dummy.add(object : IdentityData(), ProductCommentProvider {
                override fun provideName(): String {
                    return "Nguyễn Phương Thảo"
                }

                override fun provideAvatar(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideTime(): String {
                    return "1 giờ trước"
                }

                override fun provideContent(): String {
                    return "Sản phẩm dùng rất tốt, có hiệu quả ngay lần sử dụng thứ 2. Sẽ còn quay lại shop."
                }

                init {
                    id = i.toLong()
                }

            })

        dataReturned.postValue(dummy)
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}