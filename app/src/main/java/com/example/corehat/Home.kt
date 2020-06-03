package com.example.corehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        keluar.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, Login::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
