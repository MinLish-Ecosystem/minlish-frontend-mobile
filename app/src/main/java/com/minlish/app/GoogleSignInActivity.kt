package com.minlish.app;


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.minlish.app.presentation.screens.auth.viewmodels.LoginViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHost
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.minlish.app.presentation.screens.auth.viewmodels.LoginUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class GoogleSignInActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        lifecycleScope.launch {
            try {
                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)

                val googleCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                val idToken = withContext(Dispatchers.IO) {
                    auth.signInWithCredential(googleCredential).await()
                    auth.currentUser
                        ?.getIdToken(true)
                        ?.await()
                        ?.token ?: throw Exception("idToken is null")
                }
                viewModel.googleSignIn(idToken)

            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign in thất bại: ${e.message}")
                finish()
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Lỗi: ${e.message}")
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Web_Client_ID))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInLauncher.launch(googleSignInClient.signInIntent)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is LoginUiEvent.GoogleSignInSuccess -> {
                            val intent = Intent(this@GoogleSignInActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                            finish()
                        }
                        else -> finish()
                    }
                }
            }
        }
    }
}
