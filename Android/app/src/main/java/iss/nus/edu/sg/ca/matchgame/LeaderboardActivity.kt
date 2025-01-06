package iss.nus.edu.sg.ca.matchgame

import android.content.Intent
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


    private fun fetchLeaderboardData() {
        val url = "http://10.0.2.2:5126/api/Leaderboard/TopUsers"

        Thread {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Content-Type", "application/json")
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

                    leaderboardList.sortBy { it.gameTime } // 按游戏时间升序排序

                    runOnUiThread {
                        leaderboardAdapter.notifyDataSetChanged() // 刷新 RecyclerView
                    }
                } else {
                    Log.e("LeaderboardActivity", "Error: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("LeaderboardActivity", "Network error: ${e.localizedMessage}")
            } finally {
                connection?.disconnect()
            }
        }.start()
    }


    private fun showToast (message: String) {
        Toast.makeText (applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}

