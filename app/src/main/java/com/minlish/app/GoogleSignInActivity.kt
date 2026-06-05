package com.minlish.app;


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.minlish.app.presentation.screens.auth.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


class GoogleSignInActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    // 1. Khai báo cái Launcher này ở cấp độ lớp (Class level)
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Xử lý kết quả tại đây thay cho onActivityResult
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            Log.d("SUCCESS", "Đã lấy được token: $idToken")
            finish()
        } catch (e: ApiException) {
            Log.e("AUTH_ERROR", "Lỗi: ${e.statusCode}")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Web_Client_ID))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 2. Thay vì startActivityForResult, hãy dùng signInLauncher.launch()
        signInLauncher.launch(googleSignInClient.signInIntent)
    }
}
