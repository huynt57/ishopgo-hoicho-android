package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ItemOffsetDecoration(var spaceInPixel: Int, var hasVerticalPadding: Boolean = true, var hasHorizontalPadding: Boolean = true) : RecyclerView.ItemDecoration() {

    constructor(
            @NonNull context: Context,
            @DimenRes spaceDimenRes: Int,
            hasVerticalPadding: Boolean = true,
            hasHorizontalPadding: Boolean = true
    ) : this(
            context.resources.getDimensionPixelSize(spaceDimenRes),
            hasVerticalPadding,
            hasHorizontalPadding
    )

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect?.set(
                if (hasHorizontalPadding) spaceInPixel else 0,
                if (hasVerticalPadding) spaceInPixel else 0,
                if (hasHorizontalPadding) spaceInPixel else 0,
                if (hasVerticalPadding) spaceInPixel else 0
        )
    }
}