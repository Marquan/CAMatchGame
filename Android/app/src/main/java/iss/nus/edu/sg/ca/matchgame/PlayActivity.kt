package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.MobileAds
import iss.nus.edu.sg.ca.matchgame.Constants.Constants

class PlayActivity : AppCompatActivity() {

    private var theGame: GameFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        val sharedPrefs = getSharedPreferences(Constants.USER_CREDENTIALS_FILE, MODE_PRIVATE)
        val isPaidUser = getUserIsPaidStatus()
        Log.d("PlayActivity", "isPaidUser: $isPaidUser")


        if (!isPaidUser) {
            initializeAdMobAndLoadAdFragment()
        } else {
            Log.d("PlayActivity", "User is a paid user, no ads displayed.")
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

        private fun getUserIsPaidStatus(): Boolean {
            val sharedPrefs = getSharedPreferences(Constants.USER_CREDENTIALS_FILE, MODE_PRIVATE)
            return sharedPrefs.getBoolean("isPaidUser", false)
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

        public fun onWin(time_taken: Int, match_attempts: Int, matches: Int) {
            makeToast("You Win!")
            val isPaidUser = getUserIsPaidStatus()

            if (!isPaidUser) {
                makeToast("Enjoy your ad-free experience!")
            }
        }
    }

