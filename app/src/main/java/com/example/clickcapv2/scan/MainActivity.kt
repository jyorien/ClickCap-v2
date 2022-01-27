package com.example.clickcapv2.scan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.clickcapv2.R
import com.example.clickcapv2.adapters.DeviceAdapter
import com.example.clickcapv2.databinding.ActivityMainBinding

const val BLUETOOTH_CONNECT_REQUEST = 101
const val COARSE_LOCATION_REQUEST = 102
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private val deviceList = mutableListOf<BluetoothDevice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.deviceRecyclerView.adapter = DeviceAdapter()

        // get bluetooth adapter
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter

        // scan for bluetooth devices
        binding.btnScanDevice.setOnClickListener {
            enableBluetooth()
        }
    }

    private fun enableBluetooth() {
        // request permissions if API version required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_CONNECT_REQUEST)
            }
        }
        // check for bluetooth enabled
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also {
            enableBluetoothLauncher.launch(it)
        }
    }

    // handle bluetooth enabled result
    @SuppressLint("MissingPermission")
    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Please enable bluetooth!", Toast.LENGTH_SHORT).show()
                binding.btnNext.isEnabled = false
                return@registerForActivityResult
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), COARSE_LOCATION_REQUEST)
            }
            queryPairedDevices(mBluetoothAdapter)
            val discoveryStarted = mBluetoothAdapter.startDiscovery()
            Log.d("hello","Discovery Started: $discoveryStarted")
            registerDeviceReceiver()

        }

    // get BONDED but NOT CONNECTED devices
    @SuppressLint("MissingPermission")
    private fun queryPairedDevices(bluetoothAdapter: BluetoothAdapter) {
        val pairedDevices = bluetoothAdapter.bondedDevices
        deviceList.addAll(pairedDevices)
        (binding.deviceRecyclerView.adapter as DeviceAdapter).submitList(deviceList)
    }

    // register broadcast receiver
    @SuppressLint("MissingPermission")
    private fun registerDeviceReceiver() {
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    Log.d("hello","action found")
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("hello", device?.name.toString())
                    if (!deviceList.contains(device) && device?.name != null) {
                        device.let {
                            deviceList.add(it)
                            val newList = mutableListOf<BluetoothDevice>()
                            newList.addAll(deviceList)
                            Log.d("hello","new list $deviceList")
                            (binding.deviceRecyclerView.adapter as DeviceAdapter).submitList(newList)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }
}