package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider

class Category : IdentityData(), CategoryProvider {
    override fun provideIcon(): String {
        return ""
    }

    override fun provideName(): String {
        return name ?: "unknown"
    }

    override fun provideChilds(): List<CategoryProvider> {
        return subs ?: mutableListOf()
    }

    override fun provideIsParent(): Boolean {
        return subs != null
    }

    @SerializedName("name")
    var name: String? = null
    @SerializedName("subCate")
    var subs: List<Category>? = null

}
