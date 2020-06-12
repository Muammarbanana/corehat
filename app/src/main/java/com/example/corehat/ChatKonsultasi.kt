package com.example.corehat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.corehat.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_konsultasi.*
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_from.view.chatime
import kotlinx.android.synthetic.main.chat_from.view.teksChat
import kotlinx.android.synthetic.main.chat_to.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatKonsultasi : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_konsultasi)

        val namapengguna = intent.getStringExtra("Nama")
        namaPenggunaChat.text = namapengguna

        listenForMessages()

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        rvChat.layoutManager = layoutManager
        rvChat.adapter = adapter

        imgSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            if (message.isNotEmpty()) {
                performSendMessage(message)
            }
        }
    }

    fun getBack(view: View) {
        finish()
    }

    private fun performSendMessage(message: String) {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = intent.getStringExtra("Id")
        val timestamp = System.currentTimeMillis()
        val ref = FirebaseDatabase.getInstance().getReference("messages/$fromId/$toId")
        val toref = FirebaseDatabase.getInstance().getReference("messages/$toId/$fromId")
        val latestFromRef = FirebaseDatabase.getInstance().getReference("latest_messages/$fromId/$toId")
        val latestToRef = FirebaseDatabase.getInstance().getReference("latest_messages/$toId/$fromId")
        val chatmessage = ChatMessage(fromId!!, toId, message, timestamp)
        ref.push().setValue(chatmessage).addOnSuccessListener {
            editTextMessage.text.clear()
            rvChat.scrollToPosition(adapter.itemCount - 1)
        }
        toref.push().setValue(chatmessage)
        latestFromRef.setValue(chatmessage)
        latestToRef.setValue(chatmessage)
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = intent.getStringExtra("Id")
        val ref = FirebaseDatabase.getInstance().getReference("messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatmessage = p0.getValue(ChatMessage::class.java)

                if (chatmessage != null) {
                    if (chatmessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatToItem(chatmessage.message, converToHours(chatmessage.timestamp)))
                    } else {
                        adapter.add(ChatFromItem(chatmessage.message, converToHours(chatmessage.timestamp)))
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

    private fun converToHours(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        val time = Date(timestamp)
        return sdf.format(time)
    }
}

class ChatFromItem(val text: String, val time: String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.teksChat.text = text
        viewHolder.itemView.chatime.text = time
    }

}

class ChatToItem(val text: String, val time: String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.teksChatTo.text = text
        viewHolder.itemView.chatimeTo.text = time
    }

}
