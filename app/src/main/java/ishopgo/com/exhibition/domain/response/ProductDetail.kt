package ishopgo.com.exhibition.domain.response

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailProvider


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ProductDetail : IdentityData(), ProductDetailProvider {
    override fun provideShopAddress(): String {
        return "${booth?.address?.trim() ?:""}, ${booth?.district?.trim() ?:""}, ${booth?.city?.trim() ?:""}"
    }

    override fun provideLiked(): Boolean {
        return liked == LIKED
    }

    override fun provideProductLinkAffiliate(): String {
        return linkAffiliate ?: ""
    }

    override fun provideProductImage(): String {
        return image ?: ""
    }

    override fun provideProductName(): String {
        return name?.trim() ?: "unknown"
    }

    override fun provideProductPrice(): String {
        return price.asMoney()
    }

    override fun provideProductBrand(): String {
        if (department?.id == 0L) return ""
        return department?.name?.trim() ?: ""
    }

    override fun provideProductShortDescription(): String {
        return description ?: ""
    }

    override fun provideShopName(): String {
        return booth?.name?.trim() ?: ""
    }

    override fun provideShopRegion(): String {
        return booth?.address?.trim() ?: ""
    }

    override fun provideShopProductCount(): Int {
        return booth?.count ?: 0
    }

    override fun provideShopRateCount(): Int {
        return booth?.rate ?: 0
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

    override fun provideFollowed(): Boolean {
        return booth?.isFollowed() ?: false
    }

    companion object {
        const val LIKED = 1
    }

    @SerializedName("title")
    @Expose
    var title: String? = null
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
    @SerializedName("shares")
    @Expose
    var shares: Int = 0
    @SerializedName("likes")
    @Expose
    var likes: Int = 0
    @SerializedName("liked")
    @Expose
    val liked: Int = 0
    @SerializedName("followed")
    @Expose
    var followed: Int = 0
    @SerializedName("comments")
    @Expose
    var comments: Int = 0
    @SerializedName("department")
    @Expose
    var department: Department? = null
    @SerializedName("booth")
    @Expose
    var booth: Booth? = null
    @SerializedName("link_affiliate")
    @Expose
    var linkAffiliate: String? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
}