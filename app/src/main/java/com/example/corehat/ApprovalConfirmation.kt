package com.example.corehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_approval_confirmation.*

class ApprovalConfirmation : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval_confirmation)

        val id = intent.getStringExtra("id")

        btnKonfirmasi.setOnClickListener {
            kirimKonfirmasi(id)
        }

        imgBackApprovalConfirmation.setOnClickListener { finish() }
    }

    private fun kirimKonfirmasi(id: String) {
        ref = FirebaseDatabase.getInstance().getReference("janji").child(id)
        val idrg = rgApproval.checkedRadioButtonId
        var pesan = ""
        if (idrg != -1) {
            val radio: RadioButton = findViewById(idrg)
            pesan = radio.text.toString()
            ref.child("pesan").setValue(pesan)
            ref.child("status").setValue(1)
            var intent = Intent(this, Home::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
