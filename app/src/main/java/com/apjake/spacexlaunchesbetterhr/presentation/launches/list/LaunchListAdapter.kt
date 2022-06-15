package com.apjake.spacexlaunchesbetterhr.presentation.launches.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apjake.spacexlaunchesbetterhr.common.util.show
import com.apjake.spacexlaunchesbetterhr.databinding.SingleLaunchItemBinding
import com.apjake.spacexlaunchesbetterhr.domain.model.Launch

class LaunchListAdapter(
    private val onEndReach: ()->Unit,
    private val onItemClick: (Launch)->Unit,
): ListAdapter<Launch, LaunchViewHolder>(LaunchDiffUtilCallback) {

    private var _enableOnEndReach = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val binding = SingleLaunchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaunchViewHolder(binding)
    }
    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.setListener(onItemClick, item)
        if(position == itemCount-3 && _enableOnEndReach){
            onEndReach.invoke()
        }
    }

    fun enableOnEndReach(enable: Boolean){
        _enableOnEndReach = enable
    }
}

class LaunchViewHolder(
    private val binding: SingleLaunchItemBinding
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: Launch){
        if(item.images.isNotEmpty())
            binding.ivLaunch.show(item.images.first())
        binding.tvLaunchName.text = item.missionName
        binding.tvLaunchDate.text = item.launchDate
    }
    fun setListener(onClick: (Launch)->Unit, item: Launch){
        binding.root.setOnClickListener {
            onClick.invoke(item)
        }
    }
}

object LaunchDiffUtilCallback: DiffUtil.ItemCallback<Launch>(){
    override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem == newItem
    }
}