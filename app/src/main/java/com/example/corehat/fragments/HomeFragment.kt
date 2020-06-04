package com.example.corehat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corehat.R
import com.example.corehat.rvhari.Adapter
import com.example.corehat.rvhari.Hari
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.tanggalTeks
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var root: View
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()

        root.rvHari.setHasFixedSize(true)
        root.rvHari.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)

        val cal = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = formatter.format(cal.time)
        root.tanggalTeks.text = today

        getDataJadwal(auth.currentUser?.uid!!)

        return root
    }

    private fun getDataJadwal(id: String) {
        var daftarHari = arrayListOf<Hari>()
        ref = FirebaseDatabase.getInstance().getReference("jadwal")
        ref.orderByChild("id_konselor").equalTo(id).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val hari = h.child("hari").value.toString()
                        val jam = h.child("jam").value.toString()
                        daftarHari.add(Hari(hari, getDate(hari.toInt()), 0))
                    }
                    daftarHari = ArrayList(daftarHari.distinct())
                    daftarHari.sortBy { it.tanggal }
                    val adapter = Adapter(ArrayList(daftarHari))
                    adapter.notifyDataSetChanged()
                    root.rvHari.adapter = adapter
                }
            }
        })
    }

    private fun getDate(hari: Int): String {
        var finalDate = String()
        val sCalendar = Calendar.getInstance()
        sCalendar.add(Calendar.DATE,7)
        val c = GregorianCalendar()
        while (c.time.before(Date(sCalendar.timeInMillis))) {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val today = formatter.format(c.time)
            val tanggal = c.get(Calendar.DAY_OF_WEEK)
            if (tanggal == hari) {
                finalDate = today
            }
            c.add(Calendar.DAY_OF_MONTH, 1)
        }
        return finalDate
    }
}
