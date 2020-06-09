package com.example.corehat.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.corehat.Login

import com.example.corehat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profil.view.*
import kotlinx.android.synthetic.main.pop_alert.view.*

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
            popAlert()
        }

        return root
    }

    private fun popAlert() {
        val dialog = AlertDialog.Builder(root.context).create()
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.pop_alert, null)
        dialog.setView(dialogView)
        dialog.setCancelable(true)
        dialogView.alertText.text = "Apakah kamu yakin ingin keluar akun?"
        dialogView.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogView.btnAccept.setTextColor(Color.parseColor("#DB4437"))
        dialogView.btnAccept.text = "Ya, Keluar"
        dialogView.btnAccept.setOnClickListener {
            auth.signOut()
            var intent = Intent(root.context, Login::class.java)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            root.context.startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

}
