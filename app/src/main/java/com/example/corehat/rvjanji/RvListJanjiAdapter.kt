package com.example.corehat.rvjanji

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.corehat.Approval
import com.example.corehat.R
import kotlinx.android.synthetic.main.list_janji.view.*

class AdapterJanji(private val list:ArrayList<Janji>): androidx.recyclerview.widget.RecyclerView.Adapter<AdapterJanji.Holder>() {

    class Holder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterJanji.Holder {
        return Holder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_janji, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AdapterJanji.Holder, position: Int) {
        when(list[position].status) {
            0 -> {
                holder.view.teksJanji.text = "Ada Permintaan Konsultasi"
                holder.view.cardJanji.setCardBackgroundColor(Color.parseColor("#FFE5C3"))
                holder.view.imgArrow.setImageResource(R.drawable.ic_small_right_orange)
                holder.view.teksJanji.setTextColor(Color.parseColor("#FF9000"))
            }
            1 -> {
                holder.view.teksJanji.text = "Nama User"
                holder.view.cardJanji.setCardBackgroundColor(Color.parseColor("#C2FFC8"))
                holder.view.imgArrow.setImageResource(R.drawable.ic_small_right)
                holder.view.teksJanji.setTextColor(Color.parseColor("#00890E"))
            }
            else -> {
                holder.view.teksJanji.text = "Tidak ada permintaan konsultasi"
                holder.view.imgArrow.visibility = View.GONE
            }
        }
        holder.view.teksJam.text = list[position].jam
        holder.view.cardJanji.setOnClickListener {
            if (list[position].status == 0 || list[position].status == 1) {
                var intent = Intent(holder.view.context, Approval::class.java)
                holder.view.context.startActivity(intent)
            }
        }
    }

}