package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.asPhone
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailProvider


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ProductDetail : IdentityData(), ProductDetailProvider {
    override fun provideWholesaleLimit(): CharSequence {
        return "Mua tối thiểu <b><font color=\"red\">$wholesaleCountProduct</font></b> sản phẩm".asHtml()
    }

    override fun provideWholesale(): CharSequence {
        return "<b>Giá bán sỉ: Từ <font color=\"red\">${wholesalePriceFrom.asMoney()}</font> tới <font color=\"red\">${wholesalePriceTo.asMoney()}</font></b>".asHtml()
    }

    override fun provideViewWholesale(): Boolean {
        return viewWholesale == VIEW_WHOLESALE
    }

    override fun provideShopAddress(): CharSequence {
        return "${booth?.address?.trim() ?: ""}, ${booth?.district?.trim()
                ?: ""}, ${booth?.city?.trim() ?: ""}"
    }

    override fun provideLiked(): Boolean {
        return liked == LIKED
    }

    override fun provideProductLinkAffiliate(): CharSequence {
        return linkAffiliate ?: ""
    }

    override fun provideProductImage(): CharSequence {
        return image ?: ""
    }

    override fun provideProductName(): CharSequence {
        return name?.trim() ?: "unknown"
    }

    override fun provideProductPrice(): CharSequence {
        return "<b>Giá bán lẻ: Từ <font color=\"#00c853\">${price.asMoney()}</font></b>".asHtml()
    }

    override fun provideProductBrand(): CharSequence {
        if (department?.id == 0L) return ""
        return department?.name?.trim() ?: ""
    }

    override fun provideProductShortDescription(): CharSequence {
        return description ?: ""
    }

    override fun provideShopName(): CharSequence {
        return booth?.name?.trim() ?: ""
    }

    override fun provideShopRegion(): CharSequence {
        return booth?.address?.trim() ?: ""
    }

    override fun provideShopProductCount(): Int {
        return booth?.count ?: 0
    }

    override fun provideShopRateCount(): Int {
        return booth?.rate ?: 0
    }

    override fun provideShopPhone(): CharSequence {
        return booth?.hotline?.asPhone() ?: ""
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
        const val VIEW_WHOLESALE = 1
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
}