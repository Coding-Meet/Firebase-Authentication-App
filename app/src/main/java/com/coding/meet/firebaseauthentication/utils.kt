package com.coding.meet.firebaseauthentication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun Context.longToastShow(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

fun validateConPassword(
    edPassword: TextInputEditText,
    edConPassword: TextInputEditText,
    edConPasswordL: TextInputLayout,
): Boolean {

    return when {
        edConPassword.text.toString().trim().isEmpty() -> {
            edConPasswordL.error = "Required"
            false
        }

        edConPassword.text.toString().trim().length < 8 || edConPassword.text.toString()
            .trim().length > 10 -> {
            edConPasswordL.error = "Password must be 8 to 10 Character!"
            false

        }

        edPassword.text.toString().trim() != edConPassword.text.toString().trim() -> {
            edConPasswordL.error = "Password Don't Match!"
            false
        }

        else -> {
            edConPasswordL.error = null
            true
        }
    }
}

fun validatePassword(
    edPassword: TextInputEditText,
    edPasswordL: TextInputLayout,
): Boolean {
    return when {
        edPassword.text.toString().trim().isEmpty() -> {
            edPasswordL.error = "Required"
            false
        }

        edPassword.text.toString().trim().length < 8 || edPassword.text.toString()
            .trim().length > 10 -> {
            edPasswordL.error = "Password must be 8 to 10 Character!"
            false
        }

        else -> {
            edPasswordL.error = null
            true
        }
    }
}

fun validateEmail(edEmail: TextInputEditText, edEmailL: TextInputLayout): Boolean {
    val emailPattern = Regex("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+")
    return when {
        edEmail.text.toString().trim().isEmpty() -> {
            edEmailL.error = "Required"
            false
        }

        !edEmail.text.toString().trim().matches(emailPattern) -> {
            edEmailL.error = "Valid E-mail"
            false
        }

        else -> {
            edEmailL.error = null
            true
        }
    }
}

fun validateName(edName: EditText, edNameL: TextInputLayout): Boolean {
    return when {
        edName.text.toString().trim().isEmpty() -> {
            edNameL.error = "Required"
            false
        }

        else -> {
            edNameL.error = null
            true
        }
    }
}

fun isConnected(context: Context):Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val cap = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            when {
                cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else -> {
            // use Deprecated method only on older devices
            val activeNetwork = connectivityManager.activeNetworkInfo ?: return false
            return when (activeNetwork.type){
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_VPN -> true
                else -> false
            }
        }
    }
}