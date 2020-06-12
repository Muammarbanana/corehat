package com.example.corehat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_approval.*
import kotlinx.android.synthetic.main.pop_alert.view.*
import java.util.*

class Approval : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval)

        val idjanji = intent.getStringExtra("id")
        val iduser = intent.getStringExtra("id_user")
        val catatan = intent.getStringExtra("catatan")
        val status = intent.getStringExtra("status")
        val namauser = intent.getStringExtra("namauser")
        userCatatan.text = catatan

        if (status.toInt() == 1) {
            toolbarApproval.setBackgroundColor(Color.parseColor("#C2FFC8"))
            titleApproval.setTextColor(Color.parseColor("#1A8748"))
            imgBackApproval.setImageResource(R.drawable.ic_small_arrow)
            titleApproval.text = namauser
            getDataJanji(idjanji)
            imageView.visibility = View.VISIBLE
            imageView2.visibility = View.VISIBLE
            userTanggal.visibility = View.VISIBLE
            userJam.visibility = View.VISIBLE
            teksInginKonsul.visibility = View.GONE
            btnTolak.visibility = View.GONE
            btnTerima.visibility = View.GONE
            btnSelesai.visibility = View.VISIBLE
            btnSelesai.setOnClickListener {
                popAlert(idjanji)
            }
        }

        getDataUser(iduser)

        btnTerima.setOnClickListener{
            val intent = Intent(this, ApprovalConfirmation::class.java)
            intent.putExtra("id", idjanji)
            intent.putExtra("iduser", iduser)
            startActivity(intent)
        }

        btnTolak.setOnClickListener {
            val intent = Intent(this, RefusalReason::class.java)
            intent.putExtra("id", idjanji)
            startActivity(intent)
        }

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
                        if (gender != "null") {
                            userKelamin.text = gender
                        }
                        if (photo != "null") {
                            Picasso.get().load(photo).into(userPhoto)
                        }
                        if (birth != "null") {
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

    private fun getDataJanji(id: String) {
        ref = FirebaseDatabase.getInstance().getReference("janji")
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                for (h in p0.children) {
                    val tanggal = h.child("tanggal").value.toString()
                    val jam = h.child("jam").value.toString()
                    userTanggal.text = tanggal
                    userJam.text = "$jam WIB"
                }
            }
        })
    }

    private fun deleteJanji(id: String) {
        ref = FirebaseDatabase.getInstance().getReference("janji")
        ref.child(id).removeValue()
    }

    private fun popAlert(id: String) {
        val dialog = AlertDialog.Builder(this).create()
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.pop_alert, null)
        dialog.setView(dialogView)
        dialog.setCancelable(true)
        dialogView.alertText.text = "Apakah anda yakin?"
        dialogView.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogView.btnAccept.setOnClickListener {
            deleteJanji(id)
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }
}
