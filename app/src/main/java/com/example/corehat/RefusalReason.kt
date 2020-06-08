package com.example.corehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_refusal_reason.*

class RefusalReason : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refusal_reason)

        val id = intent.getStringExtra("id")

        btnPenolakan.setOnClickListener {
            kirimAlasanPenolakan(id)
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
            ref.child("status").setValue(3)
            var intent = Intent(this, Home::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
