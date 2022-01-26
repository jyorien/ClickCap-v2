package com.example.clickcapv2.scan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.clickcapv2.R
import com.example.clickcapv2.adapters.DeviceAdapter
import com.example.clickcapv2.databinding.ActivityMainBinding

const val REQUEST_ENABLE_BT = 0
const val REQUEST_DISCOVERABLE_BT = 0
const val BLUETOOTH_CONNECT_REQUEST = 101

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var mBluetoothAdapter: BluetoothAdapter

    // enable bluetooth result
    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Please enable bluetooth!", Toast.LENGTH_SHORT).show()
                binding.btnNext.isEnabled = false
                return@registerForActivityResult
            }
            queryPairedDevices(mBluetoothAdapter)

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.deviceRecyclerView.adapter = DeviceAdapter()

        // get bluetooth adapter
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        binding.btnScanDevice.setOnClickListener {
            enableBluetooth()
        }
    }

    private fun enableBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_CONNECT_REQUEST)
            }
        }
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also {
            enableBluetoothLauncher.launch(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun queryPairedDevices(bluetoothAdapter: BluetoothAdapter) {
        val pairedDevices = bluetoothAdapter.bondedDevices
        (binding.deviceRecyclerView.adapter as DeviceAdapter).submitList(pairedDevices.toList())
    }
}