package com.adhibuchori.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhibuchori.storyapp.data.remote.response.Story
import com.adhibuchori.storyapp.databinding.ItemRowUserStoryBinding
import com.bumptech.glide.Glide

class ItemUserStoryAdapter(
    private val listStory: List<Story>,
    private val onClick: (Story) -> Unit
) : RecyclerView.Adapter<ItemUserStoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRowUserStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRowUserStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvItemName.text = listStory[holder.adapterPosition].name
        Glide.with(holder.itemView)
            .load(listStory[holder.adapterPosition].photoUrl)
            .into(holder.binding.ivItemStory)
        holder.binding.tvItemDescription.text = listStory[holder.adapterPosition].description

        holder.itemView.setOnClickListener {
            onClick(listStory[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listStory.size
}