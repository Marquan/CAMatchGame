package iss.nus.edu.sg.ca.matchgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.MobileAds
import iss.nus.edu.sg.ca.matchgame.Constants.Constants
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PlayActivity : AppCompatActivity() {

    private var theGame: GameFragment? = null
    private lateinit var username: String
    private var isPaidUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        val sharedPrefs = getSharedPreferences(Constants.USER_CREDENTIALS_FILE, MODE_PRIVATE)
        val username = intent.getStringExtra("username") ?: "Guest"
        fetchUserIsPaidStatus(username)
        Log.d("PlayActivity", "isPaidUser: $isPaidUser")

        if (isPaidUser) {
            makeToast("Enjoy your ad-free experience!")
        }

        else {
            initializeAdMobAndLoadAdFragment()
        }

        val fm: FragmentManager = getSupportFragmentManager()

            theGame = fm.findFragmentById(R.id.for_game) as GameFragment?

            Log.e("PlayActivity", "Unpacking intent")
            var intent = getIntent()
            var chosenBitmaps = intent.getSerializableExtra("bitmaps") as ArrayList<ByteArray>
            if (chosenBitmaps.size < 6) {
                Log.e("PlayActivity", "Downloading before start")
                theGame?.downloadBeforeStart()
            } else {
                Log.e("PlayActivity", "Adding from Bitmap")
                theGame?.addFromBitmapList(chosenBitmaps)
            }
            //theGame?.downloadBeforeStart()
    }
        private fun fetchUserIsPaidStatus(username: String) {
            val url = URL("http://10.0.2.2:5126/api/Users/GetUser?username=$username")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")

            try {
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val responseObject = JSONObject(response)
                    isPaidUser = responseObject.getBoolean("isPaidUser")
                    Log.d("PlayActivity", "isPaidUser: $isPaidUser")
                } else {
                    Log.e("PlayActivity", "Error: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("PlayActivity", "Network error: ${e.localizedMessage}")
            } finally {
                connection.disconnect()
            }
        }

        private fun initializeAdMobAndLoadAdFragment() {
            MobileAds.initialize(this) { initializationStatus ->
                Log.d("PlayActivity", "AdMob SDK initialized: $initializationStatus")

                val adFragment =
                    supportFragmentManager.findFragmentByTag(AdFragment::class.java.simpleName)
                if (adFragment == null) {
                    Log.d("PlayActivity", "Loading AdFragment")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.for_ad, AdFragment(), AdFragment::class.java.simpleName)
                        .commitAllowingStateLoss()
                } else {
                    Log.d("PlayActivity", "AdFragment already loaded")
                }
            }
        }

        private fun makeToast(text: String) {
            val msg = Toast.makeText(
                this,
                text, Toast.LENGTH_LONG
            )
            msg.show()
        }

        public fun onWin(timeTaken: Int, matchAttempts: Int, matches: Int) {
            makeToast("You Win!")
            val handler = android.os.Handler()
            handler.postDelayed({
                makeToast("Redirecting to Leaderboard...")
                //finish()

                Log.d("PlayActivity", "About to start leaderboard")
                val intent = Intent(this, LeaderboardActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("timeTaken", timeTaken)
                intent.putExtra("matchAttempts", matchAttempts)
                intent.putExtra("matches", matches)
                startActivity(intent)
            }, 3000) // 3000ms = 3 seconds
        }
    }

