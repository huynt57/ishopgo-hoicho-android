package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ProductDetail : IdentityData() {
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
    @SerializedName("promotion_price")
    @Expose
    var promotionPrice: Long = 0
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
    @SerializedName("mua_vu")
    @Expose
    var muaVu: String? = null
    @SerializedName("dong_goi")
    @Expose
    var dongGoi: String? = null
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
    @SerializedName("link_share")
    @Expose
    var linkShare: String? = null
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
    var wholesaleCountProduct: String? = null
    @SerializedName("view_wholesale")
    @Expose
    var viewWholesale: Int? = null
    @SerializedName("desc")
    @Expose
    var process: List<ProductProcess>? = null
    @SerializedName("rate")
    @Expose
    var rate: Int? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
    @SerializedName("madeIn")
    @Expose
    var madeIn: String? = null
    @SerializedName("is_nhatky_sx")
    @Expose
    var isNhatkySx: Int? = null
    @SerializedName("is_show_date")
    @Expose
    var isShowDate: Int? = null
    @SerializedName("ngay_sx")
    @Expose
    var ngaySx: String? = null
    @SerializedName("dk_thuhoach")
    @Expose
    var dkThuhoach: String? = null
    @SerializedName("ms_lohang")
    @Expose
    var msLohang: String? = null
    @SerializedName("is_ms_lohang")
    @Expose
    var isMsLohang: Int? = null
    @SerializedName("quy_mo")
    @Expose
    var quyMo: String? = null
    @SerializedName("san_luong")
    @Expose
    var sanLuong: String? = null
    @SerializedName("info")
    @Expose
    var info: List<InfoProduct>? = null
    @SerializedName("ms_sanxuat")
    @Expose
    var msSanxuat: String? = null
    @SerializedName("hinhthuc_vc")
    @Expose
    var hinhThucVc: String? = null
    @SerializedName("ngay_vc")
    @Expose
    var ngayVc: String? = null
    @SerializedName("donvi_vc")
    @Expose
    var donviVc: String? = null
    @SerializedName("vc_note")
    @Expose
    var vcNote: String? = null
}