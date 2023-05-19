package org.apps.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apps.storyapp.databinding.ItemStoryBinding
import org.apps.storyapp.network.response.ListStoryItem
import org.apps.storyapp.ui.detail.DetailStoryActivity
import org.apps.storyapp.withDateFormat

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem){
            binding.apply{
                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .fitCenter()
                    .into(ivItemPhoto)
                tvItemName.text = storyItem.name
                dateTextView.text = storyItem.createdAt.withDateFormat()
                descriptionTextView.text = storyItem.description

                itemView.setOnClickListener{
                    val intentDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                    intentDetail.putExtra(DetailStoryActivity.KEY_STORY, storyItem)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "profile"),
                            Pair(tvItemName, "name"),
                            Pair(dateTextView, "date"),
                            Pair(descriptionTextView, "desc"),
                        )
                    itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }
}
