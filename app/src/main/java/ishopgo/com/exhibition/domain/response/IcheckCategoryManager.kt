package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckCategoryManager {
    @SerializedName("category")
    var category: IcheckCategoryParent? = null

    class IcheckCategoryParent {
        @SerializedName("roots")
        var roots: List<IcheckCategory>? = null
    }
}

