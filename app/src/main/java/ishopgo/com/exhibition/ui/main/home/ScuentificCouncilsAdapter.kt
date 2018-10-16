package ishopgo.com.exhibition.ui.main.home

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ScuentificCouncils
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_scuentific_councils.view.*

class ScuentificCouncilsAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<ScuentificCouncils.Advisor>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun createHolder(v: View, viewType: Int): ViewHolder<ScuentificCouncils.Advisor> {
        val productHolder = Holder(v, ScuentificCouncilsConverter())
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_scuentific_councils
    }

    override fun onBindViewHolder(holder: ViewHolder<ScuentificCouncils.Advisor>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is Holder) {
            holder.apply {
                itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
            }

        }

    }

    inner class Holder(v: View, private val converter: Converter<ScuentificCouncils.Advisor, ScuentificCouncilsProvider>) : BaseRecyclerViewAdapter.ViewHolder<ScuentificCouncils.Advisor>(v) {

        override fun populate(data: ScuentificCouncils.Advisor) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideImage())
                        .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_thumb)

                tv_name.text = convert.provideName()
                tv_phone.setPhone(convert.providePhone(), data.phone ?:"")
                tv_introduction.text = convert.provideNote()
            }
        }
    }

    interface ScuentificCouncilsProvider {
        fun provideName(): CharSequence
        fun provideImage(): CharSequence
        fun providePhone(): CharSequence
        fun provideNote(): CharSequence
    }

    class ScuentificCouncilsConverter : Converter<ScuentificCouncils.Advisor, ScuentificCouncilsProvider> {
        override fun convert(from: ScuentificCouncils.Advisor): ScuentificCouncilsProvider {
            return object : ScuentificCouncilsProvider {
                override fun provideName(): CharSequence {
                    return from.name ?:""
                }

                override fun provideImage(): CharSequence {
                    return from.image ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.phone ?: ""
                }

                override fun provideNote(): CharSequence {
                    return from.introduction ?:""
                }
            }
        }
    }

}