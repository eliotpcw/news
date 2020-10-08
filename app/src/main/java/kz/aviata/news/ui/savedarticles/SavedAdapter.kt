package kz.aviata.news.ui.savedarticles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import kotlinx.android.synthetic.main.news_item.view.*
import kz.aviata.news.R
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.ui.baseclass.BaseAdapter
import kz.aviata.news.ui.baseclass.BaseViewHolder

class SavedAdapter(
    val itemRemove: (ArticlesItem) -> Unit
) : BaseAdapter<ArticlesItem, SavedAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : BaseViewHolder<ArticlesItem>(view) {
        override fun bind(item: ArticlesItem) {
            itemView.title.text = item.title
            itemView.img.load(item.urlToImage)
            itemView.save.apply {
                background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_saved
                )
                setOnClickListener {
                    itemRemove(item)
                    items.remove(item)
                    notifyItemRemoved(adapterPosition)
                    notifyItemRangeChanged(adapterPosition, items.size)
                }
            }
        }
    }

}