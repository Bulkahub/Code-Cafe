package com.example.cafeapp.repository

import android.content.Context
import android.util.Log
import com.example.cafeapp.authmanager.AuthManager
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository responsible for user registration and authentication.
 * Stores credentials in Firestore and saves the current user's ID in SharedPreferences.
 */
class UsersRepository(private val context: Context) {

    // Connection to Firestore and local preferences storage.
    private val firestore = FirebaseFirestore.getInstance()
    private val sharedPreferences =
        context.getSharedPreferences("CaffeAppPref", Context.MODE_PRIVATE)


    /**
     * Registers a new user:
     * - Validates input fields
     * - Generates a unique userId
     * - Saves user data to Firestore
     * - Locally stores the current user's userId
     *
     * @param userName the username
     * @param password the password
     * @param callback result of the operation (success/failure + message)
     */
    fun createAccount(userName: String, password: String, callback: (Boolean, String) -> Unit) {
        // Check that fields are not empty and password is long enough.
        if (userName.isBlank() || password.isBlank()) {
            callback(false, "Invalid input")
            return
        }
        if (password.length < 6) {
            callback(false, "Password must be at least 6 characters")
            return
        }

        // Generate unique ID and prepare user data.
        val userId = firestore.collection("Users").document().id// Pre-generate ID.
        val userMap = hashMapOf(
            "userId" to userId,
            "userName" to userName.trim(),
            "password" to password.trim()
        )

        // Save user to Firestore.
        firestore.collection("Users").document(userId)
            .set(userMap)// Use set() with fixed ID.
            .addOnSuccessListener {
                // Successful registration — save ID to SharedPreferences.
                sharedPreferences.edit().putString("loggedInUser", userId).apply()
                callback(true, "Account created successfully")
            }
            .addOnFailureListener { e ->
                callback(false, "Error creating account")
            }
    }

    /**
     * Attempts to log in using username and password:
     * - Validates that fields are not empty
     * - Searches for user by username
     * - Compares password
     * - Saves ID to SharedPreferences on success
     *
     * @param userName the username
     * @param password the password
     * @param callback result (success/failure + message)
     */
    fun login(userName: String, password: String, callback: (Boolean, String?) -> Unit) {
        // Check that fields are not empty.
        if (userName.isBlank() || password.isBlank()) {
            callback(false, null)
            return
        }
        firestore.collection("Users").whereEqualTo("userName", userName).limit(1).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.documents[0]
                    val storedPassword = user.getString("password") ?: ""
                    val userId = user.id

                    if (storedPassword == password) {
                        val authManager = AuthManager(context)
                        authManager.saveSession(userId, userName.trim())
                        callback(true, userId)
                    } else {
                        callback(false, null)
                    }
                } else {
                    callback(false, null)
                }
            }
            .addOnFailureListener { e ->
                callback(false, null)
            }
    }
}