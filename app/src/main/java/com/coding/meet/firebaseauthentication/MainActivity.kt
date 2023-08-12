package com.coding.meet.firebaseauthentication

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadingDialog = Dialog(this, R.style.DialogCustomTheme).apply {
            setContentView(R.layout.loading_dialog)
            window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
        }

        val edName = findViewById<TextInputEditText>(R.id.edName)
        val edNameL = findViewById<TextInputLayout>(R.id.edNameL)

        edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateName(edName, edNameL)
            }

        })


        val edOriginalEmail = findViewById<TextInputEditText>(R.id.edOriginalEmail)
        val edOriginalEmailL = findViewById<TextInputLayout>(R.id.edOriginalEmailL)

        edOriginalEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEmail(edOriginalEmail, edOriginalEmailL)
            }

        })


        val edNewEmail = findViewById<TextInputEditText>(R.id.edNewEmail)
        val edNewEmailL = findViewById<TextInputLayout>(R.id.edNewEmailL)

        edNewEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEmail(edNewEmail, edNewEmailL)
            }

        })


        val edOriginalPassword = findViewById<TextInputEditText>(R.id.edOriginalPassword)
        val edOriginalPasswordL = findViewById<TextInputLayout>(R.id.edOriginalPasswordL)

        edOriginalPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validatePassword(edOriginalPassword, edOriginalPasswordL)
            }

        })


        val edNewPassword = findViewById<TextInputEditText>(R.id.edNewPassword)
        val edNewPasswordL = findViewById<TextInputLayout>(R.id.edNewPasswordL)

        edNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validatePassword(edNewPassword, edNewPasswordL)
            }

        })


        // Display current User Profile
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            val userName = currentUser.displayName
            edOriginalEmail.setText(email)
            edName.setText(userName)

            val profileImg = findViewById<ImageView>(R.id.profileImg)
            currentUser.photoUrl?.let {
                profileImg.setImageURI(it)
            }

            var profileUri: Uri? = null
            val singlePhotoPickerLauncher =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    profileImg.setImageURI(uri)
                    profileUri = uri
                }

            profileImg.setOnClickListener {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }


            val updateUserNameProfilePicBtn = findViewById<Button>(R.id.updateUserNameProfilePicBtn)
            updateUserNameProfilePicBtn.setOnClickListener {
                if (validateName(edName, edNameL)) {
                    if (isConnected(this)) {
                        loadingDialog.show()

                        val profileUpdate = UserProfileChangeRequest.Builder().apply {
                            displayName = edName.text.toString().trim()

                            profileUri?.let {
                                photoUri = it
                            }
                        }.build()

                        currentUser.updateProfile(profileUpdate).addOnSuccessListener {
                            loadingDialog.dismiss()
                            longToastShow("UserName & Profile Pic Updated Successfully")
                        }.addOnFailureListener {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }

                    } else {
                        longToastShow("No Internet Connection!")
                    }
                }
            }


            // Update Email
            val updateEmailBtn = findViewById<Button>(R.id.updateEmailBtn)
            updateEmailBtn.setOnClickListener {
                if (validateEmail(edOriginalEmail, edOriginalEmailL)
                    && validateEmail(edNewEmail, edNewEmailL)
                    && validatePassword(edOriginalPassword, edOriginalPasswordL)
                ) {
                    if (isConnected(this)) {
                        loadingDialog.show()
                        val credentials = EmailAuthProvider.getCredential(
                            edOriginalEmail.text.toString().trim(),
                            edOriginalPassword.text.toString().trim()
                        )
                        currentUser.reauthenticate(credentials).addOnSuccessListener {
                            currentUser.updateEmail(
                                edNewEmail.text.toString().trim()
                            ).addOnSuccessListener {
                                loadingDialog.dismiss()
                                longToastShow("Email id Updated Successfully")

                            }.addOnFailureListener {
                                loadingDialog.dismiss()
                                it.message?.let { it1 -> longToastShow(it1) }
                            }
                        }.addOnFailureListener {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }


                    } else {
                        longToastShow("No Internet Connection!")
                    }
                }
            }



            // Update Password
            val updatePasswordBtn = findViewById<Button>(R.id.updatePasswordBtn)
            updatePasswordBtn.setOnClickListener {
                if (validateEmail(edOriginalEmail, edOriginalEmailL)
                    && validatePassword(edOriginalPassword, edOriginalPasswordL)
                    &&   validatePassword(edNewPassword, edNewPasswordL)
                ) {
                    if (isConnected(this)) {
                        loadingDialog.show()
                        val credentials = EmailAuthProvider.getCredential(
                            edOriginalEmail.text.toString().trim(),
                            edOriginalPassword.text.toString().trim()
                        )
                        currentUser.reauthenticate(credentials).addOnSuccessListener {
                            currentUser.updatePassword(
                                edNewPassword.text.toString().trim()
                            ).addOnSuccessListener {
                                loadingDialog.dismiss()
                                longToastShow("Password Updated Successfully")
                            }.addOnFailureListener {
                                loadingDialog.dismiss()
                                it.message?.let { it1 -> longToastShow(it1) }
                            }
                        }.addOnFailureListener {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }


                    } else {
                        longToastShow("No Internet Connection!")
                    }
                }
            }


            val deleteAccountBtn = findViewById<Button>(R.id.deleteAccountBtn)
            deleteAccountBtn.setOnClickListener {
                currentUser.delete()
                    .addOnSuccessListener {
                        longToastShow("Delete Account Successful")
                        val mainIntent = Intent(this, LoginActivity::class.java)
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
            val mainIntent = Intent(this, LoginActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainIntent)
            finish()
        }
    }
}