package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class ProductsByCategoryViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var childCategories = MutableLiveData<List<CategoryProvider>>()

    fun loadChildCategory(categoryId: Long) {
        val dummy = mutableListOf<CategoryProvider>()
        for (i in 0..15)
            dummy.add(object : IdentityData(), CategoryProvider {

                init {
                    id = i.toLong()
                }

                override fun provideIcon(): String {
                    return "https://marketplace.canva.com/MAB60SfE6BE/1/thumbnail/canva-jacket-cloth-fashion-winter-cold-icon.-vector-graphic-MAB60SfE6BE.png"
                }

                override fun provideName(): String {
                    return "Quần áo"
                }

                override fun provideChilds(): List<CategoryProvider> {
                    val child = mutableListOf<CategoryProvider>()
                    for (i in 0..4)
                        child.add(object : IdentityData(), CategoryProvider {
                            override fun provideIcon(): String {
                                return "https://marketplace.canva.com/MAB60SfE6BE/1/thumbnail/canva-jacket-cloth-fashion-winter-cold-icon.-vector-graphic-MAB60SfE6BE.png"
                            }

                            override fun provideName(): String {
                                return "Trang sức"
                            }

                            override fun provideChilds(): List<CategoryProvider> {
                                return listOf()
                            }

                            override fun provideIsParent(): Boolean {
                                return false
                            }

                        })
                    return child
                }

                override fun provideIsParent(): Boolean {
                    return true
                }

            })

        childCategories.postValue(dummy)
    }

    var products = MutableLiveData<List<ProductProvider>>()

    fun loadProductsByCategory(categoryId: Long) {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..20)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        products.postValue(dummy)
    }


}