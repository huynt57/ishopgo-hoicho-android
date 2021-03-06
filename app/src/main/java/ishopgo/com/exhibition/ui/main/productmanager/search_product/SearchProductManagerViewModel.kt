package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.CertImages
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.FilterResult
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class SearchProductManagerViewModel : BaseApiViewModel() {
    var showThuongHieu = MutableLiveData<Boolean>()

    fun openSearchBrands() {
        showThuongHieu.postValue(true)
    }

    var showCertImages = MutableLiveData<Long>()

    fun openSearchCertImages(boothId:Long) {
        showCertImages.postValue(boothId)
    }

    var getCertImages = MutableLiveData<CertImages>()

    fun getCertImages(data: CertImages) {
        getCertImages.postValue(data)
    }

    var showSearchSp = MutableLiveData<Int>()

    fun openSearchSp(type: Int) {
        showSearchSp.postValue(type)
    }

    var getSpLienQuan = MutableLiveData<Product>()

    fun getDataSpLienQuan(data: Product) {
        getSpLienQuan.postValue(data)
    }

    var getSpVatTu = MutableLiveData<Product>()

    fun getDataSpVatTu(data: Product) {
        getSpVatTu.postValue(data)
    }

    var getSpGiaiPhap = MutableLiveData<Product>()

    fun getDataSpGiaiPhap(data: Product) {
        getSpGiaiPhap.postValue(data)
    }

    var searchKey = MutableLiveData<String>()

    fun search(key: String) {
        searchKey.postValue(key)
    }

    var showFilterSp = MutableLiveData<MutableList<FilterResult>>()

    fun openFilterSp(data: MutableList<FilterResult>) {
        showFilterSp.postValue(data)
    }

    var getFilterSp = MutableLiveData<MutableList<FilterResult>>()

    fun getFilterSp(data: MutableList<FilterResult>) {
        getFilterSp.postValue(data)
    }
}