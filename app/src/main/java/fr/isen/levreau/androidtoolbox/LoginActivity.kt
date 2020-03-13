package fr.isen.levreau.androidtoolbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
/*
        //message toast quand clique valider
        validateButton.setOnClickListener() {
            Toast.makeText(this, "Identifiant enregistré ! ", Toast.LENGTH_SHORT).show()
        }

        // retourne l'identifiant dans un toast
        validateButton.setOnClickListener{
            val identifiantUser :String = login_id.text.toString()
            Toast.makeText(this, "Identifiant : ${identifiantUser}", Toast.LENGTH_SHORT).show()
        }

          // vérifier l'authentification
          validateButton.setOnClickListener{
            val identifiantUser :String = login_id.text.toString()
            val mdpUser :String = login_mdp.text.toString()


            if(identifiantUser == "admin" && mdpUser == "123"){
                Toast.makeText(this, "Identifiant réussi ! ", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Identifiant refusé ! ", Toast.LENGTH_SHORT).show()

            }
*/
        var good_id = "admin"
        var good_mdp = "123"
        val sharedPreferences = getSharedPreferences( "login-info", MODE_PRIVATE)

        val save_id = sharedPreferences.getString("ID","")
        val save_mdp = sharedPreferences.getString("MDP","")

        if( save_id == good_id && save_mdp == good_mdp){

            Toast.makeText(this, "Identification réussi ! ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java) //f
            startActivity(intent)
            finish()
        }

        validateButton.setOnClickListener{

            val identifiantUser :String = login_id.text.toString()
            val mdpUser :String = login_mdp.text.toString()

            val editor = sharedPreferences.edit()

            if( identifiantUser == good_id && mdpUser == good_mdp){
                editor.putString("ID", identifiantUser)
                editor.putString("MDP", mdpUser)
                editor.apply()

                Toast.makeText(this, "Identification réussi ! ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java) //f
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Identification refusé ! ", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
