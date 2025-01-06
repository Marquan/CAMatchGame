package iss.nus.edu.sg.ca.matchgame

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.ByteArrayOutputStream

class FetchActivity : AppCompatActivity() {
    //save URL
    var webUrl = ""

    //variable to store images URL
    private var imagesUrl = mutableListOf<String>()

    //variable to store image bitmap that was read
    private var imageBitmap = mutableListOf<Bitmap>()

    //list to check whether images are pressed or not
    private var buttonStates = MutableList(20) { false } // Initially all unpressed

    //establish background thread
    private var bkgdThread:Thread? = null

    //variables for service, set bound to false
    private var svc: DownloadService? = null
    private var isBound: Boolean? = false

    //set up service to connection
    val conn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            svc = (p1 as DownloadService.LocalBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
            svc = null
        }

    }

    //create BroadcastReceiver object
    private val recv = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent){

            //get intent action
            val action = intent.action

            //if intent action is update
            if(action.equals("Update")){

                //update progress UI
                runOnUiThread {
                    //get the progress counter from intent
                    val counter = intent.getIntExtra("Counter", 0)
                    val textUpdate = "Downloading ${counter}/20 images"
                    val updateText = findViewById<TextView>(R.id.progressText)
                    val progressBar = findViewById<ProgressBar>(R.id.progressBar)

                    //update text and progress value
                    updateText.text = textUpdate
                    Log.e("text update","${updateText.text}")
                    progressBar.progress = counter
                    Log.e("progressBar update","${progressBar.progress}")

                }

            }
        }
    }

    //function for subscribe to broadcast
    protected fun subscribeToActions(){
        val filter = IntentFilter()
        filter.addAction("Update")
        registerReceiver(recv, filter, RECEIVER_EXPORTED)
    }

    //function for unsubscribe to broadcast
    protected fun unsubscribeToActions(){
        unregisterReceiver(recv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fetch)
        val username = intent.getStringExtra("username") ?: "Guest"
        val userId = intent.getIntExtra("userId",-1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //fetch download button and grid view
        val downloadBtn = findViewById<Button>(R.id.downloadBtn)
        val gridView = findViewById<GridView>(R.id.gridView)

        //bound to service
        val intent = Intent(this@FetchActivity, DownloadService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)

        //subscribe to broadcast
        subscribeToActions()

        //fetch progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        //set the max val to 20
        progressBar.max = 20

        //set progress value to 0
        progressBar.progress = 0

        //establish placeholder cards first
        //create bitmap of the card placeholder picture located in R.drawable
        val placeholderBitmap = BitmapFactory.decodeResource(resources, R.drawable.card_placeholder)

        //create a list of bitmap
        val placeholderImages : MutableList<Bitmap> = mutableListOf()

        //multiply the pictures to 20 and store in placeholderImages list
        for (i in 1..20) {
            placeholderImages.add(placeholderBitmap)
        }

        //set the card background as the placeholder image first
        //send the following data to the adapter
        //populate the imageButton with placeholder image, set the buttonStates as all false, and set the imageButton to be disabled
        val adapter = ImageAdapter(this, placeholderImages, buttonStates, false)

        //load the placeholder cards
        runOnUiThread {
            gridView.adapter = adapter
        }

        //set up download button click function
        downloadBtn.setOnClickListener{

            //if download is already in progress, interrupt thread to restart
            if (bkgdThread != null && bkgdThread!!.isAlive) {
                Log.e("Thread", "Restarting...")
                bkgdThread!!.interrupt()
                bkgdThread = null
            }

            //set background thread to process get image
            bkgdThread = Thread{
                //reset the progress bar value to 0
                progressBar.progress = 0

                //reset images and disable button before finish downloading

                runOnUiThread{
                    val tempImg = MutableList(20) {
                        BitmapFactory.decodeResource(resources, R.drawable.card_placeholder)
                    }
                    adapter.updateEnableButtons(false)
                    adapter.updateImages(tempImg)
                }

                //if service is available
                if (isBound==true && svc != null){
                    //get URL from input
                    val urlInput = findViewById<TextView>(R.id.urlInput)
                    webUrl = urlInput.text.toString()

                    //access the website to get top 20 images URL
                    imagesUrl = svc?.getHtml(webUrl)!!

                    //get the images from the image URLs and save it to a bitmap list
                    imageBitmap = svc?.getImage(imagesUrl)!!

                    //if image list is not empty, send the list of image to adapter
                    if(imageBitmap.isNotEmpty()&&imageBitmap.size==20){
                        Log.e("Download result","Image list is populated")
                        runOnUiThread {
                            //update the card images using adapter
                            adapter.updateImages(imageBitmap)

                            //enable the imageButtons to be clickable
                            adapter.updateEnableButtons(true)
                        }
                    }
                    else{
                        Log.e("Service", "Service not bound or unavailable.")
                        runOnUiThread {
                            //alert that fetch image failed
                            Toast.makeText(this, "Failed to fetch images or image not enough", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            bkgdThread?.start()
        }

        //set up play button
        val playBtn = findViewById<Button>(R.id.playBtn)
        playBtn.setOnClickListener {
            //check the number of cards that is chosen
            val numOfCardChose = buttonStates.count { it }

            if(numOfCardChose==6){
                //convert the images to byteArray to be sent through intent
                val byteArrayList = ArrayList<ByteArray>()
                for (i in buttonStates.indices) {
                    if(buttonStates[i]){
                        val bitmap = imageBitmap[i]
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        if(imagesUrl[i].contains(".png")){
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        }
                        else{
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        }
                        byteArrayList.add(byteArrayOutputStream.toByteArray())

                    }
                }

                //TODO set up which activity to move next
                //TODO selected images will be send through intent
                Log.e("FetchActivity","Creating Play Activity intent")
                val playIntent = Intent(this, PlayActivity::class.java)
                Log.e("FetchActivity","Passing username $username to intent")
                playIntent.putExtra("username", username)
                Log.e("FetchActivity","Passing userId $userId to intent")
                playIntent.putExtra("userId", userId)
                Log.e("FetchActivity","Passing bitmaps to intent")
                playIntent.putExtra("bitmaps",byteArrayList)
                Log.e("FetchActivity","Starting Play Activity")
                startActivity(playIntent)


            }
            else{
                val msg = Toast.makeText(this, "Please choose only 6!", Toast.LENGTH_SHORT)
                msg.show()
            }
        }

    }

    //function to toggle pressed/unpressed state for images
    fun toggleButtonState(position: Int) {

        //toggle the state
        buttonStates[position] = !buttonStates[position]

        //get grid view
        val gridView = findViewById<GridView>(R.id.gridView)

        //update grid view
        (gridView.adapter as ImageAdapter).notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound == true) {
            unbindService(conn)
            isBound = false
        }
        unsubscribeToActions()
    }


}
