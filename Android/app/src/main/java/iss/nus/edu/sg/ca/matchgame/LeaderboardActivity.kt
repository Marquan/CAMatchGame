package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.ca.matchgame.data.models.LeaderboardItem
import org.json.JSONArray
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class LeaderboardActivity : AppCompatActivity() {
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var recyclerView: RecyclerView
    private val leaderboardList = mutableListOf<LeaderboardItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LeaderboardActivity", "onCreate Start")
        super.onCreate(savedInstanceState)
        Log.d("LeaderboardActivity", "set content view")
        setContentView(R.layout.activity_leaderboard)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        leaderboardAdapter = LeaderboardAdapter(leaderboardList)
        recyclerView.adapter = leaderboardAdapter

        fetchLeaderboardData()
    }

    private fun assignNewScore() {
        val currentusername = intent.getStringExtra("username") ?: "Guest"
        val userId = intent.getIntExtra("userId",-1)
        val timeTaken = intent.getIntExtra("timeTaken", -1) // 86400s is 24h
        if (timeTaken == -1) {
            return
        }
        Log.d("LeaderboardActivity", "$userId: $currentusername Time: $timeTaken")
        val url = URL("http://10.0.2.2:5126/api/Users/UpdateTime")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "PUT"
        conn.setRequestProperty("Content-Type", "application/json")

        val jsonInputString = "{\"UserId\": \"${userId}\", \"updateTime\": \"${timeTaken}\"}"
        try {
            val out = OutputStreamWriter(conn.outputStream)
            out.write(jsonInputString)
            out.flush()
            out.close()

            if (conn.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                showToast("Could not update: User with Id '$userId' was not found")
            } else if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                showToast("User with Id '$userId' score updated successfully")
            }
        } catch (e: Exception) {
            showToast("Error when updating scores: ${e.localizedMessage}")
        }
        //val connection = URL(url).openConnection() as HttpURLConnection
        //connection.requestMethod = "PUT"
        //connection.setRequestProperty("Content-Type", "application/json")
    }

    private fun fetchLeaderboardData() {
        assignNewScore()

        // Perform the GET request to fetch leaderboard data
        Log.d("LeaderboardActivity", "Connecting to TopUsers")
        val url = "http://10.0.2.2:5126/api/Leaderboard/TopUsers"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")

        Thread {
            try {
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonArray = JSONArray(response)

                    leaderboardList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val username = item.getString("username")
                        val gameTime = item.getInt("gameTime")
                        leaderboardList.add(LeaderboardItem(username, gameTime))
                    }

                    runOnUiThread {
                        leaderboardAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("LeaderboardActivity", "Error: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("LeaderboardActivity", "Network error: ${e.localizedMessage}")
            } finally {
                connection.disconnect()
            }
        }.start()
    }

    private fun showToast (message: String) {
        Toast.makeText (applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}

