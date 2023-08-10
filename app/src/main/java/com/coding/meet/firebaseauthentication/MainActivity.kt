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


            val deleteAccountBtn = findViewById<Button>(R.id.deleteAccountBtn)
            deleteAccountBtn.setOnClickListener {
                currentUser.delete()
                    .addOnSuccessListener {
                        longToastShow("Delete Account Successful")
                        val mainIntent = Intent(this,LoginActivity::class.java)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(mainIntent)
                        finish()
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
            }
        }

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            // Logout User
            FirebaseAuth.getInstance().signOut()
            longToastShow("Logout Successful")
            val mainIntent = Intent(this,LoginActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainIntent)
            finish()
        }
    }
}