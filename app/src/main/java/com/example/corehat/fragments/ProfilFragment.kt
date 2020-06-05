package com.example.corehat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.corehat.Login

import com.example.corehat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profil.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfilFragment : Fragment() {

    private lateinit var root: View
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profil, container, false)

        auth = FirebaseAuth.getInstance()

        root.logOut.setOnClickListener {
            auth.signOut()
            var intent = Intent(root.context, Login::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            root.context.startActivity(intent)
        }

        return root
    }

}
