package com.example.clickcapv2.scan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.clickcapv2.R
import com.example.clickcapv2.adapters.DeviceAdapter
import com.example.clickcapv2.data.BTDevice
import com.example.clickcapv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mockDevices = listOf<BTDevice>(
        BTDevice("Joey's Airpods","101010101"),
        BTDevice("Joey's Airpods","101010101"),
        BTDevice("Joey's Airpods","101010101"),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.deviceRecyclerView.adapter = DeviceAdapter()
        (binding.deviceRecyclerView.adapter as DeviceAdapter).submitList(mockDevices)
    }
}