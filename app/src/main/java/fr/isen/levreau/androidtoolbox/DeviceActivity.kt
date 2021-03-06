package fr.isen.levreau.androidtoolbox

import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_device.*

class DeviceActivity : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG:String = "MyActivity"
    private lateinit var adapter: BleServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        device_name.text = device.name?: "Nom inconnu"
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
                        device_statut.text = STATE_CONNECTED
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
                 , this@DeviceActivity, gatt)
                 bleServiceList.layoutManager = LinearLayoutManager(this@DeviceActivity)
             }
        }
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val value = characteristic.getStringValue(0)
            Log.e(
                "TAG",
                "onCharacteristicRead: " + value + " UUID " + characteristic.uuid.toString()
            )
            runOnUiThread {
                bleServiceList.adapter?.notifyDataSetChanged()
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val value = characteristic.value
            Log.e(
                "TAG",
                "onCharacteristicWrite: " + value + " UUID " + characteristic.uuid.toString()
            )
            runOnUiThread {
                bleServiceList.adapter?.notifyDataSetChanged()
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val value = byteArrayToHexString(characteristic.value)
            Log.e(
                "TAG",
                "onCharacteristicChanged: " + value + " UUID " + characteristic.uuid.toString()
            )
            runOnUiThread {
                bleServiceList.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte) // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'
        return result.toString()
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
