package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
<<<<<<< HEAD
=======
import android.util.Log
>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
<<<<<<< HEAD

class PlayActivity : AppCompatActivity() {
=======
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.MobileAds

class PlayActivity : AppCompatActivity() {

    private var theGame: GameFragment? = null

>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)
<<<<<<< HEAD
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
=======
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.play_main)) { v, insets ->
>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
<<<<<<< HEAD
=======

        // Initialize the AdMob SDK
        MobileAds.initialize(this) { initializationStatus ->
            Log.d("MainActivity", "AdMob SDK initialized: $initializationStatus")

            // After initialization is complete, load the AdFragment
            val adFragment =
                supportFragmentManager.findFragmentByTag(AdFragment::class.java.simpleName)
            if (adFragment == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.for_ad, AdFragment())
                    .commit()
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
>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
    }

    private fun makeToast(text: String) {
        val msg = Toast.makeText(
            this,
            text, Toast.LENGTH_LONG
        )
        msg.show()
    }

<<<<<<< HEAD
=======

    public fun onWin(time_taken: Int, match_attempts: Int, matches: Int) {
        makeToast("You Win!")
    }

>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
}