package com.example.corehat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.example.corehat.model.Notifikasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_refusal_reason.*
import kotlinx.android.synthetic.main.pop_alert.view.*

class RefusalReason : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refusal_reason)

        val id = intent.getStringExtra("id")

        btnPenolakan.setOnClickListener {
            popAlert(id)
        }

        imgBackRefusalConfirmation.setOnClickListener { finish() }
    }

    private fun kirimAlasanPenolakan(id: String) {
        ref = FirebaseDatabase.getInstance().getReference("janji").child(id)
        val idrg = rgRefusal.checkedRadioButtonId
        var pesan = ""
        if (idrg != -1) {
            val radio: RadioButton = findViewById(idrg)
            if (radio == findViewById(R.id.radioButton8)) {
                pesan = editTextChoice.text.toString()
            } else {
                pesan = radio.text.toString()
            }
            ref.child("pesan").setValue(pesan)
            ref.child("status").setValue(2)
            var intent = Intent(this, Home::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val idkonselor = FirebaseAuth.getInstance().uid
        val datakonselRef = FirebaseDatabase.getInstance().getReference("konselor")
        datakonselRef.orderByKey().equalTo(idkonselor).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val namakonselor = it.child("nama").value.toString()
                    val photo = it.child("url_foto").value.toString()
                    val iduser = intent.getStringExtra("iduser")
                    val message = "telah menolak permohonan janji pertemuan kamu."
                    val time = System.currentTimeMillis()
                    val notifikasi = Notifikasi(namakonselor, photo, iduser, message, time, 0)
                    val notifRef = FirebaseDatabase.getInstance().getReference("notifikasi")
                    notifRef.push().setValue(notifikasi)
                }
            }
        })
    }

    private fun popAlert(id: String) {
        val dialog = AlertDialog.Builder(this).create()
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.pop_alert, null)
        dialog.setView(dialogView)
        dialog.setCancelable(true)
        dialogView.alertText.text = "Apakah anda yakin ingin menolak permintaan konsultasinya?"
        dialogView.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogView.btnAccept.setTextColor(Color.parseColor("#DB4437"))
        dialogView.btnAccept.setOnClickListener {
            kirimAlasanPenolakan(id)
            dialog.dismiss()
        }
        dialog.show()
    }
}
