package com.example.corehat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corehat.R
import com.example.corehat.model.User
import com.example.corehat.rvuser.Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_konsultasi.*
import kotlinx.android.synthetic.main.fragment_konsultasi.view.*
import kotlinx.android.synthetic.main.fragment_konsultasi.view.rvUserList

/**
 * A simple [Fragment] subclass.
 */
class KonsultasiFragment : Fragment() {

    private lateinit var root: View
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_konsultasi, container, false)

        root.rvUserList.setHasFixedSize(true)
        root.rvUserList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)

        getDataPesanPengguna()

        return root
    }

    private fun getDataPesanPengguna() {
        val listUser = arrayListOf<User>()
        val listIdUser = arrayListOf<String>()
        val referensi = FirebaseDatabase.getInstance().getReference("messages")
        referensi.orderByKey().equalTo(FirebaseAuth.getInstance().uid).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                listUser.clear()
                listIdUser.clear()
                if(p0.exists()) {
                    p0.children.forEach {p1 ->
                        p1.children.forEach {
                            val iduser = it.key.toString()
                            listIdUser.add(iduser)
                        }
                    }
                }
                ref = FirebaseDatabase.getInstance().getReference("Users")
                ref.orderByKey().addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            p0.children.forEach {
                                val nama = it.child("nama").value.toString()
                                val id = it.key.toString()
                                val username = it.child("username").value.toString()
                                val photo = it.child("photo").value.toString()
                                listUser.add(User(id, username, nama, photo))
                            }
                            var count = 0
                            val listUserFiltered = arrayListOf<User>()
                            for (h in listUser) {
                                for (g in listIdUser) {
                                    if (h.id == g) {
                                        listUserFiltered.add(h)
                                    }
                                    break
                                }
                                count += 1
                            }
                            val adapter = Adapter(listUserFiltered)
                            adapter.notifyDataSetChanged()
                            rvUserList.adapter = adapter
                        }
                    }
                })
            }
        })
    }

}
