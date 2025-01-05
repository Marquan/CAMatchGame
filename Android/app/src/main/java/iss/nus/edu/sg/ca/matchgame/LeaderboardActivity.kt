package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.ca.matchgame.data.models.LeaderboardItem
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL


class LeaderboardActivity : AppCompatActivity() {
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var recyclerView: RecyclerView
    private val leaderboardList = mutableListOf<LeaderboardItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        leaderboardAdapter = LeaderboardAdapter(leaderboardList)
        recyclerView.adapter = leaderboardAdapter

        fetchLeaderboardData()
    }

    private fun fetchLeaderboardData() {
        val username = intent.getStringExtra("username") ?: "Guest"
        val timeTaken = intent.getIntExtra("timeTaken", 86400) // 86400s is 24h
        Log.d("LeaderboardActivity", "$username Time: $timeTaken")

        // Perform the GET request to fetch leaderboard data
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
}

