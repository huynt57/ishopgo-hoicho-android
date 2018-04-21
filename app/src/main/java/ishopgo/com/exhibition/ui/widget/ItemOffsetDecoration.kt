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
class ItemOffsetDecoration(var spaceInPixel: Int) : RecyclerView.ItemDecoration() {

    constructor(@NonNull context: Context, @DimenRes spaceDimenRes: Int) : this(context.resources.getDimensionPixelSize(spaceDimenRes))

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect?.set(spaceInPixel, spaceInPixel, spaceInPixel, spaceInPixel)
    }
}