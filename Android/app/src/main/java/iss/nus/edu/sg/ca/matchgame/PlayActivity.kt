package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.MobileAds

class PlayActivity : AppCompatActivity() {

    private var theGame: GameFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.play_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        theGame?.downloadBeforeStart()
    }

    private fun makeToast(text: String) {
        val msg = Toast.makeText(
            this,
            text, Toast.LENGTH_LONG
        )
        msg.show()
    }
}