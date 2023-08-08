package com.coding.meet.firebaseauthentication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private val googleSignInRequestCode = 234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loadingDialog = Dialog(this,R.style.DialogCustomTheme).apply {
            setContentView(R.layout.loading_dialog)
            window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
        }
        val signUpABtn = findViewById<Button>(R.id.signUpABtn)
        signUpABtn.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }


        val edEmail = findViewById<TextInputEditText>(R.id.edEmail)
        val edEmailL = findViewById<TextInputLayout>(R.id.edEmailL)

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEmail(edEmail, edEmailL)
            }

        })


        val edPassword = findViewById<TextInputEditText>(R.id.edPassword)
        val edPasswordL = findViewById<TextInputLayout>(R.id.edPasswordL)

        edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validatePassword(edPassword, edPasswordL)
            }

        })


        val signInBtn = findViewById<Button>(R.id.signInBtn)
        signInBtn.setOnClickListener {
            if (validateEmail(edEmail, edEmailL)
                && validatePassword(edPassword, edPasswordL)
            ) {
                if (isConnected(this)){
                    loadingDialog.show()
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(
                            edEmail.text.toString().trim(),
                            edPassword.text.toString().trim())
                        .addOnSuccessListener {
                            longToastShow("Login Successful")
                            loadingDialog.dismiss()
                            val mainIntent = Intent(this,MainActivity::class.java)
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(mainIntent)
                            finish()
                        }
                        .addOnFailureListener {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                }else{
                    longToastShow("No Internet Connection!")
                }
            }

        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this,gso)

        val googleSignInBtn = findViewById<SignInButton>(R.id.googleSignInBtn)
        googleSignInBtn.setOnClickListener {
            if (isConnected(this)){
                val signInIntent =  googleSignInClient.signInIntent
                startActivityForResult(signInIntent,googleSignInRequestCode)
            }else{
                longToastShow("No Internet Connection!")
            }
        }


        val forgotPasswordTxt = findViewById<TextView>(R.id.forgotPasswordTxt)
        forgotPasswordTxt.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            googleSignInRequestCode -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)

                }catch (e:ApiException){
                    e.printStackTrace()
                    e.message?.let { longToastShow(it) }
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener {
                longToastShow("Login Successful")
                val mainIntent = Intent(this,MainActivity::class.java)
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