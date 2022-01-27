package com.example.clickcapv2.adapters

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clickcapv2.R

class DeviceAdapter : ListAdapter<BluetoothDevice, DeviceViewHolder>(DeviceComparator()) {
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
    private val name: TextView = itemView.findViewById(R.id.deviceName)
    private val bondState: TextView = itemView.findViewById(R.id.deviceBondState)
    @SuppressLint("MissingPermission")
    fun bind(device: BluetoothDevice) {
        device.name?.let {
            name.text = it
            var bondStateText = ""
            when (device.bondState) {
                BluetoothDevice.BOND_BONDED -> {bondStateText = "Bonded" }
                BluetoothDevice.BOND_BONDING -> {bondStateText = "Bonding" }
                BluetoothDevice.BOND_NONE -> {bondStateText = "Not bonded" }
            }
            bondState.text = bondStateText
        }

    }
}

class DeviceComparator : DiffUtil.ItemCallback<BluetoothDevice>() {
    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem.address == newItem.address
    }

}