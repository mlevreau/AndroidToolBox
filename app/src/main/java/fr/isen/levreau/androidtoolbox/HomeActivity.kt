package fr.isen.levreau.androidtoolbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        home_cycle.setOnClickListener{
                val intent = Intent(this, CycleVieActivity::class.java)
                startActivity(intent)
        }

        home_deco.setOnClickListener{
            val sharedPreferences = getSharedPreferences( "login-info", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.clear()
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        home_save.setOnClickListener{
            val intent = Intent(this, SaveActivity::class.java)
            startActivity(intent)
        }

        home_permission.setOnClickListener{
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
        }

        home_web.setOnClickListener{
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }
    }
}
