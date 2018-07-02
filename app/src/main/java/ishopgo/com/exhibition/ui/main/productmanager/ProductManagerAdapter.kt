package ishopgo.com.exhibition.ui.main.productmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.product_manager.ProductManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_product_manager.view.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript


/**
 * Created by xuanhong on 2/2/18. HappyCoding!
 */
class ProductManagerAdapter : ClickableAdapter<ProductManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductManager> {
        return Holder(v, ProductManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<ProductManager, ProductManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<ProductManager>(v) {

        override fun populate(data: ProductManager) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                if (convert.provideStatus() == STATUS_DISPLAY_HIDDEN) {
                    img_blur.visibility = View.VISIBLE
                    Glide.with(context)
                            .load(R.drawable.bg_glass)
                            .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                            .into(img_blur)
                }



                Glide.with(context)
                        .load(convert.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_thumb)

                tv_item_name.text = convert.provideName()
                tv_price.text = convert.providePrice()
                tv_item_code.text = convert.provideCode()
                tv_department.text = convert.provideDepartment()
            }
        }

    }

    object BlurBuilder {
        private val BITMAP_SCALE = 0.4f
        private val BLUR_RADIUS = 7.5f

        fun blur(context: Context, image: Bitmap): Bitmap {
            val width = Math.round(image.width * BITMAP_SCALE)
            val height = Math.round(image.height * BITMAP_SCALE)

            val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val rs = RenderScript.create(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(BLUR_RADIUS)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        }
    }

    companion object {
        var STATUS_DISPLAY_HIDDEN: Int = 1 //Không hiển thị
    }

    interface ProductManagerProvider {
        fun provideName(): String
        fun provideImage(): String
        fun provideCode(): String
        fun provideTTPrice(): String
        fun providePrice(): String
        fun provideDepartment(): String
        fun provideStatus(): Int
    }

    class ProductManagerConverter : Converter<ProductManager, ProductManagerProvider> {
        override fun convert(from: ProductManager): ProductManagerProvider {
            return object : ProductManagerProvider {
                override fun provideStatus(): Int {
                    return from.status ?: 0
                }

                override fun provideDepartment(): String {
                    return from.department ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideImage(): String {
                    return from.image ?: ""
                }

                override fun provideCode(): String {
                    return "MSP: ${from.code}"
                }

                override fun provideTTPrice(): String {
                    return from.ttPrice?.asMoney() ?: "0 đ"
                }

                override fun providePrice(): String {
                    return from.price?.asMoney() ?: "0 đ"
                }
            }
        }
    }

}