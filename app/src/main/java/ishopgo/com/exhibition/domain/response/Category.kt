package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class Category : IdentityData() {
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
    @SerializedName("image")
    var image: String? = null
}
