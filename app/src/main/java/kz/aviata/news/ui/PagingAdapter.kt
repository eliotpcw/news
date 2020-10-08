package kz.aviata.news.ui

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.synthetic.main.news_item.view.*
import kz.aviata.news.R
import kz.aviata.news.data.models.ArticlesItem


class PagingAdapter(
    val itemClick: (ArticlesItem) -> Unit,
) : PagedListAdapter<ArticlesItem, PagingAdapter.ViewHolder>(DIFF_CALLBACK){
    private val mDiffer =  AsyncPagedListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mDiffer.getItem(position)?.let { holder.bind(it) }
    }

    override fun getItem(position: Int): ArticlesItem? {
        return mDiffer.getItem(position)
    }

    override fun getItemCount(): Int {
        return mDiffer.itemCount
    }

    override fun submitList(pagedList: PagedList<ArticlesItem>?) {
        pagedList?.addWeakCallback(pagedList.snapshot(), object : PagedList.Callback() {
            override fun onChanged(position: Int, count: Int) {
            }

            override fun onInserted(position: Int, count: Int) {
                mDiffer.submitList(pagedList)
            }

            override fun onRemoved(position: Int, count: Int) {
            }
        })
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean =
                oldItem.description == newItem.description

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(item: ArticlesItem) {
            itemView.title.text = item.title
            itemView.img.load(item.urlToImage)
            itemView.save.setOnClickListener {
                if(item.saved) {
                    item.saved = false
                    itemView.save.background = ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_unsaved
                    )
                } else {
                    item.saved = true
                    itemClick(item)
                    itemView.save.background = ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_saved
                    )
                }
            }
        }
    }
}