package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.MobileAds
import iss.nus.edu.sg.ca.matchgame.Constants.Constants

class PlayActivity : AppCompatActivity() {

    private var theGame: GameFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        val sharedPrefs = getSharedPreferences(Constants.USER_CREDENTIALS_FILE, MODE_PRIVATE)
        //var username = sharedPrefs.getString("username",null)
        val isPaidUser = sharedPrefs.getBoolean("isPaidUser",false)

        // Initialize the AdMob SDK
        MobileAds.initialize(this) { initializationStatus ->
            Log.d("MainActivity", "AdMob SDK initialized: $initializationStatus")

            if (!isPaidUser) {
                // After initialization is complete, load the AdFragment
                val adFragment =
                    supportFragmentManager.findFragmentByTag(AdFragment::class.java.simpleName)
                if (adFragment == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.for_ad, AdFragment())
                        .commit()
                }
            }

        }
        val fm: FragmentManager = getSupportFragmentManager()

        theGame = fm.findFragmentById(R.id.for_game) as GameFragment?

        Log.e("PlayActivity","Unpacking intent")
        var intent = getIntent()
        var chosenBitmaps = intent.getSerializableExtra("bitmaps") as ArrayList<ByteArray>
        if (chosenBitmaps.size < 6) {
            Log.e("PlayActivity","Downloading before start")
            theGame?.downloadBeforeStart()
        } else {
            Log.e("PlayActivity","Adding from Bitmap")
            theGame?.addFromBitmapList(chosenBitmaps)
        }
        //theGame?.downloadBeforeStart()
    }

    private fun makeToast(text: String) {
        val msg = Toast.makeText(
            this,
            text, Toast.LENGTH_LONG
        )
        msg.show()
    }


    public fun onWin(time_taken: Int, match_attempts: Int, matches: Int) {
        makeToast("You Win!")
        val sharedPrefs = getSharedPreferences(Constants.USER_CREDENTIALS_FILE, MODE_PRIVATE)
        var username = sharedPrefs.getString("username",null)
        //val isPaidUser = sharedPrefs.getBoolean("isPaidUser",false)

    }

}