package com.example.corehat.rvhari

import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.corehat.R
import kotlinx.android.synthetic.main.list_hari.view.*
import java.text.SimpleDateFormat

class Adapter(private val list:ArrayList<Hari>): androidx.recyclerview.widget.RecyclerView.Adapter<Adapter.Holder>() {

    private var selectedItemPosition = -1

    class Holder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.Holder {
        return Holder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_hari, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.teksHari.text = toDay(list[position].hari.toInt())
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        var tgl = formatter.parse(list[position].tanggal)
        val bulan = DateFormat.format("MM", tgl).toString()
        val hari = DateFormat.format("dd", tgl)
        if (position != selectedItemPosition) {
            holder.view.constHari.setBackgroundColor(Color.parseColor("#F3F4FA"))
            holder.view.teksHari.setTextColor(Color.parseColor("#172B4D"))
            holder.view.teksTanggal.setTextColor(Color.parseColor("#172B4D"))
        } else {
            holder.view.constHari.setBackgroundResource(R.drawable.rounded_button_green_borderless)
            holder.view.teksHari.setTextColor(Color.parseColor("#FFFFFF"))
            holder.view.teksTanggal.setTextColor(Color.parseColor("#FFFFFF"))
        }
        holder.view.teksTanggal.text = "$hari ${toMonth(bulan.toInt())}"
        holder.view.constHari.setOnClickListener {
            changeValueSelected(holder)
        }
    }

    private fun changeValueSelected(holder: Holder) {
        selectedItemPosition = holder.adapterPosition
        notifyDataSetChanged()
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

    private fun toMonth(value: Int): String {
        var bulan: String
        when (value) {
            1 -> bulan = "Jan"
            2 -> bulan = "Feb"
            3 -> bulan = "Mar"
            4 -> bulan = "Apr"
            5 -> bulan = "Mei"
            6 -> bulan = "Jun"
            7 -> bulan = "Jul"
            8 -> bulan = "Agu"
            9 -> bulan = "Sep"
            10 -> bulan = "Okt"
            11 -> bulan = "Nov"
            else -> bulan = "Des"
        }
        return bulan
    }
}