package fr.isen.levreau.androidtoolbox

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_save.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        var age = 0
        val cal = Calendar.getInstance()
        val dateSetListener=
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthYear: Int, dayMonth: Int ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthYear)
                cal.set(Calendar.DAY_OF_MONTH, dayMonth)

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                save_date.text = sdf.format(cal.time)

                val today = Calendar.getInstance()

                age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR)

                if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR))
                    age--
            }


        save_date.setOnClickListener{
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this@SaveActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        save_button.setOnClickListener {


            val name: String = save_name.text.toString()
            val firstName: String = save_fisrtname.text.toString()
            val date: String = save_date.text.toString()

            val save = JSONObject()
            save.put("Nom", name)
            save.put("Prenom", firstName)
            save.put("Date", date)


            val data = save.toString()

            Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
            File(cacheDir.absolutePath + "sauvegarde.json").writeText(data)


        }

        save_lecture.setOnClickListener {

            val data = File(cacheDir.absolutePath + "sauvegarde.json").readText(Charsets.UTF_8)
            val json = JSONObject(data)


            AlertDialog.Builder(this)
                .setTitle("profil utilisateur : ")
                .setMessage("Nom :"+json.get("Nom").toString()+"\n" + "Prénom :"+json.get("Prenom").toString() + "\n" +"Date de Naissance : "+json.get("Date").toString()+"\n"
                        + "Age : $age" )
                .show()

        }
    }
}
