package ishopgo.com.exhibition.ui.base.widget

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by xuanhong on 7/14/17. HappyCoding!
 */

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder<T>>() {

    protected lateinit var mData: MutableList<T>

    /**
     * Running count of how many items are currently checked
     */
    var checkedItemCount: Int = 0
        protected set

    /**
     * Running state of which positions are currently checked
     */
    var checkedItems: SparseBooleanArray
        protected set

    init {
        mData = mutableListOf()
        checkedItems = SparseBooleanArray()
    }

    fun updateData(position: Int, newData: T) {
        if (position < 0 || position >= mData.size)
            throw IndexOutOfBoundsException("request index [$position], data length: ${mData.size}")

        this.mData[position] = newData
        notifyItemChanged(position)
    }

    fun updateData(newData: T, a: Comparator<T>) {
        val filter = this.mData.filter { a.compare(newData, it) == 0 }
        filter.mapIndexed { index, t ->
            {
                mData[index] = newData
                notifyItemChanged(index)
            }
        }
    }

    open fun replaceAll(data: List<T>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: T) {
        this.mData.add(data)
        notifyItemInserted(this.mData.size - 1)
    }

    fun addData(index: Int, data: T) {
        this.mData.add(index, data)
        notifyItemInserted(index)
    }

    fun addAll(data: List<T>) {
        val size = mData.size
        this.mData.addAll(data)
        notifyItemRangeInserted(size, data.size)
    }

    fun addAll(index: Int, data: List<T>) {
        this.mData.addAll(index, data)
        notifyItemRangeInserted(index, index + data.size)
    }

    fun remove(data: T): T? {
        var removedData: T? = null
        val index = this.mData.indexOf(data)
        if (index != -1) {
            removedData = this.mData.removeAt(index)
            notifyItemRemoved(index)
        }

        return removedData
    }

    fun removeAll(from: Int) {
        if (from < mData.size - 1) {
            val removeList = mData.subList(from, mData!!.size)
            val size = removeList.size
            removeList.clear()
            notifyItemRangeRemoved(from, size)
        }

    }

    fun clear() {
        this.mData.clear()
        notifyDataSetChanged()
    }

    fun resetSelection() {
        checkedItems.clear()
        checkedItemCount = 0
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return mData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val layoutResId = getChildLayoutResource(viewType)

        return createHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false), viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.populate(this.mData[position])
    }

    override fun getItemCount(): Int {
        return this.mData.size
    }

    abstract fun getChildLayoutResource(viewType: Int): Int

    abstract fun createHolder(v: View, viewType: Int): ViewHolder<T>

    open class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun populate(data: T) {

        }
    }
}
