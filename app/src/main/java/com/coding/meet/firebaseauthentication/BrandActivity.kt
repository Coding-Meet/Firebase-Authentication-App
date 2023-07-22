package com.coding.meet.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth

class BrandActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand)
        // here setKeepOnScreenCondition true so, activity redirect another activity
        // and some api call here
        // if setKeepOnScreenCondition false so, activity code not redirect another activity
        splashScreen.setKeepOnScreenCondition { true }
        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 2000)
    }
}