package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class Category: IdentityData() {

    @SerializedName("name")
    var name: String? = null
    @SerializedName("subCate")
    var subs: List<Category>? = null

    var isExpanding = false
    var level: Int = 0
        set(value) {
            field = value
            subs?.onEach { category -> category.level = field + 1 }
        }

    fun hasSub(): Boolean = subs != null && subs!!.isNotEmpty()

    override fun toString(): String {
        return id.toString() + ": " + id + ", name:" + name + ", subs count:" + if (subs != null) subs!!.size else 0
    }
}
