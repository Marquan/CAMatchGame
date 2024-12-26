package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.ca.matchgame.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {

    private var _binding: ActivityPlayBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_play)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun makeToast(text: String) {
        val msg = Toast.makeText(
            this,
            text, Toast.LENGTH_LONG
        )
        msg.show()
    }

}