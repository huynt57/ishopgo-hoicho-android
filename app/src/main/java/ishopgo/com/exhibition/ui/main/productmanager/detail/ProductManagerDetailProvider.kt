package ishopgo.com.exhibition.ui.main.productmanager.detail

import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.model.product_manager.ProductRelated

interface ProductManagerDetailProvider {
    fun provideName(): String
    fun provideTitle(): String
    fun provideDVT(): String
    fun provideCode(): String
    fun provideTTPrice(): String
    fun provideProviderPrice(): String
    fun providePrice(): String
    fun provideStatus(): Boolean
    fun provideMadeIn(): String
    fun provideTags(): String
    fun provideDescription(): String
    fun provideMetaDescription(): String
    fun provideCollectionProducts(): ProductRelated?
    fun provideProviderAccount(): Provider?
    fun provideImages(): List<String>
    fun provideDepartments(): List<Brand>?
    fun provideCategory(): List<Category>?
    fun provideLink(): String
    fun provideIsFeatured(): Boolean
    fun provideViewWholesale(): Boolean
    fun provideWholesaleFrom(): String
    fun provideWholesaleTo(): String
    fun provideWholesaleCountProduct(): String
    fun providerProviderName(): String

}