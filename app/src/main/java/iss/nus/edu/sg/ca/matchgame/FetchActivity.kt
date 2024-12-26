package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.ca.matchgame.databinding.ActivityFetchBinding
import iss.nus.edu.sg.ca.matchgame.databinding.ActivityLoginBinding
import iss.nus.edu.sg.ca.matchgame.databinding.FragmentGameBinding

class FetchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFetchBinding

    private val pvList: MutableList<ImageButton> = mutableListOf() // the list of buttons


    fun fillPvList() {
        pvList.clear()
        binding.apply {
            pvList.add(imgPv01)
            pvList.add(imgPv02)
            pvList.add(imgPv03)
            pvList.add(imgPv04)
            pvList.add(imgPv05)
            pvList.add(imgPv06)
            pvList.add(imgPv07)
            pvList.add(imgPv08)
            pvList.add(imgPv09)
            pvList.add(imgPv10)
            pvList.add(imgPv11)
            pvList.add(imgPv12)
            pvList.add(imgPv13)
            pvList.add(imgPv14)
            pvList.add(imgPv15)
            pvList.add(imgPv16)
            pvList.add(imgPv17)
            pvList.add(imgPv18)
            pvList.add(imgPv19)
            pvList.add(imgPv20)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fetch)
        binding = ActivityFetchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillPvList()
    }
}