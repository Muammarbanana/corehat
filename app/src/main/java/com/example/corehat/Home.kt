package com.example.corehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.corehat.fragments.HomeFragment
import com.example.corehat.fragments.KonsultasiFragment
import com.example.corehat.fragments.ProfilFragment
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

        getPhoto()
        removeExpiredRequest()

        val menu = bottomNav.menu
        selectedItem(menu.getItem(0))
        bottomNav.setOnNavigationItemSelectedListener {
            selectedItem(it)

            false
        }
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

    private fun getName() {
        ref = FirebaseDatabase.getInstance().getReference("konselor")
        ref.orderByKey().equalTo(auth.currentUser?.uid!!).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (h in p0.children) {
                    val nama = h.child("nama").value.toString()
                    title2.text = nama
                }
            }

        })
    }

    private fun selectedItem(item: MenuItem) {
        item.isChecked = true
        when(item.itemId) {
            R.id.jadwalMenu -> {
                selectedFragment(HomeFragment())
                title1.text = "Jadwal"
                title2.text = "Konsultasi"
            }
            R.id.konsultasiMenu -> {
                selectedFragment(KonsultasiFragment())
                title1.text = "Konsultasi"
                title2.text = "Online"
            }
            R.id.profileMenu -> {
                selectedFragment(ProfilFragment())
                title1.text = "Halo,"
                getName()
            }
        }
    }

    private fun removeExpiredRequest() {
        val ref = FirebaseDatabase.getInstance().getReference("janji")
        ref.orderByChild("status").equalTo(1.toDouble()).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val idjanji = h.key.toString()
                        val timestamp = h.child("timestamp").value
                        if (timestamp != null) {
                            val elapsedtime = System.currentTimeMillis() - timestamp.toString().toLong()
                            if (elapsedtime > 86400000) {
                                val rev = FirebaseDatabase.getInstance().getReference("janji")
                                rev.child(idjanji).removeValue()
                            }
                        }
                    }
                }
            }

        })
    }

    private fun selectedFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainframe, fragment)
        transaction.commit()
    }
}
