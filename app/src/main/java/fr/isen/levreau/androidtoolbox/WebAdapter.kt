package fr.isen.levreau.androidtoolbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_web_cell.view.*


class WebAdapter (private val userList : RandomUser, val context : Context) : RecyclerView.Adapter<WebAdapter.WebHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            WebHolder = WebHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_web_cell, parent, false)
        ,userList
    )


    override fun getItemCount(): Int = userList.results.size

    override fun onBindViewHolder(holder: WebHolder, position: Int) {
        holder.loadInfo(position)


    }

    class WebHolder(userView: View, private val userList: RandomUser) : RecyclerView.ViewHolder(userView) {
        private val textName: TextView = userView.user_name
        private val textAddress: TextView = userView.user_address
        private val textMail: TextView = userView.user_mail
        private val image: ImageView = userView.user_image



        fun loadInfo(position: Int) {
            val nameUser =
                userList.results[position].name.first + " " + userList.results[position].name.last
            val addressUser =
                userList.results[position].location.city

            Picasso.get()
                .load(userList.results[position].picture.medium)
                .fit().centerInside()
                .into(image)

            textName.text = nameUser
            textMail.text = userList.results[position].email
            textAddress.text = addressUser

        }
    }
}