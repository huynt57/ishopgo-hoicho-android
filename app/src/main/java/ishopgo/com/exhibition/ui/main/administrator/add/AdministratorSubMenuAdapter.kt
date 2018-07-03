package ishopgo.com.exhibition.ui.main.administrator.add

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.administrator.AdministratorRole
import ishopgo.com.exhibition.model.administrator.AdministratorSubMenu
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_administrator_menu_parent.view.*

class AdministratorSubMenuAdapter : ClickableAdapter<AdministratorSubMenu>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_administrator_menu_parent
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<AdministratorSubMenu> {
        return Holder(v, MemberConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<AdministratorSubMenu>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            holder.apply {
                itemView.view_parent_text.setOnClickListener {
                    if (itemView.rv_administrator_child.visibility == View.GONE) {
                        itemView.rv_administrator_child.visibility = View.VISIBLE
                        itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_more_neutral_24dp)
                    } else {
                        itemView.rv_administrator_child.visibility = View.GONE
                        itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_less_neutral_24dp)
                    }
                }

                itemView.view_parent_expand.setOnClickListener {
                    if (itemView.rv_administrator_child.visibility == View.GONE) {
                        itemView.rv_administrator_child.visibility = View.VISIBLE
                        itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_more_neutral_24dp)
                    } else {
                        itemView.rv_administrator_child.visibility = View.GONE
                        itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_less_neutral_24dp)
                    }
                }
            }
        }
    }

    inner class Holder(v: View, private val converter: Converter<AdministratorSubMenu, AdministratorSubMenuProvider>) : BaseRecyclerViewAdapter.ViewHolder<AdministratorSubMenu>(v) {

        override fun populate(data: AdministratorSubMenu) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                view_parent_text.text = convert.provideName()

                if (convert.provideRole().isEmpty()) {
                    val adapterRole = AdministratorRoleAdapter()
                    adapterRole.replaceAll(convert.provideRole())
                    rv_administrator_child.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rv_administrator_child.isNestedScrollingEnabled = false
                    rv_administrator_child.setHasFixedSize(false)
                    rv_administrator_child.adapter = adapterRole
                }

                if (itemView.rv_administrator_child.visibility == View.GONE) {
                    itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_more_neutral_24dp)
                } else {
                    itemView.view_parent_expand.setImageResource(R.drawable.ic_expand_less_neutral_24dp)
                }
            }
        }

    }

    interface AdministratorSubMenuProvider {
        fun provideName(): String
        fun provideRole(): List<AdministratorRole>
        fun provideValue(): Boolean
    }

    class MemberConverter : Converter<AdministratorSubMenu, AdministratorSubMenuProvider> {

        override fun convert(from: AdministratorSubMenu): AdministratorSubMenuProvider {
            return object : AdministratorSubMenuProvider {
                override fun provideRole(): List<AdministratorRole> {
                    return from.role ?: mutableListOf()
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideValue(): Boolean {
                    return from.value ?: true
                }

            }
        }
    }
}