package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.product_manager.ProductRelated


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ProductDetail : IdentityData() {
    @SerializedName("tags")
    @Expose
    var tags: String? = null
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
    var department: Brand? = null
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
    @SerializedName("dvt")
    @Expose
    var dvt: String? = null
    @SerializedName("ngay_donggoi")
    @Expose
    var ngayDonggoi: String? = null

    @SerializedName("provider_price")
    @Expose
    var providerPrice: String? = null
    @SerializedName("is_featured")
    @Expose
    var isFeatured: Int? = null
    @SerializedName("local_image")
    @Expose
    var localImage: Any? = null
    @SerializedName("department_id")
    @Expose
    var departmentId: Int? = null
    @SerializedName("meta")
    @Expose
    var meta: List<Any>? = null
    @SerializedName("meta_detail")
    @Expose
    var metaDetail: Any? = null
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
    @SerializedName("collection_products")
    @Expose
    var collectionProducts: ProductRelated? = null
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
    @SerializedName("dm_cap_0_shop")
    @Expose
    var dmCap0Shop: Long? = null
    @SerializedName("dm_cap_1_shop")
    @Expose
    var dmCap1Shop: Long? = null
    @SerializedName("dm_cap_2_shop")
    @Expose
    var dmCap2Shop: Long? = null
    @SerializedName("dm_cap_3_shop")
    @Expose
    var dmCap3Shop: Long? = null
    @SerializedName("dm_cap_4_shop")
    @Expose
    var dmCap4Shop: Long? = null
    @SerializedName("is_baotieu")
    @Expose
    var isBaoTieu: Int? = null
    @SerializedName("hsd")
    @Expose
    var hsd: String? = null
    @SerializedName("xuat_xuong")
    @Expose
    var xuatXuong: String? = null
    @SerializedName("cang_xuat")
    @Expose
    var cangXuat: String? = null
    @SerializedName("cang_nhap")
    @Expose
    var cangNhap: String? = null
    @SerializedName("xuat_xuong_date")
    @Expose
    var xuatXuongdate: String? = null
    @SerializedName("nhap_hang_date")
    @Expose
    var nhapHangdate: String? = null
    @SerializedName("sl_nhap")
    @Expose
    var slNhap: String? = null
    @SerializedName("pp")
    @Expose
    var pp: Booth? = null
    @SerializedName("nnk")
    @Expose
    var nnk: Booth? = null
    @SerializedName("cscb")
    @Expose
    var cscb: Booth? = null
    @SerializedName("nsx_id")
    @Expose
    var nsxId: Long? = null
    @SerializedName("nk_id")
    @Expose
    var nkId: Long? = null
    @SerializedName("cs_chebien_id")
    @Expose
    var csChebienId: Long? = null
    @SerializedName("cert_images")
    @Expose
    var certImages: List<ListCert>? = null
    @SerializedName("relate_shops")
    @Expose
    var relateShops: List<BoothManager>? = null
    @SerializedName("chung_nhan")
    @Expose
    var chungNhan: String? = null

    class ListCert {
        @SerializedName("image")
        @Expose
        var image: String? = null
    }
}