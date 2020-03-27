package fr.isen.levreau.androidtoolbox
import android.bluetooth.BluetoothGattCharacteristic
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.*
import kotlinx.android.synthetic.main.activity_ble_device_service_cell.view.*

class BleServiceAdapter(private val serviceList : MutableList<BleService>):
    ExpandableRecyclerViewAdapter<BleServiceAdapter.ServiceViewHolder,
            BleServiceAdapter.CharacteristicViewHolder>(serviceList){

    class ServiceViewHolder(itemView: View) : GroupViewHolder(itemView){
        val serviceName : TextView = itemView.serviceName
    }
    class CharacteristicViewHolder (itemView:View) : ChildViewHolder(itemView){
        val characteristicUUID : TextView = itemView.characteristicUUID

    }
    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int):ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ble_device_service_cell, parent, false))

    override fun onCreateChildViewHolder(
        parent:ViewGroup, viewType:Int):CharacteristicViewHolder=
        CharacteristicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ble_device_characteristic_cell, parent,false)
        )

    override fun onBindChildViewHolder(
        holder:CharacteristicViewHolder, flatPosition : Int, group : ExpandableGroup<*>,
        childIndex:Int
    ){
        val characteristic : BluetoothGattCharacteristic=(group as BleService).items[childIndex]
        val uuid = characteristic.uuid
        holder.characteristicUUID.text = uuid.toString()
    }
    override fun onBindGroupViewHolder(
        holder:ServiceViewHolder, flatPosition:Int,
        group: ExpandableGroup<*>
    ){
        val title = group.title
        holder.serviceName.text= title
    }
}