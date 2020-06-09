package com.example.corehat.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corehat.R
import com.example.corehat.rvhari.Adapter
import com.example.corehat.rvhari.Hari
import com.example.corehat.rvjanji.AdapterJanji
import com.example.corehat.rvjanji.Janji
import com.example.corehat.viewmodel.SharedViewModel
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
    private lateinit var ref2: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()

        root.rvHari.setHasFixedSize(true)
        root.rvHari.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)

        root.rvJanji.setHasFixedSize(true)
        root.rvJanji.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)

        viewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val cal = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = formatter.format(cal.time)
        root.tanggalTeks.text = today

        getDataJadwal(auth.currentUser?.uid!!)

        viewModel.data.observeForever(androidx.lifecycle.Observer {
            when(it) {
                "dayclicked" -> getDataJanji(auth.currentUser?.uid!!)
            }
        })

        return root
    }

    private fun getDataJadwal(id: String) {
        var daftarHari = arrayListOf<Hari>()
        ref = FirebaseDatabase.getInstance().getReference("jadwal")
        ref.orderByChild("id_konselor").equalTo(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val hari = h.child("hari").value.toString()
                        daftarHari.add(Hari(hari, getDate(hari.toInt()), 0))
                    }
                    daftarHari = ArrayList(daftarHari.distinct())
                    daftarHari.sortBy { it.tanggal }
                    val adapter = Adapter(daftarHari, viewModel)
                    adapter.notifyDataSetChanged()
                    root.rvHari.adapter = adapter
                    getDataJanji(id)
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

    private fun getDataJanji(id: String) {
        val listJanji = arrayListOf<Janji>()
        val listJadwal = arrayListOf<Janji>()
        ref = FirebaseDatabase.getInstance().getReference("janji")
        ref.orderByChild("id_konselor").equalTo(id).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                listJanji.clear()
                if (p0.exists()) {
                    for (h in p0.children) {
                        val idJanji = h.key.toString()
                        val idKonselor = h.child("id_konselor").value.toString()
                        val idUser = h.child("id_user").value.toString()
                        val status = h.child("status").value.toString()
                        val jam = h.child("jam").value.toString()
                        val tanggal = h.child("tanggal").value.toString()
                        val catatan = h.child("catatan").value.toString()
                        listJanji.add(Janji(idJanji, idKonselor, idUser, status.toInt(), jam, tanggal, catatan))
                    }
                }
                ref2 = FirebaseDatabase.getInstance().getReference("jadwal")
                ref2.orderByChild("id_konselor").equalTo(id).addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(p1: DatabaseError) {  }

                    override fun onDataChange(p1: DataSnapshot) {
                        listJadwal.clear()
                        val jum = root.rvHari.adapter?.itemCount
                        var teks: Int; var hari = ""; var tanggal = ""
                        for (i in 0 until jum!!) {
                            if (root.rvHari.findViewHolderForAdapterPosition(i) != null) {
                                teks = root.rvHari
                                    .findViewHolderForAdapterPosition(i)
                                    ?.itemView
                                    ?.findViewById<TextView>(R.id.teksHari)
                                    ?.currentTextColor!!
                                if (teks == -1) {
                                    hari = root.rvHari
                                        .findViewHolderForAdapterPosition(i)
                                        ?.itemView
                                        ?.findViewById<TextView>(R.id.teksHari)
                                        ?.text.toString()
                                    tanggal = root.rvHari
                                        .findViewHolderForAdapterPosition(i)
                                        ?.itemView
                                        ?.findViewById<TextView>(R.id.teksTanggal)
                                        ?.text.toString()
                                    break
                                }
                            }
                        }
                        if (p1.exists()) {
                            for (h in p1.children) {
                                val clock = h.child("jam").value.toString()
                                val day = h.child("hari").value.toString()
                                if (hari == toDay(day.toInt())) {
                                    listJadwal.add(getJanji(tanggal, clock, listJanji))
                                }
                            }
                            val adapter = AdapterJanji(listJadwal)
                            adapter.notifyDataSetChanged()
                            root.rvJanji.adapter = adapter
                        }
                    }
                })
            }
        })
    }

    private fun toDay(value: Int): String {
        var hari : String
        when (value) {
            1 -> hari = "Minggu"
            2 -> hari = "Senin"
            3 -> hari = "Selasa"
            4 -> hari = "Rabu"
            5 -> hari = "Kamis"
            6 -> hari = "Jumat"
            else -> hari = "Sabtu"
        }
        return hari
    }

    private fun getJanji(tanggal: String, clock: String, listJanji: ArrayList<Janji>): Janji {
        var janji = Janji("0", "0", "0", 2, "$clock.00", "", "")
        for (counter in 0 until listJanji.size) {
            if (tanggal == listJanji[counter].tanggal && "$clock.00" == listJanji[counter].jam) {
                janji = Janji(listJanji[counter].id, listJanji[counter].id_konselor, listJanji[counter].id_user, listJanji[counter].status, listJanji[counter].jam, listJanji[counter].tanggal, listJanji[counter].catatan)
                break
            }
            janji = Janji("0", "0", "0", 2, "$clock.00", "", "")
        }
        return janji
    }
}
