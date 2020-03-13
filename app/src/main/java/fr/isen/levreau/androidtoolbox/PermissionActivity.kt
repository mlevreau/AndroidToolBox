package fr.isen.levreau.androidtoolbox

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_permission.*
import kotlinx.android.synthetic.main.activity_permission_cell.*


class PermissionActivity : AppCompatActivity() {

    val listeContacts = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)



        camera_button.setOnClickListener {
                pickImageFromGallery()
        }

        loadContacts()

        contact_name.adapter = ContactAdapter(listeContacts)
        contact_name.layoutManager = LinearLayoutManager(this)


    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            camera_button.setImageURI(data?.data)
        }
    }

        private fun loadContacts() {
            var builder = StringBuilder()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS)
                //callback onRequestPermissionsResult
            } else {
                builder = getContacts()
                name_contact.text = builder.toString()
            }
        }
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                                grantResults: IntArray) {
            if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts()
                } else {
                    //  toast("Permission must be granted in order to display contacts information")
                }
            }
        }
    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor != null && cursor?.count > 0) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                listeContacts.add("Nom : $name")
            }
        } else {
            //   toast("No contacts available!")
        }
        cursor?.close()
        return builder
    }

}
