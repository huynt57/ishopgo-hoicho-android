package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.productmanager.detail.ProductManagerDetailProvider

class ProductManagerDetail : IdentityData(), ProductManagerDetailProvider {
    override fun provideWholesaleFrom(): String {
        return wholesalePriceFrom.toString()
    }

    override fun provideWholesaleTo(): String {
        return wholesalePriceTo.toString()
    }

    override fun provideWholesaleCountProduct(): String {
        return wholesaleCountProduct.toString()
    }

    override fun provideCategory(): List<Category>? {
        return categories ?: mutableListOf()
    }

    companion object {
        var STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        var STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dạng landing page

        var IS_FEATURED: Int = 1  //Sản phẩm nổi bật

        const val VIEW_WHOLESALE = 1
    }

    override fun provideViewWholesale(): Boolean {
        return viewWholesale == VIEW_WHOLESALE
    }

    override fun provideName(): String {
        return name ?: ""
    }

    override fun provideTitle(): String {
        return title ?: ""
    }

    override fun provideDVT(): String {
        return dvt ?: ""
    }

    override fun provideCode(): String {
        return code ?: ""
    }

    override fun provideTTPrice(): String {
        return ttPrice?.asMoney() ?: "0 đ"
    }

    override fun provideProviderPrice(): String {
        return providerPrice?.asMoney() ?: "0 đ"
    }

    override fun providePrice(): String {
        return price?.asMoney() ?: "0 đ"
    }

    override fun provideStatus(): String {
        var provideStatus = "Không hiển thị"
        if (status == STATUS_DISPLAY_SHOW) {
            provideStatus = "Hiển thị dạng chuẩn "
        }
        if (status == STATUS_DISPLAY_LANDING_PAGE)
            provideStatus = "Hiển thị dạng landing page"

        return provideStatus
    }

    override fun provideMadeIn(): String {
        return madeIn ?: ""
    }

    override fun provideTags(): String {
        return tags ?: ""
    }

    override fun provideDescription(): String {
        return description ?: ""
    }

    override fun provideMetaDescription(): String {
        return metaDescription ?: ""
    }

    override fun provideCollectionProducts(): ProductRelated? {
        return collectionProducts
    }

    override fun provideProviderAccount(): Provider? {
        return providerAccount
    }

    override fun provideImages(): List<String> {
        return images ?: mutableListOf()
    }

    override fun provideDepartments(): List<Brand>? {
        return departments ?: mutableListOf()
    }

    override fun provideLink(): String {
        return link ?: ""
    }

    override fun provideIsFeatured(): Boolean {
        return wasIsFeatured()
    }

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("provider_price")
    @Expose
    var providerPrice: Long? = null
    @SerializedName("dvt")
    @Expose
    var dvt: String? = null
    @SerializedName("madeIn")
    @Expose
    var madeIn: String? = null
    @SerializedName("tags")
    @Expose
    var tags: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("ck_mh")
    @Expose
    var ckMh: String? = null
    @SerializedName("ck_bl")
    @Expose
    var ckBl: String? = null
    @SerializedName("local_image")
    @Expose
    var localImage: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("price")
    @Expose
    var price: Long? = null
    @SerializedName("tt_price")
    @Expose
    var ttPrice: Long? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("meta")
    @Expose
    var meta: List<String>? = null
    @SerializedName("meta_detail")
    @Expose
    var metaDetail: Any? = null
    @SerializedName("meta_description")
    @Expose
    var metaDescription: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("parent_id")
    @Expose
    var parentId: Long? = null
    @SerializedName("collection_id")
    @Expose
    var collectionId: Int? = null
    @SerializedName("collection_products")
    @Expose
    var collectionProducts: ProductRelated? = null
    @SerializedName("provider_account")
    @Expose
    var providerAccount: Provider? = null
    @SerializedName("images")
    @Expose
    var images: List<String>? = null
    @SerializedName("departments")
    @Expose
    var departments: List<Brand>? = null
    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null
    @SerializedName("relate")
    @Expose
    var relate: List<Any>? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("link_affiliate")
    @Expose
    var linkAffiliate: String? = null
    @SerializedName("is_featured")
    @Expose
    var is_featured: Int = 0
    @SerializedName("wholesale_price_from")
    @Expose
    var wholesalePriceFrom: Long = 0
    @SerializedName("wholesale_price_to")
    @Expose
    var wholesalePriceTo: Long = 0
    @SerializedName("wholesale_count_product")
    @Expose
    var wholesaleCountProduct: Int = 0
    @SerializedName("view_wholesale")
    @Expose
    var viewWholesale: Int? = null

    fun wasIsFeatured() = is_featured == IS_FEATURED
}