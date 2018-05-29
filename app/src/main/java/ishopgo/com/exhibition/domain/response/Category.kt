package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider

class Category : IdentityData(), CategoryProvider {
    override fun provideCount(): Int {
        return count ?: 0
    }

    override fun provideLevel(): Int {
        return level
    }

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
        return level == 1
    }

    var level: Int = 1
        set(value) {
            field = value
            subs?.onEach {
                it.level = field + 1

                // prevent recursive problem because of gson
                val p = Category()
                p.id = id
                p.name = name
                it.parent = p
            }
        }

    var parent: Category? = null

    @SerializedName("name")
    var name: String? = null
    @SerializedName("count")
    var count: Int? = 0
    @SerializedName("subCate")
    var subs: List<Category>? = null
}
