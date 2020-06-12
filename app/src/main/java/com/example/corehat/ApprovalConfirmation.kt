package com.example.corehat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.corehat.model.Notifikasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_approval_confirmation.*
import kotlinx.android.synthetic.main.pop_alert.view.*

class ApprovalConfirmation : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval_confirmation)

        val id = intent.getStringExtra("id")

        btnKonfirmasi.setOnClickListener {
            popAlert(id)
        }

        imgBackApprovalConfirmation.setOnClickListener { finish() }
    }

    private fun kirimKonfirmasi(id: String) {
        ref = FirebaseDatabase.getInstance().getReference("janji").child(id)
        val idrg = rgApproval.checkedRadioButtonId
        var pesan = ""
        if (idrg != -1) {
            val radio: RadioButton = findViewById(idrg)
            if (radio == findViewById(R.id.radioButton4)) {
                pesan = editTextChoice.text.toString()
            } else {
                pesan = radio.text.toString()
            }
            ref.child("pesan").setValue(pesan)
            ref.child("status").setValue(1)
            var intent = Intent(this, Home::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val idkonselor = FirebaseAuth.getInstance().uid
        val datakonselRef = FirebaseDatabase.getInstance().getReference("konselor")
        datakonselRef.orderByKey().equalTo(idkonselor).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val namakonselor = it.child("nama").value.toString()
                    val photo = it.child("url_foto").value.toString()
                    val iduser = intent.getStringExtra("iduser")
                    val message = "telah menerima permohonan janji pertemuan kamu."
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
        dialogView.alertText.text = "Apakah anda yakin ingin menerima permintaan konsultasinya?"
        dialogView.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogView.btnAccept.setOnClickListener {
            kirimKonfirmasi(id)
            dialog.dismiss()
        }
        dialog.show()
    }
}
