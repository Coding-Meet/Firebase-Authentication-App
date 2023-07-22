package com.coding.meet.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayTxt = findViewById<TextView>(R.id.displayTxt)

        // Display current User Profile
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            val email = currentUser.email
            val userName = currentUser.displayName
            displayTxt.text = "Email=${email}\nUserName=${userName}"
        }

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            // Logout User
            FirebaseAuth.getInstance().signOut()
            val mainIntent = Intent(this,LoginActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainIntent)
            finish()
        }
    }
}