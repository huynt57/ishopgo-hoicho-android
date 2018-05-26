package ishopgo.com.exhibition.ui.main.home.search.member

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerProvider
import kotlinx.android.synthetic.main.item_member_manager.view.*

class SearchMemberAdapter (var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<MemberManagerProvider>() {

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_member_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<MemberManagerProvider> {
        val productHolder = ProductHolder(v)
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<MemberManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    internal inner class ProductHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<MemberManagerProvider>(view) {

        override fun populate(data: MemberManagerProvider) {
            super.populate(data)

            itemView.apply {
                tv_member_manager_name.text = data.provideName()
                tv_member_manager_phone.text = data.providePhone()
                tv_member_manager_email.text = data.provideEmail()
                tv_member_manager_region.text = data.provideRegion()
                tv_member_manager_booth.text = data.provideBooth()
            }
        }
    }
}