package fr.isen.levreau.androidtoolbox

import android.bluetooth.BluetoothGattCharacteristic
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class BleService ( title: String, items: List<BluetoothGattCharacteristic>):
        ExpandableGroup<BluetoothGattCharacteristic>(title, items)