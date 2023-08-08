package com.coding.meet.firebaseauthentication

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val loadingDialog = Dialog(this,R.style.DialogCustomTheme).apply {
            setContentView(R.layout.loading_dialog)
            window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
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

        val sendEmailBtn = findViewById<Button>(R.id.sendEmailBtn)
        sendEmailBtn.setOnClickListener {
            if (validateEmail(edEmail, edEmailL)) {
                if (isConnected(this)){
                    loadingDialog.show()
                    FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(
                            edEmail.text.toString().trim())
                        .addOnSuccessListener {
                            loadingDialog.dismiss()
                            longToastShow("Send Email Successful")
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
    }
}