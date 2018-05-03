package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailProvider


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ProductDetail : IdentityData(), ProductDetailProvider {
    override fun provideProductImage(): String {
        return image ?: ""
    }

    override fun provideProductName(): String {
        return name ?: "unknown"
    }

    override fun provideProductPrice(): String {
        return ttPrice.asMoney()
    }

    override fun provideProductBrand(): String {
        return department?.name ?: ""
    }

    override fun provideProductShortDescription(): String {
        return description ?: ""
    }

    override fun provideShopName(): String {
        return booth?.name ?: ""
    }

    override fun provideShopRegion(): String {
        return booth?.address ?: ""
    }

    override fun provideShopProductCount(): Int {
        return booth?.count ?: 0
    }

    override fun provideShopRateCount(): Int {
        return rate
    }

    override fun provideShopPhone(): String {
        return booth?.hotline ?: ""
    }

    override fun provideProductLikeCount(): Int {
        return likes
    }

    override fun provideProductCommentCount(): Int {
        return comments
    }

    override fun provideProductShareCount(): Int {
        return shares
    }

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("provider_price")
    @Expose
    var providerPrice: Long = 0
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
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("price")
    @Expose
    var price: Long = 0
    @SerializedName("tt_price")
    @Expose
    var ttPrice: Long = 0
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("provider_id")
    @Expose
    var providerId: Int? = null
    @SerializedName("shares")
    @Expose
    var shares: Int = 0
    @SerializedName("likes")
    @Expose
    var likes: Int = 0
    @SerializedName("comments")
    @Expose
    var comments: Int = 0
    @SerializedName("rate")
    @Expose
    var rate: Int = 0
    @SerializedName("images")
    @Expose
    var images: List<String>? = null
    @SerializedName("department")
    @Expose
    var department: Department? = null
    @SerializedName("booth")
    @Expose
    var booth: Booth? = null
    @SerializedName("link_affiliate")
    @Expose
    var linkAffiliate: String? = null

}