package ishopgo.com.exhibition.ui.main.shop.category

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.category.CategoryProvider

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class CategoryViewMode : BaseListViewModel<List<CategoryProvider>>(), AppComponent.Injectable {
    override fun loadData(params: RequestParams) {
        val dummy = mutableListOf<CategoryProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), CategoryProvider {

                init {
                    id = i.toLong()
                }

                override fun provideIcon(): String {
                    return "http://theme.hstatic.net/1000122548/1000237689/14/leftmenu_icon_4.png?v=127"
                }

                override fun provideName(): String {
                    return "Quần áo"
                }

                override fun provideChilds(): List<CategoryProvider> {
                    val child = mutableListOf<CategoryProvider>()
                    for (i in 0..4)
                        child.add(object : IdentityData(), CategoryProvider {
                            override fun provideIcon(): String {
                                return "http://theme.hstatic.net/1000122548/1000237689/14/leftmenu_icon_4.png?v=127"
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


        dataReturned.postValue(dummy)
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}