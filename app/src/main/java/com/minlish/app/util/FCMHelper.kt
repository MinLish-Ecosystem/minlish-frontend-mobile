package com.minlish.app.util

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.minlish.app.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FCMHelper {
    private const val TAG = "FCMHelper"
    private val repository = UserRepository()
    private val scope = CoroutineScope(Dispatchers.IO)
    fun registerFCMToken(context: Context) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            scope.launch {
                try {
                    val deviceId = Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    repository.registerFCMToken(token, deviceId)
                    Log.d(TAG, "FCM Token registered: ${token.take(10)}...")
                } catch (e: Exception) {
                    Log.e(TAG, "FCM Token registration failed", e)
                }
            }
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to get FCM Token", e)
        }
    }

    fun deleteFCMToken(context: Context, onComplete: () -> Unit) {
        scope.launch {
            try {
                val deviceId = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                repository.deleteFCMToken(deviceId)
                Log.d(TAG, "FCM Token deleted successfully on server")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete FCM Token on server", e)
            } finally {
                onComplete()
            }
        }
    }
}
