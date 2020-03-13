package fr.isen.levreau.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cycle_vie.*


class CycleVieActivity : AppCompatActivity() {

    private var texte :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle_vie)
        texte += "onCreate()\n"
        cycle_etatview.text = texte
    }

    override fun onPause() {
        super.onPause()
        texte += "onPause()\n"
        cycle_etatview.text = texte
    }

    override fun onResume() {
        super.onResume()
        texte += "onResume()\n"
        cycle_etatview.text = texte
    }

    override fun onStop() {
        super.onStop()
        texte += "onStop()\n"
        cycle_etatview.text = texte
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext,"onDestroy()", Toast.LENGTH_SHORT).show()
    }
}