package fr.isen.levreau.androidtoolbox

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_device.*

class DeviceActivity : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG:String = "MyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        bluetoothGatt = device.connectGatt(this, false, gattCallback)

    }

    private val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ){
            when(newState){
                BluetoothProfile.STATE_CONNECTED -> {
                    runOnUiThread{
                        device_name.text = STATE_CONNECTED
                    }
                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT server.")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        device_statut.text = STATE_DISCONNECTED
                    }
                    Log.i(TAG, "Disconnected from GATT server.")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
             runOnUiThread{
                 bleServiceList.adapter = BleServiceAdapter(
                     gatt?.services?.map{
                         BleService(it.uuid.toString(), it.characteristics)
                     }?.toMutableList() ?: arrayListOf()
                 )
                 bleServiceList.layoutManager = LinearLayoutManager(this@DeviceActivity)
             }
        }
    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.discoverServices()
    }

    companion object{
        private const val STATE_CONNECTED = "Connected"
        private const val STATE_DISCONNECTED = "Disconnected"
    }

}
