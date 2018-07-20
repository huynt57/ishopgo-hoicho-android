package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.IdentityData

class ProductManagerDetail : IdentityData() {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("provider_price")
    @Expose
    var providerPrice: Any? = null
    @SerializedName("dvt")
    @Expose
    var dvt: String? = null
    @SerializedName("madeIn")
    @Expose
    var madeIn: String? = null
    @SerializedName("tags")
    @Expose
    var tags: String? = null
    @SerializedName("is_featured")
    @Expose
    var isFeatured: Int? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("local_image")
    @Expose
    var localImage: Any? = null
    @SerializedName("department_id")
    @Expose
    var departmentId: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("price")
    @Expose
    var price: Int? = null
    @SerializedName("promotion_price")
    @Expose
    var promotionPrice: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("meta")
    @Expose
    var meta: List<Any>? = null
    @SerializedName("meta_detail")
    @Expose
    var metaDetail: Any? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("parent_id")
    @Expose
    var parentId: Int? = null
    @SerializedName("collection_id")
    @Expose
    var collectionId: Int? = null
    @SerializedName("provider_id")
    @Expose
    var providerId: Long? = null
    @SerializedName("meta_description")
    @Expose
    var metaDescription: String? = null
    @SerializedName("wholesale_price_from")
    @Expose
    var wholesalePriceFrom: Any? = null
    @SerializedName("wholesale_price_to")
    @Expose
    var wholesalePriceTo: Any? = null
    @SerializedName("wholesale_count_product")
    @Expose
    var wholesaleCountProduct: String? = null
    @SerializedName("view_wholesale")
    @Expose
    var viewWholesale: Int? = null
    @SerializedName("collection_products")
    @Expose
    var collectionProducts: ProductRelated? = null
    @SerializedName("images")
    @Expose
    var images: List<String>? = null
    @SerializedName("relate")
    @Expose
    var relate: List<Any>? = null
    @SerializedName("department")
    @Expose
    var department: Brand? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("provider_name")
    @Expose
    var providerName: String? = null
    @SerializedName("provider_phone")
    @Expose
    var providerPhone: String? = null
    @SerializedName("categories")
    @Expose
    var categories: MutableList<Category>? = null
    @SerializedName("link_affiliate")
    @Expose
    var linkAffiliate: String? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null
}