package com.example.corehat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corehat.ChatKonsultasi
import com.example.corehat.R
import com.example.corehat.model.ChatMessage
import com.example.corehat.model.User
import com.example.corehat.rvuser.Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_konsultasi.view.*
import kotlinx.android.synthetic.main.fragment_konsultasi.*
import kotlinx.android.synthetic.main.fragment_konsultasi.view.*
import kotlinx.android.synthetic.main.fragment_konsultasi.view.rvUserList
import kotlinx.android.synthetic.main.list_user_chat.view.*

/**
 * A simple [Fragment] subclass.
 */
class KonsultasiFragment : Fragment() {

    private lateinit var root: View
    private lateinit var ref: DatabaseReference
    val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessageMap = HashMap<String, ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_konsultasi, container, false)

        root.rvUserList.setHasFixedSize(true)
        root.rvUserList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
        root.rvUserList.adapter = adapter

        listenLatestMessage()

        return root
    }

    private fun listenLatestMessage() {
        val id = FirebaseAuth.getInstance().uid
        ref = FirebaseDatabase.getInstance().getReference("latest_messages/$id")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessage()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessage()
            }
        })
    }

    private fun refreshRecyclerViewMessage() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(ListUserChat(it))
        }
    }
}

class ListUserChat(private val chatMessage: ChatMessage): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.list_user_chat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chatMessage.text = chatMessage.message

        val toId = chatMessage.fromId
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.orderByKey().equalTo(toId).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val name = it.child("nama").value.toString()
                    val photo = it.child("photo").value.toString()
                    viewHolder.itemView.chatNamaPengguna.text = name
                    if (photo != "null") {
                        Picasso.get().load(photo).into(viewHolder.itemView.userPhoto)
                    }
                }
            }
        })

        viewHolder.itemView.constListUserChat.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, ChatKonsultasi::class.java)
            intent.putExtra("Nama", viewHolder.itemView.chatNamaPengguna.text)
            intent.putExtra("Id", chatMessage.fromId)
            viewHolder.itemView.context.startActivity(intent)
        }
    }
}
