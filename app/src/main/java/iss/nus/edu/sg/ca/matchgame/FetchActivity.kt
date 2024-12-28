package iss.nus.edu.sg.ca.matchgame

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FetchActivity : AppCompatActivity() {
    //variable to store images URL
    private var imagesUrl = mutableListOf<String>()

    //variable to store image bitmap that was read
    private var imageBitmap = mutableListOf<Bitmap>()

    //establish background thread
    private var bkgdThread:Thread? = null

    //variables for service
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fetch)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val webUrl = "https://stocksnap.io/"

        val downloadBtn = findViewById<Button>(R.id.downloadBtn)
        val gridView = findViewById<GridView>(R.id.gridView)

        val intent = Intent(this@FetchActivity, DownloadService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)

        downloadBtn.setOnClickListener{

            //if download is already in progress
            if (bkgdThread != null && bkgdThread!!.isAlive) {
                Log.e("Thread", "A download is already in progress.")
                return@setOnClickListener
            }

            //check if previous thread is stopped
            bkgdThread?.join()
            //set background thread to process get image
            bkgdThread = Thread{

                if (isBound==true && svc != null){
                    imagesUrl = svc?.getHtml(webUrl)!!
                    imageBitmap = svc?.getImage(imagesUrl)!!

                    //if image list is not empty, send the list of image to adapter
                    if(imageBitmap.isNotEmpty()&&imageBitmap.size==20){
                        Log.e("Download result","Image list is populated")
                        runOnUiThread {
                            val adapter = ImageAdapter(this, imageBitmap)
                            gridView.adapter = adapter
                        }
                    }
                    else{
                        Log.e("Service", "Service not bound or unavailable.")
                        runOnUiThread {
                            Toast.makeText(this, "Failed to fetch images or image not enough", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            bkgdThread?.start()
        }

        val stopBtn = findViewById<Button>(R.id.stopBtn)

        //stop fetching image
        stopBtn.setOnClickListener{
            bkgdThread?.interrupt()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound == true) {
            unbindService(conn)
            isBound = false
        }
    }


}

