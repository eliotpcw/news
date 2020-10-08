package kz.aviata.news.ui.baseclass

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseAdapter<T, ViewHolder: BaseViewHolder<T>>(
    items: List<T> = emptyList()
) : RecyclerView.Adapter<ViewHolder>() {

    var items: MutableList<T> = items.toMutableList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])
}

abstract class BaseViewHolder<E>(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    abstract fun bind(item: E)
}