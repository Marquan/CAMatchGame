package iss.nus.edu.sg.ca.matchgame

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID


class DownloadService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent?.action
        Log.e("DownloadService", "onStartCommand run ${action}")
        if (intent.action.equals("download_file")) {
            val url = intent.getStringExtra("url")
            var nameNoExt = intent.getStringExtra("nameNoExt")
            if (url != null) {
                if (nameNoExt == null) {
                    nameNoExt = ""
                }
                startImageDownload(url, nameNoExt)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    protected fun startImageDownload(imgURL: String, nameNoExt: String = "") {
        val ext = getFileExtension(imgURL)
        val file = createDestFile(ext, nameNoExt)
        Log.e("DownloadService","Save file created in ${file.absolutePath}")

        // creating and starting a background thread
        Thread {
            if (downloadImage(imgURL, file)) {
                val intent = Intent()
                intent.setAction("download_completed")
                intent.putExtra("file_path", file.absolutePath)
                Log.e("DownloadService","Download successful for ${file.absolutePath}")
                sendBroadcast(intent)
            }
        }.start()
    }

    private fun downloadImage(imgURL: String, file: File): Boolean {
        var conn : HttpURLConnection? = null

        try {
            val url = URL(imgURL)

            conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent","Mozilla");
            conn.requestMethod = "GET" // optional; default is GET

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                Log.e("DownloadService","HTTP_OK. Starting output to ${file.absolutePath}.")
                outputToFile(conn, file)
            } else {
                Log.e("DownloadService","responseCode is not HTTP_OK. Failed to start download for ${file.absolutePath}.")
            }

            return true
        } catch (e: Exception) {
            Log.e("DownloadService","Error when downloading ${file.absolutePath}.")
            return false
        } finally {
            conn?.disconnect()
        }
    }

    private fun outputToFile(conn: HttpURLConnection, file: File) {
        val inp = conn.inputStream
        val outp = FileOutputStream(file)

        // for storing incoming raw bytes from input-stream
        val buf = ByteArray(4096)

        // read in first 4096 bytes
        var bytesRead = inp.read(buf)

        while (bytesRead != -1) {   // -1 means no more bytes to read
            // write to file the number of bytes read
            outp.write(buf, 0, bytesRead)

            // read from input-stream again
            bytesRead = inp.read(buf)
        }

        inp.close()
        outp.close()
    }

    private fun getFileExtension(str: String) : String {
        return str.substring(str.lastIndexOf("."))
    }

    private fun createDestFile(ext: String, nameNoExt: String = "") : File {
        // using random string as filename to prevent filename clashes
        var filename = nameNoExt
        if (filename == "") {
            filename = UUID.randomUUID().toString()
        }
        filename = filename + ext
        //val filename = UUID.randomUUID().toString() + ext

        // storing under /files/PICTURES folder
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, filename)
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

}