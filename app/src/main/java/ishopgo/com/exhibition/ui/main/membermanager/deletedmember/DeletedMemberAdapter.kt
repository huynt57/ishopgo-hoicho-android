package ishopgo.com.exhibition.ui.main.membermanager.deletedmember

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerProvider
import kotlinx.android.synthetic.main.item_member_manager.view.*

class DeletedMemberAdapter : ClickableAdapter<MemberManagerProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_member_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<MemberManagerProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<MemberManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.img_member_manager_delete.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<MemberManagerProvider>(v) {

        override fun populate(data: MemberManagerProvider) {
            super.populate(data)

            itemView.apply {
                tv_member_manager_name.text = data.provideName()
                tv_member_manager_phone.text = data.providePhone()
                tv_member_manager_email.text = data.provideEmail()
                tv_member_manager_region.text = data.provideRegion()
                tv_member_manager_booth.text = data.provideBooth()

                Glide.with(context)
                        .load(R.drawable.ic_restore)
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(img_member_manager_delete)
            }
        }

    }
}