package com.example.corehat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_konsultasi.*

class ChatKonsultasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_konsultasi)

        val namapengguna = intent.getStringExtra("Nama")

        namaPenggunaChat.text = namapengguna

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())


        rvChat.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvChat.adapter = adapter
    }

    fun getBack(view: View) {
        finish()
    }
}

class ChatFromItem: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}

class ChatToItem: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}
