package ishopgo.com.exhibition.ui.main.boothmanager

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_booth_manager.view.*

class BoothManagerAdapter : ClickableAdapter<BoothManagerProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_booth_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BoothManagerProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<BoothManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<BoothManagerProvider>(v) {

        override fun populate(data: BoothManagerProvider) {
            super.populate(data)

            itemView.apply {
                tv_booth_manager_name.text = data.provideName()
                tv_booth_manager_phone.text = data.providePhone()
                tv_booth_manager_store.text = data.provideCompanyStore()
                tv_booth_manager_region.text = data.provideRegion()
                tv_booth_manager_number_product.text = data.provideNumberProduct()
                tv_booth_manager_member_cnt.text = data.provideMemberCNT()
            }
        }

    }
}