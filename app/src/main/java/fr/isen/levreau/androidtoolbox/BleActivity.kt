package fr.isen.levreau.androidtoolbox

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_ble.*
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_permission_cell.*

class BleActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val SCAN_PERIOD: Long = 10000
    }

    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private lateinit var adapter: BleAdapter


    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnable: Boolean
        get() = bluetoothAdapter?.isEnabled == true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble)

        image_pause.visibility = View.INVISIBLE

        image_start.setOnClickListener {
            when {
                isBLEEnable -> {
                    initBLEScan()
                }
                bluetoothAdapter != null -> {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                    // bleFailedText.text = "nul"
                }
                else -> {
                    bleFailedText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                Log.w("BleActivity", "Scanning for devices")
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallBack)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallBack)
                adapter.clearResults()
                adapter.notifyDataSetChanged()
            } else {
                mScanning = false
                stopScan(leScanCallBack)
            }
        }
    }

    private val leScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLE", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
                dividerBle.visibility = View.GONE
            }
        }
    }

    private fun initBLEScan() {
        progressBar.visibility = View.VISIBLE

        dividerBle.visibility = View.GONE
        image_pause.visibility = View.VISIBLE
        image_start.visibility = View.INVISIBLE
        ble_title.text = "Scan en cours ... "

        adapter = BleAdapter(
            arrayListOf(),
            ::onDeviceClicked
        )
        recycler_device.adapter = adapter
        recycler_device.layoutManager = LinearLayoutManager(this)
        recycler_device.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        handler = Handler()

        scanLeDevice(true)
        image_start.setOnClickListener {
            scanLeDevice(!mScanning)
        }

    }

    override fun onPause() {
        super.onPause()
        if (isBLEEnable){
            scanLeDevice(false)
            image_pause.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            dividerBle.visibility = View.VISIBLE
            ble_title.text = "Lancer le scan"


        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, Device::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (isBLEEnable) {
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

data class Device(
    val name: String?,
    val address: String,
    val rssi: Int
)