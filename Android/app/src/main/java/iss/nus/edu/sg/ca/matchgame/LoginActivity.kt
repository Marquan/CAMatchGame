package iss.nus.edu.sg.ca.matchgame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.ca.matchgame.Constants.Constants
import iss.nus.edu.sg.ca.matchgame.data.models.LoginRequest
import iss.nus.edu.sg.ca.matchgame.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(Constants.USER_CREDENTIALS_FILE, MODE_PRIVATE)

        initButtons()
    }

    private fun showToast (message: String) {
        Toast.makeText (applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun initButtons() {
        binding.loginBtn.setOnClickListener {
            val username = binding.loginText.text.toString()
            val password = binding.passwordText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Username and password cannot be empty")
            } else {
                logIn(username, password)
            }
        }
    }

// For mock data only
//    private fun logIn(username: String?, password: String?): Boolean {
//        // Mocked logic for successful and unsuccessful login
//        return when {
//            username == "mockuser" && password == "mockpassword" -> {
//                showToast("Mock: Login successful")
//                true
//            }
//            username.isNullOrEmpty() || password.isNullOrEmpty() -> {
//                showToast("Mock: Username or password cannot be empty")
//                false
//            }
//            else -> {
//                showToast("Mock: Invalid credentials")
//                false
//            }
//        }
//    }

    private fun startFetchActivity() {
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }

    private fun logIn(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        Thread {
            try {
                val response = sendLoginRequest(loginRequest)

                runOnUiThread {
                    if (response.contains("Login successful")) {
                        saveCredentialsData(username, password)
                        sharedPrefs.edit().putBoolean("isPaidUser", true).apply()
                        showToast("Login successful")
                        startFetchActivity()
                    } else {
                        showToast("Login failed: $response")
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showToast("Network error: ${e.localizedMessage}")
                }
            }
        }.start()
    }

    private fun sendLoginRequest(loginRequest: LoginRequest): String {
        val url = URL("http://10.0.2.2:5126/api/Users/Login")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        val jsonInputString = "{\"username\": \"${loginRequest.username}\", \"password\": \"${loginRequest.password}\"}"
        val out = OutputStreamWriter(conn.outputStream)
        out.write(jsonInputString)
        out.flush()
        out.close()

        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            val response = conn.inputStream.bufferedReader().readText()
            conn.disconnect()

            // Assuming the response is JSON
            val responseObject = JSONObject(response)
            val status = responseObject.getBoolean("status")
            val message = responseObject.getString("message")

            return if (status) {
                "Login successful"
            } else {
                "Login failed: $message"
            }
        } else {
            conn.disconnect()
            return "Error: ${conn.responseCode}"
        }
    }

    private fun saveCredentialsData(username: String, password: String) {
        val editor = sharedPrefs.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
}