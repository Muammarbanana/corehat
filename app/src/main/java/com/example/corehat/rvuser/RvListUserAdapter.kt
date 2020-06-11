package com.example.corehat.rvuser

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.corehat.ChatKonsultasi
import com.example.corehat.R
import kotlinx.android.synthetic.main.list_user_chat.view.*

class Adapter(private val list: ArrayList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<Adapter.Holder>() {

    class Holder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.Holder {
        return Holder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_user_chat, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.chatNamaPengguna.text = list[position]
        holder.view.constListUserChat.setOnClickListener{
            val intent = Intent(holder.view.context, ChatKonsultasi::class.java)
            intent.putExtra("Nama", list[position])
            holder.view.context.startActivity(intent)
        }
    }
}