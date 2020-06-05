package com.example.corehat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_approval.*
import java.util.*

class Approval : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval)

        val idjanji = intent.getStringExtra("id")
        val iduser = intent.getStringExtra("id_user")
        val catatan = intent.getStringExtra("catatan")
        userCatatan.text = catatan

        getDataUser(iduser)

        imgBackApproval.setOnClickListener { finish() }
    }

    private fun getDataUser(id: String) {
        ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val name = h.child("nama").value.toString()
                        val gender = h.child("gender").value.toString()
                        val photo = h.child("photo").value.toString()
                        val birth = h.child("birth").value.toString()
                        userName.text = name
                        if (gender != "null" || gender != "") {
                            userKelamin.text = gender
                        }
                        if (photo != "null" || photo != "") {
                            Picasso.get().load(photo).into(userPhoto)
                        }
                        if (birth != "null" || birth != "") {
                            val birthdate = birth.split("-").toTypedArray()
                            userUsia.text = getAge(birthdate)
                        }
                    }
                }
            }

        })
    }

    private fun getAge(birthdate: Array<String>): String {
        val today = Calendar.getInstance()
        val birth = Calendar.getInstance()
        birth.set(birthdate[2].toInt(), birthdate[1].toInt(), birthdate[0].toInt())
        val age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        return "$age tahun"
    }
}
