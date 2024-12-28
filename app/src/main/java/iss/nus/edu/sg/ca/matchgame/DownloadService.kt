package iss.nus.edu.sg.ca.matchgame

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadService : Service() {

    inner class LocalBinder : Binder() {
        fun getService(): DownloadService {
            return this@DownloadService
        }
    }

    val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    //function to get image from given URL
    //return a list of bitmap (images)
    fun getImage(imgUrl: MutableList<String>): MutableList<Bitmap> {
        val tempImg: MutableList<Bitmap> = mutableListOf()
        //establish HttpURLConnection variable
        var conn: HttpURLConnection? = null

        for (i in imgUrl) {
            try {
                //check for thread interruption before starting the download
                if (Thread.interrupted()) {
                    Log.e("Thread", "Thread interrupted, stopping download.")
                    return mutableListOf() //return empty list if interrupted
                }

                Log.e("Simulating", "Downloading: $i")

                /*
                //simulate long download to interrupt
                Thread.sleep(5000)

                 */

                //parse as URL
                val url = URL(i);

                //open connection with HttpURLConnection
                conn = url.openConnection() as HttpURLConnection

                //Use get method
                conn.requestMethod = "GET"

                //set user-agent header as Mozilla
                conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                )

                //if response HTTP.OK
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("HTTP Response", "Image get successfully: $i")
                    //pass input stream of connection to show image
                    val inputStream: InputStream = conn.inputStream
                    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    tempImg.add(bitmap)
                } else {
                    Log.e("HTTP Response", "Failed to connect to image URL: $i")

                }

            } catch (e: InterruptedException) {
                // Handle interruption properly
                Log.e("Thread", "Thread was interrupted during sleep. Stopping download.")
                return mutableListOf()
            } catch (e: Exception) {
                Log.e("HTTP Response", "Failed to connect to image URL: ${e.message}")
            } finally {
                //disconnect the connection
                conn?.disconnect()
            }

        }
        return tempImg
    }

    fun getHtml(urlString: String): MutableList<String> {
        var imgUrl: MutableList<String> = mutableListOf()
        var conn: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
            )

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                imgUrl = readFromServer(conn)
            }
        } catch (e: Exception) {
            Log.e("HTML response", "Error: ${e.message}", e)
        } finally {
            conn?.disconnect()
        }
        return imgUrl;
    }

    fun readFromServer(conn: HttpURLConnection): MutableList<String> {
        val temp: MutableList<String> = mutableListOf()

        var i = 0;
        val inp = BufferedReader(InputStreamReader(conn.inputStream))
        val srcRegex = """<img\s+[^>]*src="([^"]+)"""".toRegex()

        var line: String? = inp.readLine()
        while (line != null && i < 20) {
            if (line.contains("<img")) {
                val matchResult = srcRegex.find(line)
                if (matchResult != null) {
                    val src = matchResult.groupValues[1]
                    if (src.contains(".jpg") || src.contains(".png")) {
                        temp.add(src)
                        i++
                    }
                }

            }
            line = inp.readLine()
        }

        inp.close()
        return temp
    }
}