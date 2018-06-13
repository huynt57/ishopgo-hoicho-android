package ishopgo.com.exhibition.ui.base.widget

/**
 * Created by xuanhong on 6/13/18. HappyCoding!
 */
interface Converter<FROM, TO> {
    fun convert(from: FROM): TO
}