package ishopgo.com.exhibition.ui.main.product

/**
 * Created by xuanhong on 6/15/18. HappyCoding!
 */
class DummyProductProvider: ProductProvider {
    override fun provideImage(): String {
        return ""
    }

    override fun provideName(): String {
        return "Tên sản phẩm"
    }

    override fun providePrice(): String {
        return "100.000 đ"
    }

    override fun provideMarketPrice(): String {
        return "120.000 đ"
    }

}