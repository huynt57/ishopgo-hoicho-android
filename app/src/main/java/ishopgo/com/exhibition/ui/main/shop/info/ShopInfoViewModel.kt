package ishopgo.com.exhibition.ui.main.shop.info

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopInfoViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var info = MutableLiveData<ShopInfoProvider>()

    fun loadInfo(shopId: Long) {
        info.postValue(object : ShopInfoProvider {
            override fun provideImage(): String {
                return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-c7acb6baf71b0e474f0b94dd957d2401medi-agingjpg.jpg"
            }

            override fun provideJoinedDate(): String {
                return "28/05/2018"
            }

            override fun providePIC(): String {
                return "Vương Xuân Hồng"
            }

            override fun provideName(): String {
                return "Công ty TNHH Mua đồ tốt"
            }

            override fun provideProductCount(): Int {
                return 10
            }

            override fun provideRegion(): String {
                return "Hồ Chí Minh"
            }

            override fun provideRating(): Int {
                return 4
            }

            override fun provideClickCount(): Int {
                return 20
            }

            override fun provideShareCount(): Int {
                return 30
            }

            override fun provideDescription(): String {
                return "Công ty chuyên cung cấp mỹ phẩm nhập khẩu hàng đầu Việt Nam"
            }

            override fun provideSalePoints(): List<SalePointProvider> {
                val salePoints = mutableListOf<SalePointProvider>()
                for (i in 0..3) {
                    salePoints.add(object : SalePointProvider {
                        override fun provideName(): String {
                            return "Chi nhánh miền Bắc"
                        }

                        override fun provideRegion(): String {
                            return "Hồ Nội"
                        }

                        override fun provdeDistance(): String {
                            return "1.800 km"
                        }

                    })
                }

                return salePoints
            }

        })
    }

}