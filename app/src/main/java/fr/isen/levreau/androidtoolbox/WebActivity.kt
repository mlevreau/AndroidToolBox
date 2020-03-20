package fr.isen.levreau.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        callApi()
    }

    private fun callApi(): RandomUser {
        val queue = Volley.newRequestQueue(this)
        val url = "https://randomuser.me/api/?results=10&nat=fr"
        var userRandom = RandomUser()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val gson = Gson()
                userRandom = gson.fromJson(response.toString(), RandomUser::class.java)
                Log.d("USER", userRandom.results.toString())
                recycler_user.layoutManager = LinearLayoutManager(this)
                recycler_user.adapter = WebAdapter(userRandom,this)
                recycler_user.visibility = View.VISIBLE
            },
            Response.ErrorListener {
               Log.d("TAG", "ERROR")
            }
        )
        queue.add(jsonObjectRequest)
        return userRandom
    }
}

class RandomUser {
    val results : ArrayList<Result> = ArrayList()
}





