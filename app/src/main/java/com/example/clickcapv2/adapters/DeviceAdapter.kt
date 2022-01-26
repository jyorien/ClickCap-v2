package com.example.clickcapv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clickcapv2.R
import com.example.clickcapv2.data.BTDevice

class DeviceAdapter : ListAdapter<BTDevice, DeviceViewHolder>(DeviceComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.device_list_item, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.deviceName)
    fun bind(device: BTDevice) {
        name.text = device.deviceName
    }
}

class DeviceComparator : DiffUtil.ItemCallback<BTDevice>() {
    override fun areItemsTheSame(oldItem: BTDevice, newItem: BTDevice): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BTDevice, newItem: BTDevice): Boolean {
        return oldItem.deviceMAC == newItem.deviceMAC
    }

}