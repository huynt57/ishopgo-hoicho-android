package ishopgo.com.exhibition.ui.main.administrator.add

import android.graphics.Color
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.administrator.AdministratorRole
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_administrator_menu_child.view.*
import android.content.res.ColorStateList


class AdministratorRoleAdapter : ClickableAdapter<AdministratorRole>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_administrator_menu_child
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<AdministratorRole> {
        return Holder(v, MemberConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<AdministratorRole>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.view_child.setOnCheckedChangeListener { _, _ ->
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class Holder(v: View, private val converter: Converter<AdministratorRole, AdministratorRoleProvider>) : BaseRecyclerViewAdapter.ViewHolder<AdministratorRole>(v) {

        override fun populate(data: AdministratorRole) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                view_child.isChecked = convert.provideSelected()
                if (convert.provideSelected())
                    view_child.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                else view_child.setTextColor(resources.getColor(R.color.colorPrimaryText))

                view_child.text = convert.provideName()
            }
        }

    }

    interface AdministratorRoleProvider {
        fun provideName(): String
        fun provideKey(): String
        fun provideValue(): Boolean
        fun provideSelected(): Boolean
    }

    class MemberConverter : Converter<AdministratorRole, AdministratorRoleProvider> {

        override fun convert(from: AdministratorRole): AdministratorRoleProvider {
            return object : AdministratorRoleProvider {
                override fun provideSelected(): Boolean {
                    return from.isSelected
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideKey(): String {
                    return from.key ?: ""
                }

                override fun provideValue(): Boolean {
                    return from.value ?: true
                }

            }
        }

    }
}