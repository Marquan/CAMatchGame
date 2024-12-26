package iss.nus.edu.sg.ca.matchgame

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RECEIVER_EXPORTED
import androidx.fragment.app.Fragment
import iss.nus.edu.sg.ca.matchgame.databinding.FragmentGameBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var chose1: Int = -1 // the 'first' card that is flipped to try and match
    private var chose2: Int = -1 // the 'second card that is flipped to try and match
    private val imgArrangement = listOf(0,0,1,1,2,2,3,3,4,4,5,5).shuffled() // the image each button should hold
    private val imgBitmaps: MutableList<Bitmap> = mutableListOf() // the list of bitmaps
    private val buttonList: MutableList<ImageButton> = mutableListOf() // the list of buttons
    private val isCleared: MutableList<Boolean> = mutableListOf() // True if the card is 'cleared' false otherwise
    private var matchAttempts: Int = 0 // how many times you tried to match
    private var matches: Int = 0 // how many successful matches you have made
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    // Define the BroadcastReceiver to handle the broadcast
    val recv2 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("MyReceiver","onReceive has received a broadcast")
            //val success = intent.getBooleanExtra("success", false)
            val filePath = intent.getStringExtra("file_path")
            Log.e("MyReceiver","Filepath: ${filePath}")
            if (filePath != null) {
                // Load the image into the ImageView
                val bitmap = BitmapFactory.decodeFile(filePath)
                if (bitmap != null) {
                    imgBitmaps.add(bitmap)
                    Log.e("MyReceiver","Bitmap count: ${imgBitmaps.size}")
                    if (imgBitmaps.size >= TestSettings.getImgLinks().size) {
                        Log.e("MyReceiver","All ${TestSettings.getImgLinks().size} image(s) downloaded")
                        startTheGame()
                    }
                //imageView.setImageBitmap(bitmap)
                } else {
                    Log.e("MyReceiver", "Failed to decode bitmap from file path: $filePath")
                }
            } else {
                // Handle the failure case
            }
        }
    }

    private var seconds = 0
    private var isRunning = false


    fun fillButtonList() {
        buttonList.clear()
        binding.apply {
            buttonList.add(imgCard01)
            buttonList.add(imgCard02)
            buttonList.add(imgCard03)
            buttonList.add(imgCard04)
            buttonList.add(imgCard05)
            buttonList.add(imgCard06)
            buttonList.add(imgCard07)
            buttonList.add(imgCard08)
            buttonList.add(imgCard09)
            buttonList.add(imgCard10)
            buttonList.add(imgCard11)
            buttonList.add(imgCard12)
        }
    }


    fun startTheGame() {
        binding.apply {
            Log.e("GameFragment","Starting the Game")
            for (i in 0..buttonList.size-1) {
                isCleared.add(false)
                val theButton = buttonList[i]
                theButton.setOnClickListener {
                    flipCard(i)
                }
            }
            isRunning = true
        }
        runTimer()
        makeToast("Game Start!")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = FragmentGameBinding.inflate(layoutInflater)
        fillButtonList()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        for (i in 0..TestSettings.getImgLinks().size-1) {
            val url = TestSettings.getImgLink(i)
            val intent = Intent(getActivity(), DownloadService::class.java)
            intent.action = "download_file"
            intent.putExtra("url", url)
            intent.putExtra("nameNoExt","image_${i}")
            Log.e("GameFragment","Starting download for ${i}: ${url}")
            getActivity()?.startService(intent)
        }

        return binding.root
    }

    fun flipCard(index: Int) {

        if (isCleared[index]){
            // don't bother if the card is already cleared
            makeToast("Card is already cleared")
            return
        } else if (index == chose1) {
            // don't bother if flipping the same card twice
            makeToast("Card is already opened")
            return
        } else if (chose2 != -1) {
            // don't bother if two cards are already opened
            makeToast("Two cards are already opened")
            return
        }
        // set the image
        val bitIndex = imgArrangement[index]
        val whichImage = buttonList[index]
        whichImage.setImageBitmap(imgBitmaps[bitIndex])

        if (chose1 == -1) {
            chose1 = index
        } else {
            chose2 = index
            // get image index of previous chosen
            val prevIndex = imgArrangement[chose1]
            // if similar check both of them
            if (bitIndex == prevIndex) {
                isCleared[index] = true
                isCleared[chose1] = true
                chose1 = -1
                chose2 = -1
                matches = matches + 1
                binding.matchCount.text = "Matches: ${matches}"
                if (matches >= TestSettings.getImgLinks().size) {
                    isRunning = false
                    makeToast("You Win!")
                }
            } else {
                val handler = Handler()
                handler.postDelayed(
                    {whichImage.setImageBitmap(null)
                    buttonList[chose1].setImageBitmap(null)
                    chose2 = -1
                    chose1 = -1
                    },
                    300)

            }
            matchAttempts = matchAttempts + 1
            binding.attemptCount.text = "Attempts: ${matchAttempts}"
        }
    }




    override fun onResume() {
        Log.e("MainActivity","onResume and registering receiver")
        super.onResume()
        // Register the BroadcastReceiver with NOT_EXPORTED flag for local use
        val filter = IntentFilter("download_completed")
        getActivity()?.registerReceiver(recv2, filter, RECEIVER_EXPORTED)
    }


    override fun onPause() {
        Log.e("MainActivity","onPause and unregistering receiver")
        super.onPause()
        // Unregister the BroadcastReceiver
        getActivity()?.unregisterReceiver(recv2)
    }


    private fun runTimer() {
        val timeShow = binding.timeShow


        // Creates a new Handler
        val handler = Handler()
        handler.postDelayed(
            {if (isRunning) {
                seconds += 1
                val minNo = seconds / 60
                val secNo = seconds % 60
                timeShow.setText("Time: ${"%02d".format(minNo)}:${"%02d".format(secNo)}")
                runTimer()
            }},
            1000)
    }


    private fun makeToast(text: String) {
        val msg = Toast.makeText(
            getActivity(),
            text, Toast.LENGTH_LONG
        )
        msg.show()
    }
}
