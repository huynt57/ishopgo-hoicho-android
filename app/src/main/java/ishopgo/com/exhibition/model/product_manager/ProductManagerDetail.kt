package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Provider

class ProductManagerDetail : IdentityData() {
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
    @SerializedName("department")
    @Expose
    var departments: Brand? = null
    @SerializedName("categories")
    @Expose
    var categories: MutableList<Category>? = null
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
    @SerializedName("provider_name")
    @Expose
    var providerName: String? = null
    @SerializedName("provider_id")
    @Expose
    var providerId: Long = 0L

}