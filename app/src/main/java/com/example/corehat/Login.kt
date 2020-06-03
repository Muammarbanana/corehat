package com.example.corehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.corehat.model.Konselor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val namaPengguna = editTextNamaPengguna.text.toString()
            val password = editTextKataSandi.text.toString()
            if (namaPengguna.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tolong isi username dan password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            passUsername(object : MyCallBack {
                override fun onCallBack(email: String) {
                    loginUser(email, password)
                }
            }, namaPengguna)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isEmpty()) {
            Toast.makeText(baseContext, "Login Gagal", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Login Sukses", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Home::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Login Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // Read username from database and get the user email for login
    private fun passUsername(firebasecallback: MyCallBack, username: String) {
        val daftar = mutableListOf<Konselor>()
        ref = FirebaseDatabase.getInstance().getReference("konselor")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var email = ""
                if(p0.exists()){
                    for(h in p0.children) {
                        val username = h.child("username").value.toString()
                        val name = h.child("nama_konselor").value.toString()
                        val email = h.child("email").value.toString()
                        daftar.add(Konselor(name, username, email))
                    }
                }
                for(i in daftar) {
                    if (username == i.username) {
                        email = i.email
                    }
                }
                firebasecallback.onCallBack(email)
            }
        })
    }

    // Get email outside of onDataChange method
    private interface MyCallBack {
        fun onCallBack(email: String)
    }
}
