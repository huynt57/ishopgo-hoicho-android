package ishopgo.com.exhibition.domain.response

class HistoryScan {
    var qrCode: QrCode? = null
    var barCode: BarCode? = null

    class QrCode {
        var productId: Long = -1L
        var productName: String = ""
        var productImage: String = ""
        var link: String = ""
        var code: String = ""
        var time: String = ""
    }

    class BarCode {
        var code: String = ""
        var time: String = ""
    }
}