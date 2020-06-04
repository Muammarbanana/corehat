package com.example.corehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

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

        getPhoto()
    }

    private fun getPhoto() {
        ref = FirebaseDatabase.getInstance().getReference("konselor")
        ref.orderByKey().equalTo(auth.currentUser?.uid!!).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val photo = h.child("url_foto").value.toString()
                        Picasso.get().load(photo).into(conselPhoto)
                    }
                }
            }

        })
    }
}
